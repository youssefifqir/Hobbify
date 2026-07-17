# Deploying Hobbify (Docker-free, free-tier)

This guide deploys the whole stack without Docker, using only free tiers:

- **Neon** — managed Postgres (replaces the local `docker-compose` Postgres container)
- **Render** — runs the Spring Boot backend as a native Java web service
- **Vercel** — builds and serves the Vite/React frontend as a static site

```
Browser  →  Vercel (static frontend)  →  Render (Spring Boot API)  →  Neon (Postgres)
```

Do these in order — each step needs a value produced by the previous one.

---

## 0. Before you start

Push this repo to GitHub (Render and Vercel both deploy from a Git repo, not a local folder). Both `hobbify-backend/` and `hobbify-frontend/` live in the same repo, so both Render and Vercel will point at the same repo but a different **root directory**.

Two backend changes were made specifically so this deploy works — worth knowing about, not just trivia:

- **Database connection** (`application-prod.yml`) now reads `SPRING_DATASOURCE_URL` / `SPRING_DATASOURCE_USERNAME` / `DB_PASSWORD` from the environment instead of a hardcoded `db:5432` hostname (that hostname only existed inside the old `docker-compose` network — it would never resolve on Render).
- **JWT signing keys**: the backend signs tokens with an RSA key pair. Locally it generates and caches one on disk. Render's filesystem is **ephemeral** — anything written at runtime disappears on the next restart or scale-to-zero, which on a free tier happens after 15 minutes of inactivity. Without a fix, every restart would silently log out every signed-in user. The backend now checks for `JWT_PRIVATE_KEY_B64` / `JWT_PUBLIC_KEY_B64` env vars first, before falling back to the disk-based behavior — Part 2 below generates these for you. **Do not skip that step.**

---

## 1. Neon — Postgres database

