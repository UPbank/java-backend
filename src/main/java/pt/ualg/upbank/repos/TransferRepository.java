package pt.ualg.upbank.repos;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
	Page<Transfer> findBySenderOrReceiver(Account account, Account account2, Pageable pageable);

	Optional<Transfer> findBySenderAndIdOrReceiverAndId(Account account, Long id, Account account2, Long id2);

	boolean existsBySenderAndId(Account account, Long id);

	Page<Transfer> findBySenderAndReceiverOrReceiverAndSender(Account account, Account account2, Account account3,
			Account account4, Pageable pageable);

	// TODO: implement
	@Query(value = "SELECT t.*, a.* from transfer t inner join account a on t.reciever.id=a.id where a.fullName LIKE :name And t.receiver.id=a.id And t.sender=:id Or a.fullName LIKE :name And t.sender.id=a.id And t.receiver.id=:id", nativeQuery = true)
	Page<Transfer> getByNameAndByDate(@Param("id") Long id, @Param("name") String name, Pageable pageables);

	Page<Transfer> findBySenderAndReceiverContainingAndDateCreatedGreaterThanAndDateCreatedLessThanOrReceiverAndSenderContainingAndDateCreatedGreaterThanAndDateCreatedLessThan(
			Account account, String name, OffsetDateTime dateCreated, OffsetDateTime endDate, Account account2, String name2,
			OffsetDateTime dateCreated2, OffsetDateTime endDate2, Pageable pageable);

	Page<Transfer> findBySenderAndDateCreatedGreaterThanOrReceiverAndDateCreatedGreaterThan(Account account,
			OffsetDateTime dateCreated, Account account2, OffsetDateTime dateCreated2, Pageable pageable);

	Page<Transfer> findBySenderAndDateCreatedGreaterThanAndDateCreatedLessThanOrReceiverAndDateCreatedGreaterThanAndDateCreatedLessThan(
			Account account, OffsetDateTime dateCreated, OffsetDateTime endDate, Account account2,
			OffsetDateTime dateCreated2, OffsetDateTime endDate2, Pageable pageable);

	boolean existsById(long id);
}
