package pt.ualg.upbank.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ualg.upbank.domain.StandingOrder;
import pt.ualg.upbank.model.Frequency;


public interface StandingOrderRepository extends JpaRepository<StandingOrder, Long> {

    List<StandingOrder> findByFrequency(Frequency frequency);
}