1. Create a free account at neon.tech, then **New Project**. Any region close to where you'll deploy Render is fine (Render's Oregon/Frankfurt free-tier regions are common — pick a Neon region in the same continent to reduce latency, it's not a hard requirement).
2. Name the database `hobbify` (or accept the default `neondb` — either works, you'll reference whatever you pick).
3. Open the project's **Connection Details** panel. Neon shows two connection strings:
   - **Pooled connection** (goes through PgBouncer) — do **not** use this one. Spring Boot's HikariCP already pools connections itself; stacking it on top of PgBouncer's transaction-mode pooling causes prepared-statement errors with Hibernate.
   - **Direct connection** — use this one.
4. From the direct connection string, e.g.:
   ```
   postgresql://alex:AbC123xyz@ep-cool-firefly-12345.us-east-2.aws.neon.tech/neondb?sslmode=require
   ```
   you need three separate pieces for Render in Part 2:
   - **JDBC URL**: `jdbc:postgresql://ep-cool-firefly-12345.us-east-2.aws.neon.tech/neondb?sslmode=require` (same host/db/params, `jdbc:` prefix, no credentials in the URL)
   - **Username**: `alex`
   - **Password**: `AbC123xyz`

Keep this tab open — you'll copy these into Render next.

---

## 2. Render — backend

1. Create a free account at render.com, then **New → Web Service**, and connect the GitHub repo.
2. Configure the service:
   - **Root Directory**: `hobbify-backend`
   - **Runtime**: `Java`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/hobbify-0.0.1-SNAPSHOT.jar`
   - **Instance Type**: Free
3. Generate a production RSA key pair — **don't reuse your local dev one**. Run this on your machine (requires `openssl`, present on macOS/Linux/Git Bash):
   ```bash
   openssl genpkey -algorithm RSA -pkcs8 -out private_key.pem -pkeyopt rsa_keygen_bits:2048
   openssl rsa -pubout -in private_key.pem -out public_key.pem

   # Base64-encode both (one line each) — this is what goes into Render:
   base64 -w0 private_key.pem   # macOS: base64 -i private_key.pem | tr -d '\n'
   base64 -w0 public_key.pem
   ```
   Copy each base64 blob — you'll paste them as env vars below. Then delete the two `.pem` files from your machine (or keep them somewhere safe outside the repo — they must never be committed).
4. Under **Environment**, add:

   | Key | Value |
   |---|---|
   | `SPRING_PROFILES_ACTIVE` | `prod` |
   | `SPRING_DATASOURCE_URL` | the JDBC URL from Neon (step 1.4) |
   | `SPRING_DATASOURCE_USERNAME` | the Neon username |
   | `DB_PASSWORD` | the Neon password |
   | `JWT_PRIVATE_KEY_B64` | the base64 blob from step 3 |
   | `JWT_PUBLIC_KEY_B64` | the base64 blob from step 3 |
   | `ADMIN_EMAIL` | your real admin email |
   | `ADMIN_PASSWORD` | a strong password — **set this explicitly**; if left unset, a random one is generated and only printed once to the Render log |
   | `DEMO_USER_ENABLED` | `true` to seed a pre-approved demo login, `false` to skip it |
   | `DEMO_USER_EMAIL` | e.g. `demo@hobbify.com` (only matters if enabled) |
   | `DEMO_USER_PASSWORD` | a password you're comfortable sharing with anyone demoing the app |
   | `CORS_ALLOWED_ORIGINS` | leave blank for now — comes back in Part 4 |

5. Deploy. Watch the build logs. On first successful boot, Flyway runs all five migrations automatically (schema, then hobby image columns, then the seeded achievements and hobby/stage/step catalog) — no manual SQL step needed. The `DataInitializer` and `DemoUserSeeder` also run automatically and create the admin and demo accounts.
6. Once live, note the public URL Render gives you, e.g. `https://hobbify-backend.onrender.com`. Confirm it works:
   ```bash
   curl https://hobbify-backend.onrender.com/api/v1/catalog/hobbies
   ```
   You should get back the seeded hobby list as JSON.

**Free-tier note**: the service spins down after 15 minutes with no traffic and takes ~30-50 seconds to wake back up on the next request. That's expected, not a bug — the JWT key fix above just makes sure that a sleep/wake cycle doesn't invalidate anyone's session.

---

## 3. Vercel — frontend

1. Create a free account at vercel.com, then **Add New → Project**, and import the same GitHub repo.
2. Configure the project:
   - **Root Directory**: `hobbify-frontend`
   - **Framework Preset**: Vite (auto-detected)
   - **Build Command**: `npm run build` (default)
   - **Output Directory**: `dist` (default)
3. Under **Environment Variables**, add:

   | Key | Value |
   |---|---|
   | `VITE_API_BASE_URL` | your Render URL + `/api/v1`, e.g. `https://hobbify-backend.onrender.com/api/v1` |

   **This is baked into the static build at build time**, not read at runtime — if you ever change the backend URL later, you must redeploy (a plain env var change alone does nothing until the next build).
4. Deploy. `vercel.json` (already in the repo) rewrites every path to `index.html`, which React Router needs — without it, refreshing any route other than `/` would 404.
5. Note the deployed URL, e.g. `https://hobbify.vercel.app`.

---

## 4. Wire them together: CORS

The backend doesn't yet know your Vercel domain is allowed to call it. Go back to Render:

1. Set the `CORS_ALLOWED_ORIGINS` env var to your exact Vercel URL (no trailing slash): `https://hobbify.vercel.app`. Comma-separate multiple values if you also have a custom domain.
2. Save — Render redeploys automatically on env var changes.

Without this, every request from the deployed frontend will fail as an opaque network error in the browser (CORS-blocked), even though `curl` against the same endpoint works fine — CORS is a browser-only restriction, so it's an easy thing to falsely rule out by testing with `curl` alone.

---

## 5. Verify end to end

1. Open the Vercel URL. The landing page and Discover grid should show the six seeded hobbies with local stock photos.
2. Sign in with the demo account (`DEMO_USER_EMAIL` / `DEMO_USER_PASSWORD`), or register a new account — new registrations are disabled until an admin activates them (Users page in the admin panel, signed in as `ADMIN_EMAIL`).
3. As the demo user: open a hobby, start its journey, complete a step. Confirm it persists after a refresh, and that an achievement unlocks after enough steps.
4. As the admin: sign in, confirm you land on `/admin` (not the consumer dashboard), and that Content Library shows the 6 seeded hobbies with correct stage/step counts.

---

## What ships automatically (no manual seeding step)

Everything below runs the first time the backend boots against a fresh Neon database — nothing to run by hand:

- **Flyway migrations** (`hobbify-backend/src/main/resources/db/migration/`) — schema, then hobby image support, then column widening, then seed data for achievements and the hobby/stage/step catalog.
- **`DataInitializer`** — creates the admin account from `ADMIN_EMAIL`/`ADMIN_PASSWORD`.
- **`DemoUserSeeder`** — creates one pre-approved, already-verified demo account (skips if `DEMO_USER_ENABLED=false`).

To reseed from scratch, drop the Neon database and recreate it (empty) before the next deploy — Flyway will replay every migration in order.

## Common failure modes

| Symptom | Cause |
|---|---|
| Frontend loads but all API calls fail with a generic network error | `CORS_ALLOWED_ORIGINS` on Render doesn't match the Vercel URL exactly (check for trailing slash, http vs https) |
| Backend fails to boot with a Hibernate schema-validation error | `SPRING_DATASOURCE_URL` pointing at a Neon database that already has a differently-shaped `public` schema from a previous attempt — use a fresh database |
| Everyone gets logged out ~15-50 minutes after the last request | `JWT_PRIVATE_KEY_B64` / `JWT_PUBLIC_KEY_B64` not set — the backend generated a throwaway key pair that didn't survive the free-tier sleep/wake cycle |
| Frontend built successfully but every API call goes to `localhost:8080` | `VITE_API_BASE_URL` wasn't set **before** the Vercel build ran — set it and trigger a new deploy, an env var change alone doesn't retroactively affect an already-built bundle |
