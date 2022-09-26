package pt.ualg.upbank.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.DirectDebit;


public interface DirectDebitRepository extends JpaRepository<DirectDebit, Long> {
    Optional<DirectDebit> findBySenderOrReceiverAndId(Account accountSender ,Account accountReciever, Long Id);

}
