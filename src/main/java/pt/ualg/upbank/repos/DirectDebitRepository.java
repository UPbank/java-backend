package pt.ualg.upbank.repos;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.DirectDebit;

public interface DirectDebitRepository extends JpaRepository<DirectDebit, Long> {
	Optional<DirectDebit> findBySenderAndId(Account account, Long id);

	boolean existsBySenderAndId(Account account, Long id);

	Optional<DirectDebit> findBySenderOrReceiverAndId(Account accountSender, Account accountReciever, Long Id);

	Page<DirectDebit> findBySender(Account accountSender, Pageable pageable);

}
