package com.diogomekie.binderbuddy.backend.controllers;

import com.diogomekie.binderbuddy.backend.dto.AddCardToBinderRequest;
import com.diogomekie.binderbuddy.backend.dto.CardDto;
import com.diogomekie.binderbuddy.backend.service.CardService;
import com.diogomekie.binderbuddy.backend.dto.PokemonCardDataDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<PokemonCardDataDto>> searchCards(@RequestParam String name) {
        List<PokemonCardDataDto> cards = cardService.searchCardsFromApi(name);
        return ResponseEntity.ok(cards);
    }

    @PostMapping
    public ResponseEntity<CardDto> addCardToBinder(@RequestBody AddCardToBinderRequest request) {
        try {
            CardDto newCard = cardService.addCardToBinder(request)
                    .orElseThrow(() -> new RuntimeException("Falha ao adicionar card à binder."));
            return new ResponseEntity<>(newCard, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/binder/{binderId}")
    public ResponseEntity<List<CardDto>> getCardsByBinder(@PathVariable Long binderId) {
        try {
            List<CardDto> cards = cardService.getCardsByBinder(binderId);
            return ResponseEntity.ok(cards);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> removeCardFromBinder(@PathVariable Long cardId) {
        try {
            if (cardService.removeCardFromBinder(cardId)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
