package pt.ualg.upbank.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.ualg.upbank.domain.TelcoProvider;

public interface TelcoProviderRepository extends JpaRepository<TelcoProvider, Long> {

	Optional<TelcoProvider> findById(Long id);

	boolean existsById(Long Id);

	List<TelcoProvider> findByNameLike(String name);

	boolean existsByName(String name);

}
