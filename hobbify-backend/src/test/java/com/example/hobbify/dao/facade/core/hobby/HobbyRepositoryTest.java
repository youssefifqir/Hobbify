package com.example.hobbify.dao.facade.core.hobby;

import com.example.hobbify.bean.core.hobby.Hobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@DisplayName("HobbyDao Integration Tests")
class HobbyRepositoryTest {

    @TestConfiguration
    @EnableJpaAuditing(auditorAwareRef = "testAuditorAware")
    static class AuditTestConfig {
        @Bean("testAuditorAware")
        AuditorAware<String> testAuditorAware() {
            return () -> Optional.of("test-user");
        }
    }

    @Autowired
    private HobbyDao dao;

    private Hobby sample;

    @BeforeEach
    void setUp() {
        sample = new Hobby();
        sample.setRef("hobby-test-ref-001");
        sample.setName("test-name");
        sample.setDescription("test-description");
        sample.setCategory("test-category");
    }

    @Nested
    @DisplayName("save")
    class Save {

        @Test
        @DisplayName("persists entity and assigns auto-generated ID")
        void persistsEntity() {
            Hobby saved = dao.save(sample);

            assertThat(saved.getId()).isNotNull().isPositive();
            assertThat(saved.getRef()).isEqualTo("hobby-test-ref-001");
        }

        @Test
        @DisplayName("audit fields are populated on save")
        void populatesAuditFields() {
            Hobby saved = dao.save(sample);

            assertThat(saved.getCreatedDate()).isNotNull();
            assertThat(saved.getCreatedBy()).isEqualTo("test-user");
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("returns saved entity by ID")
        void returnsSavedEntity() {
            Hobby saved = dao.save(sample);

            Optional<Hobby> found = dao.findById(saved.getId());

            assertThat(found).isPresent();
            assertThat(found.get().getId()).isEqualTo(saved.getId());
        }

        @Test
        @DisplayName("returns empty Optional for unknown ID")
        void returnsEmpty_whenNotFound() {
            Optional<Hobby> found = dao.findById(-999L);

            assertThat(found).isEmpty();
        }
    }

    @Nested
    @DisplayName("findByRef")
    class FindByRef {

        @Test
        @DisplayName("returns entity matching the given ref")
        void returnsEntityByRef() {
            dao.save(sample);

            Hobby found = dao.findByRef("hobby-test-ref-001");

            assertThat(found).isNotNull();
            assertThat(found.getRef()).isEqualTo("hobby-test-ref-001");
        }

        @Test
        @DisplayName("returns null for unknown ref")
        void returnsNull_whenRefNotFound() {
            Hobby found = dao.findByRef("non-existent-ref");

            assertThat(found).isNull();
        }
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("returns all persisted entities")
        void returnsAllEntities() {
            dao.save(sample);

            assertThat(dao.findAll()).hasSize(1);
        }

        @Test
        @DisplayName("returns empty list when no entities exist")
        void returnsEmptyList_whenNoneExist() {
            assertThat(dao.findAll()).isEmpty();
        }
    }

    @Nested
    @DisplayName("deleteById")
    class DeleteById {

        @Test
        @DisplayName("removes entity from the database")
        void removesEntity() {
            Hobby saved = dao.save(sample);
            Long id = saved.getId();

            dao.deleteById(id);

            assertThat(dao.findById(id)).isEmpty();
        }

        @Test
        @DisplayName("count decreases after deletion")
        void decreasesCount() {
            dao.save(sample);
            long countBefore = dao.count();

            dao.deleteById(dao.findAll().get(0).getId());

            assertThat(dao.count()).isEqualTo(countBefore - 1);
        }
    }
}
