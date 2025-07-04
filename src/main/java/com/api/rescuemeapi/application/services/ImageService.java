package com.api.rescuemeapi.application.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.api.rescuemeapi.api.producers.CreateImagesSagaPublisher;
import com.api.rescuemeapi.api.producers.DeleteImageSagaPublisher;
import com.api.rescuemeapi.api.producers.DeleteImagesByReferenceSagaPublisher;
import com.api.rescuemeapi.domain.dtos.image.CreateImagesCommand;
import com.api.rescuemeapi.domain.dtos.image.DeleteImageCommand;
import com.api.rescuemeapi.domain.dtos.image.DeleteImagesByReferenceCommand;
import com.api.rescuemeapi.domain.dtos.image.ImagePayload;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final CreateImagesSagaPublisher createImagesSagaPublisher;
    private final DeleteImageSagaPublisher deleteImageSagaPublisher;
    private final DeleteImagesByReferenceSagaPublisher deleteImagesByReferenceSagaPublisher;

    public void createImages(List<MultipartFile> files, UUID referenceId) {
        List<ImagePayload> imagePayloads = new ArrayList<ImagePayload>();
        AtomicInteger orderIndex = new AtomicInteger();

        files.forEach(file -> {
            String imageBase64 = convertToBase64(file);
            imagePayloads.add(
                    ImagePayload.builder()
                            .base64Data(imageBase64)
                            .filename(file.getOriginalFilename())
                            .orderIndex(orderIndex.incrementAndGet())
                            .build());
        });

        UUID sagaId = UUID.randomUUID();
        createImagesSagaPublisher.publishCreateImagesCommand(
                CreateImagesCommand.builder()
                        .sagaId(sagaId)
                        .referenceId(referenceId)
                        .images(imagePayloads)
                        .build());
    }

    public void deleteImage(Long imageId, UUID referenceId) {
        deleteImageSagaPublisher.publishDeleteImageCommand(
                DeleteImageCommand.builder()
                        .imageId(imageId)
                        .referenceId(referenceId)
                        .build());
    }

    public void deleteImages(UUID referenceId) {
        deleteImagesByReferenceSagaPublisher
                .publishDeleteImagesByReferenceCommand(
                        DeleteImagesByReferenceCommand.builder()
                                .referenceId(referenceId)
                                .build());
    }

    private String convertToBase64(MultipartFile file) {
        try {
            byte[] fileBytes = file.getBytes();
            return Base64.getEncoder().encodeToString(fileBytes);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
