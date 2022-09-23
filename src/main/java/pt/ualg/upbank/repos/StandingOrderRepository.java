package pt.ualg.upbank.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ualg.upbank.domain.StandingOrder;
import pt.ualg.upbank.model.Frequency;


public interface StandingOrderRepository extends JpaRepository<StandingOrder, Long> {

    Optional<List<StandingOrder>> findByFrequency(Frequency frequency);
}
