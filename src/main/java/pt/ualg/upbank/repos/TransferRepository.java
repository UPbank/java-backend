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
    Optional <List<Transfer>> findBySenderAndDateCreatedBetween(Account sender, OffsetDateTime start, OffsetDateTime end);
    Optional <List<Transfer>> 
    findByReceiverAndDateCreatedBetween(Account receiver, OffsetDateTime start, OffsetDateTime end);

    // Optional<List<Transfer>> findBySenderAndByDateCreatedGreaterThanEqualAndLessThanEqual(Account sender, OffsetDateTime start, OffsetDateTime end);

    // Optional<List<Transfer>> findBySenderOrRecieverAndByDateCreatedGreaterThanEqualAndLessThanEqual(Account sender,Account reciever, OffsetDateTime start, OffsetDateTime end);

    // public class TransferService {
    //     public List<Transfer> findByConditions(
    //         OffsetDateTime start,
    //         OffsetDateTime end,
    //         Long sender_id,
    //         Long receiver_id,
    //         String type,
    //     ) {
    //         if (start && end && start > end) {
    //             OffsetDateTime temp_end = start;
    //             start = end;
    //             end = temp_end;
    //         } 
    
    //         return tarnsfersList
    //             .stream()
    //             .filter(t -> start == null || t.getTransferDate() >= start)
    //             .filter(t -> end == null || t.getTransferDate() <= end)
    //             .filter(t -> sender_id == null || t.getSender().getId() == sender_id)
    //             .filter(t -> receiver_id == null || t.getReceiver().getId() == receiver_id)
    //             .filter(t -> type == null || t.getType() == type)
    //             .toList()
    //     }
    // }
    boolean existsById(long id);
}
