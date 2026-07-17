package com.example.hobbify.service.impl.hobby;

import com.example.hobbify.bean.core.hobby.Hobby;
import com.example.hobbify.dao.facade.core.hobby.HobbyDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HobbyService Unit Tests")
class HobbyServiceTest {

    @Mock
    private HobbyDao dao;

    @InjectMocks
    private HobbyServiceImpl service;

    private Hobby sample;

    @BeforeEach
    void setUp() {
        sample = new Hobby();
        sample.setId(1L);
        sample.setRef("hobby-ref-001");
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("returns all entities when records exist")
        void returnsEntities() {
            when(dao.findAll()).thenReturn(List.of(sample));

            List<Hobby> result = service.findAll();

            assertThat(result).hasSize(1).contains(sample);
            verify(dao).findAll();
        }

        @Test
        @DisplayName("returns empty list when no records exist")
        void returnsEmptyList() {
            when(dao.findAll()).thenReturn(Collections.emptyList());

            List<Hobby> result = service.findAll();

            assertThat(result).isEmpty();
            verify(dao).findAll();
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("returns entity when found")
        void returnsEntity_whenFound() {
            when(dao.findById(1L)).thenReturn(Optional.of(sample));

            Optional<Hobby> result = service.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
            verify(dao).findById(1L);
        }

        @Test
        @DisplayName("returns null when not found")
        void returnsNull_whenNotFound() {
            when(dao.findById(99L)).thenReturn(Optional.empty());

            Optional<Hobby> result = service.findById(99L);

            assertThat(result).isNotPresent();
            verify(dao).findById(99L);
        }
    }

    @Nested
    @DisplayName("save")
    class Save {

        @Test
        @DisplayName("persists entity and returns saved instance")
        void persistsEntity() {
            Hobby toSave = new Hobby();
            when(dao.save(any(Hobby.class))).thenReturn(sample);

            Hobby result = service.save(toSave);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            verify(dao).save(any(Hobby.class));
        }

        @Test
        @DisplayName("generates ref when not present")
        void generatesRef_whenMissing() {
            Hobby toSave = new Hobby();
            when(dao.save(any(Hobby.class))).thenAnswer(inv -> inv.getArgument(0));

            Hobby result = service.save(toSave);

            assertThat(result.getRef()).isNotBlank();
        }

        @Test
        @DisplayName("preserves existing ref when already set")
        void preservesRef_whenAlreadySet() {
            Hobby toSave = new Hobby();
            toSave.setRef("existing-ref");
            when(dao.save(any(Hobby.class))).thenAnswer(inv -> inv.getArgument(0));

            Hobby result = service.save(toSave);

            assertThat(result.getRef()).isEqualTo("existing-ref");
        }

        @Test
        @DisplayName("returns null when entity is null")
        void returnsNull_whenEntityIsNull() {
            Hobby result = service.save(null);

            assertThat(result).isNull();
            verifyNoInteractions(dao);
        }
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("delegates to save")
        void delegatesToSave() {
            Hobby toCreate = new Hobby();
            when(dao.save(any(Hobby.class))).thenReturn(sample);

            Hobby result = service.create(toCreate);

            assertThat(result).isNotNull();
            verify(dao).save(any(Hobby.class));
        }
    }

    @Nested
    @DisplayName("deleteById")
    class DeleteById {

        @Test
        @DisplayName("deletes entity when found")
        void deletesEntity_whenFound() {
            when(dao.findById(1L)).thenReturn(Optional.of(sample));
            doNothing().when(dao).deleteById(1L);

            service.deleteById(1L);

            verify(dao).findById(1L);
            verify(dao).deleteById(1L);
        }

        @Test
        @DisplayName("does nothing when entity not found")
        void doesNothing_whenNotFound() {
            when(dao.findById(99L)).thenReturn(Optional.empty());

            service.deleteById(99L);

            verify(dao).findById(99L);
            verify(dao, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("does nothing when id is null")
        void doesNothing_whenIdIsNull() {
            service.deleteById(null);

            verifyNoInteractions(dao);
        }
    }

    @Nested
    @DisplayName("findByRef")
    class FindByRef {

        @Test
        @DisplayName("returns entity matching ref")
        void returnsEntity_whenRefMatches() {
            when(dao.findByRef("hobby-ref-001")).thenReturn(sample);

            Hobby result = service.findByRef("hobby-ref-001");

            assertThat(result).isNotNull();
            assertThat(result.getRef()).isEqualTo("hobby-ref-001");
            verify(dao).findByRef("hobby-ref-001");
        }

        @Test
        @DisplayName("returns null for blank ref without hitting the DAO")
        void returnsNull_whenRefIsBlank() {
            Hobby result = service.findByRef("  ");

            assertThat(result).isNull();
            verifyNoInteractions(dao);
        }
    }
}
