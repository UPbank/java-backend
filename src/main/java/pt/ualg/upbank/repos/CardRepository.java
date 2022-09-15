package pt.ualg.upbank.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ualg.upbank.domain.Card;


public interface CardRepository extends JpaRepository<Card, Long> {
}
