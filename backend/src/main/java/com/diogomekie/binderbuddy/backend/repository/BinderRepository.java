package com.diogomekie.binderbuddy.backend.repository;

import com.diogomekie.binderbuddy.backend.model.Binder;
import com.diogomekie.binderbuddy.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface BinderRepository extends JpaRepository<Binder, Long> {

    List<Binder> findByUser(User user);

    Optional<Binder> findByIdAndUser(Long id, User user);
}