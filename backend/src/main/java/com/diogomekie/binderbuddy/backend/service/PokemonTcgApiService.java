package com.diogomekie.binderbuddy.backend.service;

import com.diogomekie.binderbuddy.backend.dto.PokemonCardDataDto;
import com.diogomekie.binderbuddy.backend.dto.PokemonTcgApiResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;


@Service
public class PokemonTcgApiService {

    private final RestTemplate restTemplate;
    @Value("${pokemontcg.api.base-url}")
    private String baseUrl;
    @Value("${pokemontcg.api.key}")
    private String apiKey;

    public PokemonTcgApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<PokemonCardDataDto> searchCards(String cardName) {
        String url = baseUrl + "/cards?q=name:" + cardName;
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("X-Api-Key", apiKey);
            return execution.execute(request, body);
        });

        PokemonTcgApiResponseDto response = restTemplate.getForObject(url, PokemonTcgApiResponseDto.class);

        return Optional.ofNullable(response)
                .map(res -> res.data)
                .orElse(List.of());
    }
}

