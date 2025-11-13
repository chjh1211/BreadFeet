package com.example.breadfeet_BE.domain.challenge;

import com.example.breadfeet_BE.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    Optional<UserChallenge> findByUserAndChallenge(User user, Challenge challenge);
    List<UserChallenge> findAllByUser(User user);
}
