package pt.ualg.upbank.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ualg.upbank.domain.StandingOrder;


public interface StandingOrderRepository extends JpaRepository<StandingOrder, Long> {
}
