package com.api.rescuemeapi.application.mappers;

import com.api.rescuemeapi.domain.dtos.petition.PetitionResponse;
import com.api.rescuemeapi.domain.models.Petition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class PetitionResponseMapper implements Function<Petition, PetitionResponse> {

    private final UserResponseMapper userResponseMapper;
    private final PetResponseMapper petResponseMapper;

    @Override
    public PetitionResponse apply(Petition petition) {
        return PetitionResponse.builder()
                .id(petition.getId())
                .message(petition.getMessage())
                .status(petition.getStatus())
                .requestedPet(petResponseMapper.apply(petition.getRequestedPet()))
                .requestingUser(userResponseMapper.apply(petition.getRequestingUser())).build();
    }
}

