package com.example.hobbify.service.impl.favorite;

import com.example.hobbify.bean.core.favorite.Favorite;
import com.example.hobbify.dao.facade.core.favorite.FavoriteDao;
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
@DisplayName("FavoriteService Unit Tests")
class FavoriteServiceTest {

    @Mock
    private FavoriteDao dao;

    @InjectMocks
    private FavoriteServiceImpl service;

    private Favorite sample;

    @BeforeEach
    void setUp() {
        sample = new Favorite();
        sample.setId(1L);
        sample.setRef("favorite-ref-001");
    }

    @Nested
    @DisplayName("findAll")
    class FindAll {

        @Test
        @DisplayName("returns all entities when records exist")
        void returnsEntities() {
            when(dao.findAll()).thenReturn(List.of(sample));

            List<Favorite> result = service.findAll();

            assertThat(result).hasSize(1).contains(sample);
            verify(dao).findAll();
        }

        @Test
        @DisplayName("returns empty list when no records exist")
        void returnsEmptyList() {
            when(dao.findAll()).thenReturn(Collections.emptyList());

            List<Favorite> result = service.findAll();

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

            Optional<Favorite> result = service.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(1L);
            verify(dao).findById(1L);
        }

        @Test
        @DisplayName("returns null when not found")
        void returnsNull_whenNotFound() {
            when(dao.findById(99L)).thenReturn(Optional.empty());

            Optional<Favorite> result = service.findById(99L);

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
            Favorite toSave = new Favorite();
            when(dao.save(any(Favorite.class))).thenReturn(sample);

            Favorite result = service.save(toSave);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            verify(dao).save(any(Favorite.class));
        }

        @Test
        @DisplayName("generates ref when not present")
        void generatesRef_whenMissing() {
            Favorite toSave = new Favorite();
            when(dao.save(any(Favorite.class))).thenAnswer(inv -> inv.getArgument(0));

            Favorite result = service.save(toSave);

            assertThat(result.getRef()).isNotBlank();
        }

        @Test
        @DisplayName("preserves existing ref when already set")
        void preservesRef_whenAlreadySet() {
            Favorite toSave = new Favorite();
            toSave.setRef("existing-ref");
            when(dao.save(any(Favorite.class))).thenAnswer(inv -> inv.getArgument(0));

            Favorite result = service.save(toSave);

            assertThat(result.getRef()).isEqualTo("existing-ref");
        }

        @Test
        @DisplayName("returns null when entity is null")
        void returnsNull_whenEntityIsNull() {
            Favorite result = service.save(null);

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
            Favorite toCreate = new Favorite();
            when(dao.save(any(Favorite.class))).thenReturn(sample);

            Favorite result = service.create(toCreate);

            assertThat(result).isNotNull();
            verify(dao).save(any(Favorite.class));
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
            when(dao.findByRef("favorite-ref-001")).thenReturn(sample);

            Favorite result = service.findByRef("favorite-ref-001");

            assertThat(result).isNotNull();
            assertThat(result.getRef()).isEqualTo("favorite-ref-001");
            verify(dao).findByRef("favorite-ref-001");
        }

        @Test
        @DisplayName("returns null for blank ref without hitting the DAO")
        void returnsNull_whenRefIsBlank() {
            Favorite result = service.findByRef("  ");

            assertThat(result).isNull();
            verifyNoInteractions(dao);
        }
    }
}
