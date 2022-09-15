package pt.ualg.upbank.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ualg.upbank.domain.TelcoProvider;


public interface TelcoProviderRepository extends JpaRepository<TelcoProvider, Long> {
}
