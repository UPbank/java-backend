package pt.ualg.upbank.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.ualg.upbank.domain.TelcoProvider;
import pt.ualg.upbank.model.TelcoProviderDTO;
import pt.ualg.upbank.repos.TelcoProviderRepository;

@Service
public class TelcoProviderService {

	private final TelcoProviderRepository telcoProviderRepository;

	public TelcoProviderService(final TelcoProviderRepository telcoProviderRepository) {
		this.telcoProviderRepository = telcoProviderRepository;
	}

	public List<TelcoProviderDTO> findAll() {
		return telcoProviderRepository.findAll(Sort.by("id"))
				.stream()
				.map(telcoProvider -> mapToDTO(telcoProvider, new TelcoProviderDTO()))
				.collect(Collectors.toList());
	}

	public TelcoProviderDTO get(final Long id) {
		return telcoProviderRepository.findById(id)
				.map(telcoProvider -> mapToDTO(telcoProvider, new TelcoProviderDTO()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	public Long create(final TelcoProviderDTO telcoProviderDTO) {
		final TelcoProvider telcoProvider = new TelcoProvider();
		mapToEntity(telcoProviderDTO, telcoProvider);
		telcoProvider.setDateCreated(OffsetDateTime.now());
		telcoProvider.setLastUpdated(OffsetDateTime.now());
		return telcoProviderRepository.save(telcoProvider).getId();
	}

	public void update(final Long id, final TelcoProviderDTO telcoProviderDTO) {
		final TelcoProvider telcoProvider = telcoProviderRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		mapToEntity(telcoProviderDTO, telcoProvider);
		telcoProviderRepository.save(telcoProvider);
	}

	public void delete(final Long id) {
		telcoProviderRepository.deleteById(id);
	}

	private TelcoProviderDTO mapToDTO(final TelcoProvider telcoProvider,
			final TelcoProviderDTO telcoProviderDTO) {
		telcoProviderDTO.setId(telcoProvider.getId());
		telcoProviderDTO.setName(telcoProvider.getName());
		return telcoProviderDTO;
	}

	private TelcoProvider mapToEntity(final TelcoProviderDTO telcoProviderDTO,
			final TelcoProvider telcoProvider) {
		telcoProvider.setName(telcoProviderDTO.getName());
		return telcoProvider;
	}
}
