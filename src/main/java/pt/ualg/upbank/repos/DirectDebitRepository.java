package pt.ualg.upbank.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ualg.upbank.domain.DirectDebit;


public interface DirectDebitRepository extends JpaRepository<DirectDebit, Long> {
}
