package pt.ualg.upbank.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ualg.upbank.domain.Account;


public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

}
