package pt.ualg.upbank.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.ualg.upbank.domain.Address;
import pt.ualg.upbank.model.AddressDTO;
import pt.ualg.upbank.repos.AddressRepository;

@Service
public class AddressService {

	private final AddressRepository addressRepository;

	public AddressService(final AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}

	public List<AddressDTO> findAll() {
		return addressRepository.findAll(Sort.by("id"))
				.stream()
				.map(address -> mapToDTO(address, new AddressDTO()))
				.collect(Collectors.toList());
	}

	public AddressDTO get(final Long id) {
		return addressRepository.findById(id)
				.map(address -> mapToDTO(address, new AddressDTO()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	public Long create(final AddressDTO addressDTO) {
		final Address address = new Address();
		mapToEntity(addressDTO, address);
		return addressRepository.save(address).getId();
	}

	public void update(final Long id, final AddressDTO addressDTO) {
		final Address address = addressRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		mapToEntity(addressDTO, address);
		addressRepository.save(address);
	}

	public void delete(final Long id) {
		addressRepository.deleteById(id);
	}

	public static AddressDTO toDTO(final Address address, final AddressDTO addressDTO) {
		addressDTO.setId(address.getId());
		addressDTO.setLine1(address.getLine1());
		addressDTO.setLine2(address.getLine2());
		addressDTO.setZipCode(address.getZipCode());
		addressDTO.setCity(address.getCity());
		addressDTO.setDistrict(address.getDistrict());
		return addressDTO;
	}

	private AddressDTO mapToDTO(final Address address, final AddressDTO addressDTO) {
		return toDTO(address, addressDTO);
	}

	public static Address toEntity(final AddressDTO addressDTO, final Address address) {
		address.setLine1(addressDTO.getLine1());
		address.setLine2(addressDTO.getLine2());
		address.setZipCode(addressDTO.getZipCode());
		address.setCity(addressDTO.getCity());
		address.setDistrict(addressDTO.getDistrict());
		address.setIdentifier(addressDTO.getIdentifier());
		return address;
	}

	public Address mapToEntity(final AddressDTO addressDTO, final Address address) {
		return toEntity(addressDTO, address);
	}

}
