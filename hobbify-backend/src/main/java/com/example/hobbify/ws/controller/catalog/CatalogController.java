package com.example.hobbify.ws.controller.catalog;

import com.example.hobbify.bean.core.hobby.Hobby;
import com.example.hobbify.bean.core.stage.Stage;
import com.example.hobbify.bean.core.step.Step;
import com.example.hobbify.service.facade.hobby.HobbyService;
import com.example.hobbify.service.facade.stage.StageService;
import com.example.hobbify.service.facade.step.StepService;
import com.example.hobbify.ws.converter.hobby.HobbyConverter;
import com.example.hobbify.ws.dto.hobby.response.HobbyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final HobbyService hobbyService;
    private final StageService stageService;
    private final StepService stepService;
    private final HobbyConverter hobbyConverter;

    // The list endpoint used to return the flat HobbyResponse (no nested stages/steps at
    // all), so every page reading from the shared catalog list — admin content library
    // counts, the consumer guide, dashboard, my-progress — saw 0 stages/0 steps for every
    // hobby regardless of what was actually authored. Every hobby now gets its full,
    // ordered stage/step tree here too, built with two bulk queries instead of the
    // flat converter so counts and guide content agree everywhere.
    @GetMapping("/hobbies")
    public ResponseEntity<List<CatalogHobbyResponse>> listHobbies() {
        final Map<Long, List<Step>> stepsByStage = stepService.findAll().stream()
                .collect(Collectors.groupingBy(step -> step.getStage().getId()));
        final Map<Long, List<Stage>> stagesByHobby = stageService.findAll().stream()
                .collect(Collectors.groupingBy(stage -> stage.getHobby().getId()));

        return ResponseEntity.ok(hobbyService.findAll().stream()
                .sorted(Comparator.comparing(hobby -> hobby.getName().toLowerCase()))
                .map(hobby -> toCatalogResponse(hobby, stagesByHobby, stepsByStage))
                .toList());
    }

    @GetMapping("/hobbies/{id}")
    public ResponseEntity<CatalogHobbyResponse> getHobby(@PathVariable final Long id) {
        final var hobby = hobbyService.findById(id).orElse(null);
        if (hobby == null) {
            return ResponseEntity.notFound().build();
        }

        final Map<Long, List<Step>> stepsByStage = stepService.findAll().stream()
                .collect(Collectors.groupingBy(step -> step.getStage().getId()));
        final Map<Long, List<Stage>> stagesByHobby = stageService.findAll().stream()
                .filter(stage -> stage.getHobby().getId().equals(id))
                .collect(Collectors.groupingBy(stage -> stage.getHobby().getId()));

        return ResponseEntity.ok(toCatalogResponse(hobby, stagesByHobby, stepsByStage));
    }

    private CatalogHobbyResponse toCatalogResponse(
            final Hobby hobby,
            final Map<Long, List<Stage>> stagesByHobby,
            final Map<Long, List<Step>> stepsByStage) {
        final HobbyResponse response = hobbyConverter.toResponse(hobby);

        final List<CatalogStageResponse> stages = stagesByHobby.getOrDefault(hobby.getId(), List.of()).stream()
                .sorted(Comparator.comparing(stage -> stage.getOrder() == null ? BigDecimal.ZERO : stage.getOrder()))
                .map(stage -> new CatalogStageResponse(
                        stage.getId(),
                        stage.getRef(),
                        stage.getTitle(),
                        stage.getOrder(),
                        stepsByStage.getOrDefault(stage.getId(), List.of()).stream()
                                .sorted(Comparator.comparing(step -> step.getOrder() == null ? BigDecimal.ZERO : step.getOrder()))
                                .map(step -> new CatalogStepResponse(
                                        step.getId(), step.getRef(), step.getTitle(), step.getContent(),
                                        step.getOrder(), step.getEstimatedMinutes()))
                                .toList()))
                .toList();

        return new CatalogHobbyResponse(
                response.getId(), response.getRef(), response.getName(), response.getDescription(),
                response.getCategory(), response.getCostTier(), response.getSpaceNeeded(),
                response.getTimeCommitment(), response.getDifficulty(), response.getStatus(),
                response.getIcon(), response.getImageData(), stages);
    }

    public record CatalogHobbyResponse(
            Long id, String ref, String name, String description, String category,
            Object costTier, Object spaceNeeded, Object timeCommitment, Object difficulty, Object status,
            String icon, String imageData,
            List<CatalogStageResponse> stages
    ) {}

    public record CatalogStageResponse(Long id, String ref, String title, BigDecimal order,
                                       List<CatalogStepResponse> steps) {}

    public record CatalogStepResponse(Long id, String ref, String title, String content,
                                      BigDecimal order, BigDecimal estimatedMinutes) {}
}
