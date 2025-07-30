package com.diogomekie.binderbuddy.backend.service;

import com.diogomekie.binderbuddy.backend.dto.AddCardToBinderRequest;
import com.diogomekie.binderbuddy.backend.dto.CardDto;
import com.diogomekie.binderbuddy.backend.dto.PokemonCardDataDto;
import com.diogomekie.binderbuddy.backend.model.Binder;
import com.diogomekie.binderbuddy.backend.model.Card;
import com.diogomekie.binderbuddy.backend.model.User;
import com.diogomekie.binderbuddy.backend.repository.BinderRepository;
import com.diogomekie.binderbuddy.backend.repository.CardRepository;
import com.diogomekie.binderbuddy.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final BinderRepository binderRepository;
    private final UserRepository userRepository;
    private final PokemonTcgApiService pokemonTcgApiService;

    public CardService(CardRepository cardRepository, BinderRepository binderRepository, UserRepository userRepository, PokemonTcgApiService pokemonTcgApiService) {
        this.cardRepository = cardRepository;
        this.binderRepository = binderRepository;
        this.userRepository = userRepository;
        this.pokemonTcgApiService = pokemonTcgApiService;
    }


    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilizador autenticado não encontrado."));
    }

    public List<PokemonCardDataDto> searchCardsFromApi(String cardName) {
        return pokemonTcgApiService.searchCards(cardName);
    }

    @Transactional
    public Optional<CardDto> addCardToBinder(AddCardToBinderRequest request) {
        User currentUser = getCurrentAuthenticatedUser();

        Optional<Binder> optionalBinder = binderRepository.findByIdAndUser(request.getBinderId(), currentUser);

        if (optionalBinder.isEmpty()) {
            throw new RuntimeException("Binder não encontrada ou não pertence ao utilizador autenticado.");
        }

        Binder binder = optionalBinder.get();

        if (cardRepository.findByApiCardIdAndBinder(request.getApiCardId(), binder).isPresent()) {
            throw new RuntimeException("Esta carta já existe nesta binder.");
        }

        Card card = new Card();
        card.setApiCardId(request.getApiCardId());
        card.setName(request.getName());
        card.setImageUrl(request.getImageUrl());
        card.setCardSet(request.getCardSet());
        card.setRarity(request.getRarity());
        card.setPrice(request.getPrice());
        card.setBinder(binder);

        Card savedCard = cardRepository.save(card);
        return Optional.of(convertToDto(savedCard));
    }

    @Transactional
    public boolean removeCardFromBinder(Long cardId) {
        User currentUser = getCurrentAuthenticatedUser();
        Optional<Card> optionalCard = cardRepository.findById(cardId);

        if (optionalCard.isEmpty()) {
            return false;
        }

        Card card = optionalCard.get();

        if (!card.getBinder().getUser().equals(currentUser)) {
            throw new RuntimeException("Não tem permissão para remover este card.");
        }

        cardRepository.delete(card);
        return true;
    }

    public List<CardDto> getCardsByBinder(Long binderId) {
        User currentUser = getCurrentAuthenticatedUser();
        Optional<Binder> optionalBinder = binderRepository.findByIdAndUser(binderId, currentUser);

        if (optionalBinder.isEmpty()) {
            throw new RuntimeException("Binder não encontrada ou não pertence ao utilizador autenticado.");
        }

        Binder binder = optionalBinder.get();
        return cardRepository.findByBinder(binder).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CardDto convertToDto(Card card) {
        return new CardDto(
                card.getId(),
                card.getApiCardId(),
                card.getName(),
                card.getImageUrl(),
                card.getCardSet(),
                card.getRarity(),
                card.getPrice()
        );
    }
}
