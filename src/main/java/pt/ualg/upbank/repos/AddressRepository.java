package pt.ualg.upbank.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ualg.upbank.domain.Address;


public interface AddressRepository extends JpaRepository<Address, Long> {
}
