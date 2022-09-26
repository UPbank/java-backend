package pt.ualg.upbank.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.Card;


public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByIdAndAccount(Account account , Long Id);
}
