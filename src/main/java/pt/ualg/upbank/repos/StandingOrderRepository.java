package pt.ualg.upbank.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.StandingOrder;
import pt.ualg.upbank.model.Frequency;


public interface StandingOrderRepository extends JpaRepository<StandingOrder, Long> {

    List<StandingOrder>  findByFrequency(Frequency frequency);

    Page<StandingOrder> findBySender(Account account, Pageable pageable);
}
