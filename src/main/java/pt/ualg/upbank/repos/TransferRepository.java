package pt.ualg.upbank.repos;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.Transfer;


public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Optional <List<Transfer>> findBySender(Account sender);
    Optional <List<Transfer>>  findByDateCreated(OffsetDateTime dateCreated);
    List<Transfer> findByDateCreatedGreaterThan(OffsetDateTime start);

    

    boolean existsById(long id);
}
