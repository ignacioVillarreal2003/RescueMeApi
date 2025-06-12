package com.api.rescuemeapi.application.helpers;

import com.api.rescuemeapi.domain.dtos.image.CreateImageRequest;
import com.api.rescuemeapi.domain.dtos.image.DeleteImageRequest;
import com.api.rescuemeapi.domain.dtos.image.DeleteImagesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUtils {

    /*private final RabbitMQProducer rabbitMQProducer;*/

    public void createImage(List<MultipartFile> files, UUID referenceId) {
        /*files.forEach(file -> {
            String imageBase64 = convertToBase64(file);
            rabbitMQProducer.sendMessageCreateImage(CreateImageRequest.builder()
                    .imageBase64(imageBase64)
                    .imageName(file.getName())
                    .referenceId(referenceId)
                    .build());
        });*/
    }

    public void deleteImage(Long petId, UUID referenceId) {
        /*rabbitMQProducer.sendMessageDeleteImage(DeleteImageRequest.builder()
                .imageId(petId)
                .referenceId(referenceId)
                .build());*/
    }

    public void deleteImages(UUID referenceId) {
        /*rabbitMQProducer.sendMessageDeleteImages(DeleteImagesRequest.builder()
                .referenceId(referenceId)
                .build());*/
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
