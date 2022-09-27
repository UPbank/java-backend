package pt.ualg.upbank.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ualg.upbank.domain.Address;


public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByIdentifier(String identifier);

    boolean existsByIdentifier(String identifier);
}
