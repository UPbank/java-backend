package pt.ualg.upbank.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.Transfer;


public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsById(Long id);
    boolean existsByIdentifier(String identifier);
    Optional<Account> findByIdentifier(String identifier);

    Optional<Account> findById(Long id);

    List<Account> findByFullNameContaining(String fullName);

    @Modifying
    @Query("UPDATE Account t set t.balance = t.balance + :amount WHERE t.id = :id")
    void addToAccountBalance(@Param("id") Long id, @Param("amount") Long amount);

    @Modifying
    @Query("UPDATE Account t set t.identifier = :identifier  WHERE t.id = :id")
    void changeIdentifier(@Param("id") Long id, @Param("identifier") String identifier);


    

}
