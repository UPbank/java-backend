package pt.ualg.upbank.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ualg.upbank.domain.Account;


public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    @Query("UPDATE Account t set t.balance = t.balance + :amount WHERE t.id = :id")
    boolean addToAccountBalance(@Param("id") Long id, @Param("amount") Long amount);

    

}
