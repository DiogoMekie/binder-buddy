package com.diogomekie.binderbuddy.backend.repository;

import com.diogomekie.binderbuddy.backend.model.Binder;
import com.diogomekie.binderbuddy.backend.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByBinder (Binder binder);

    Optional<Card> findByApiCardId(String apiCardId);

    Optional<Card> findByApiCardIdAndBinder(String apiCardId, Binder binder);
}
