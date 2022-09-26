package pt.ualg.upbank.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ualg.upbank.domain.Account;


public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmailIgnoreCase(String email);



    boolean existsByEmailIgnoreCase(String email);
    boolean existsById(Long id);
    Optional<Account> findById(Long id);

    Optional<List<Account>> findByFullNameContaining(String fullName);

    @Modifying
    @Query("UPDATE Account t set t.balance = t.balance + :amount WHERE t.id = :id")
    void addToAccountBalance(@Param("id") Long id, @Param("amount") Long amount);

    

}
