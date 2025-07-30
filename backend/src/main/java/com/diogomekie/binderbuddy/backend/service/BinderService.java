package com.diogomekie.binderbuddy.backend.service;

import com.diogomekie.binderbuddy.backend.dto.BinderDto;
import com.diogomekie.binderbuddy.backend.dto.CardDto;
import com.diogomekie.binderbuddy.backend.dto.CreateBinderRequest;
import com.diogomekie.binderbuddy.backend.model.Binder;
import com.diogomekie.binderbuddy.backend.model.Card;
import com.diogomekie.binderbuddy.backend.model.User;
import com.diogomekie.binderbuddy.backend.repository.BinderRepository;
import com.diogomekie.binderbuddy.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BinderService {

    private final BinderRepository binderRepository;
    private final UserRepository userRepository;

    public BinderService(BinderRepository binderRepository, UserRepository userRepository) {
        this.binderRepository = binderRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilizador autenticado não encontrado."));
    }

    @Transactional
    public BinderDto createBinder(CreateBinderRequest request) {
        User currentUser = getCurrentAuthenticatedUser();
        Binder binder = new Binder();
        binder.setName(request.getName());
        binder.setDescription(request.getDescription());
        binder.setUser(currentUser);
        Binder savedBinder = binderRepository.save(binder);
        return convertToDto(savedBinder);
    }

    public List<BinderDto> getAllBinders() {
        User currentUser = getCurrentAuthenticatedUser();
        List<Binder> binders = binderRepository.findByUser(currentUser);
        return binders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Optional<BinderDto> getBinderById(Long binderId) {
        User currentUser = getCurrentAuthenticatedUser();
        return binderRepository.findByIdAndUser(binderId, currentUser)
                .map(this::convertToDto);
    }

    @Transactional
    public Optional<BinderDto> updateBinder(Long binderId, CreateBinderRequest request) {
        User currentUser = getCurrentAuthenticatedUser();
        return binderRepository.findByIdAndUser(binderId, currentUser)
                .map(binder -> {
                    binder.setName(request.getName());
                    binder.setDescription(request.getDescription());
                    Binder updatedBinder = binderRepository.save(binder);
                    return convertToDto(updatedBinder);
                });
    }

    @Transactional
    public boolean deleteBinder(Long binderId) {
        User currentUser = getCurrentAuthenticatedUser();
        return binderRepository.findByIdAndUser(binderId, currentUser)
                .map(binder -> {
                    binderRepository.delete(binder);
                    return true;
                })
                .orElse(false);
    }

    private BinderDto convertToDto(Binder binder) {
        List<CardDto> cardDtos = binder.getCards().stream()
                .map(card -> new CardDto(
                        card.getId(),
                        card.getApiCardId(),
                        card.getName(),
                        card.getImageUrl(),
                        card.getCardSet(),
                        card.getRarity(),
                        card.getPrice()
                ))
                .collect(Collectors.toList());

        Double totalPrice = cardDtos.stream()
                .mapToDouble(CardDto::getPrice)
                .sum();

        return new BinderDto(
                binder.getId(),
                binder.getName(),
                binder.getDescription(),
                binder.getUser().getId(),
                cardDtos,
                totalPrice
        );
    }
}