package com.example.hobbify.config.security.authz;

import com.example.hobbify.config.security.authz.PrincipalContext;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Row-filtering face of the policy engine (RBAC_V3 §6.4) — replaces the v1
 * {@code buildOwnershipSpec} helper. For list/page/criteria/count queries:
 *
 * <pre>
 * P_permit = OR( toPredicate(s) for every applicable PERMIT statement matching READ )
 *          OR ( EXISTS active grant matching READ )                    [grants module]
 * P_deny   = OR( toPredicate(s) for every applicable DENY  statement matching READ )
 * result   = P_permit AND NOT(P_deny)
 * </pre>
 *
 * ALL-scope permits contribute TRUE (null Specification = no filter); an ALL-scope deny
 * yields zero rows. Statements are pre-filtered by principal applicability (role held /
 * PUBLIC floor / grant-derived) using the SAME rule as {@code PolicyEngine.decide()} —
 * the list you can query and the rows you can touch stay the same set.
 *
 * <p><b>Grant/exclusive-ACL scope note (RBAC_V3 §6.3 step 3, §6.4):</b> the grant-existence
 * predicate below covers both regular and {@code exclusive} grants for VISIBILITY — a shared
 * resource shows up in the grantee's list either way. The "exclusive restricts evaluation to
 * level-0 statements" rule DOES matter for membership-derived permits (§6.1 step 2, level ≥ 1):
 * a row carrying an active exclusive grant for this principal is excluded from the membership
 * permit predicate below, mirroring {@code PolicyEngine.decide()}'s
 * {@code !exclusiveAclApplies} gate on {@code membershipStatementsFor()}. Deny statements are
 * exempt from that gate either way — they pierce exclusivity per §6.3/§10.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationSpecificationAdvisor {

    private final PolicyEngine engine;
    private final StaticPolicyRegistry registry;

    /**
     * Build the row filter for a READ list query on {@code entityType}.
     * Returns {@code null} when no filtering is required (unconditional permit, no denies).
     */
    public <T> Specification<T> forList(Class<T> entityType) {
        PrincipalContext.Snapshot principal = PrincipalContext.currentOrAnonymous();
        String entityKey = entityType.getSimpleName();

        // §14.4 — service principals: allowlist only, all-or-nothing per action.
        if (principal.isServicePrincipal()) {
            boolean allowed = principal.servicePrincipalActions().contains("*")
                    || principal.servicePrincipalActions().contains(entityKey + ":READ");
            return allowed ? null : none();
        }

        List<Statement> permits = new ArrayList<>();
        List<Statement> denies = new ArrayList<>();
        for (Statement s : registry.forEntity(entityKey)) {
            if (!engine.appliesToPrincipal(s, principal)) continue;
            if (!engine.matchesAction(s, entityKey, "READ")) continue;
            (s.effect() == Decision.Effect.PERMIT ? permits : denies).add(s);
        }

        // An unconditional ALL permit (no when-condition) makes the compiled-statement side of
        // P_permit TRUE outright — nothing to OR against it.
        boolean unconditionalPermit = permits.stream()
                .anyMatch(s -> s.scope() == Statement.Scope.ALL
                        && (s.whenJava() == null || s.whenJava().isBlank() || "true".equals(s.whenJava())));

        Specification<T> permitSpec;
        if (unconditionalPermit) {
            permitSpec = null; // null = "no filter" = every row passes the permit side
        } else if (!permits.isEmpty()) {
            permitSpec = orCombine(permits, principal);
        } else {
            // No explicit compiled permit: defaultPolicy decides the floor. "read" ⇒ unfiltered
            // (grants/denies still apply below); "deny" ⇒ grants are the ONLY way in.
            Decision def = engine.defaultFor(entityKey, "READ");
            permitSpec = def.isDeny() ? none() : null;
        }


        Specification<T> denySpec = denies.isEmpty() ? null : notDenies(denies, principal);
        if (denySpec == null) {
            return permitSpec;
        }
        return permitSpec == null ? denySpec : permitSpec.and(denySpec);
    }




    /** OR of every permit statement's criteria predicate. */
    private <T> Specification<T> orCombine(List<Statement> permits, PrincipalContext.Snapshot principal) {
        return (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            for (Statement s : permits) {
                preds.add(s.criteriaCheck().build(root, cb, principal));
            }
            return cb.or(preds.toArray(new Predicate[0]));
        };
    }

    /** NOT(OR of every deny statement's predicate). ALL-scope deny ⇒ conjunction ⇒ NOT(true) ⇒ zero rows. */
    private <T> Specification<T> notDenies(List<Statement> denies, PrincipalContext.Snapshot principal) {
        return (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            for (Statement s : denies) {
                preds.add(s.criteriaCheck().build(root, cb, principal));
            }
            return cb.not(cb.or(preds.toArray(new Predicate[0])));
        };
    }

    private <T> Specification<T> none() {
        return (root, query, cb) -> cb.disjunction();
    }
}
