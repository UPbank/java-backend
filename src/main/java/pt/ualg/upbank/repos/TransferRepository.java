package pt.ualg.upbank.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ualg.upbank.domain.Transfer;


public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
