package pt.ualg.upbank.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.ualg.upbank.IBAN.IBANGenerator;
import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.Address;
import pt.ualg.upbank.model.AccountDTO;
import pt.ualg.upbank.model.AddressDTO;
import pt.ualg.upbank.repos.AccountRepository;
import pt.ualg.upbank.repos.AddressRepository;

@Service
public class AccountService {
	private final AccountRepository accountRepository;
	private final AddressRepository addressRepository;
	private final PasswordEncoder passwordEncoder;

	public AccountService(final AccountRepository accountRepository,
			final AddressRepository addressRepository,
			final PasswordEncoder passwordEncoder,
			final CardService cardService) {
		this.accountRepository = accountRepository;
		this.addressRepository = addressRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<AccountDTO> findAll() {
		return accountRepository.findAll(Sort.by("id"))
				.stream()
				.map(account -> mapToDTO(account, new AccountDTO()))
				.collect(Collectors.toList());
	}

	public AccountDTO get(final Long id) {
		return accountRepository.findById(id)
				.map(account -> mapToDTO(account, new AccountDTO()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	public AccountDTO getByEmail(final String email) {
		return accountRepository.findByEmailIgnoreCase(email)
				.map(account -> mapToDTO(account, new AccountDTO()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Transactional
	public Long create(final AccountDTO accountDTO) {
		final Account account = new Account();
		mapToEntity(accountDTO, account);
		return accountRepository.save(account).getId();
	}

	/**
	 * Method to use the information from the User to update the {@link Account} of
	 * the User.
	 * 
	 * @param id
	 * @param email
	 * @param addressDTO
	 */
	@Transactional
	public void update(Long id, String email, AddressDTO addressDTO) {
		final Account account = accountRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		final AccountDTO accountDTO = new AccountDTO();
		if (email != null) {
			accountDTO.setEmail(email);
		} else {
			accountDTO.setEmail(account.getEmail());
		}

		if (addressDTO != null) {
			final Address address = AddressService.toEntity(addressDTO, new Address());
			addressRepository.save(address);
			accountDTO.setAddress(addressDTO);
		} else {
			accountDTO.setAddress(AddressService.toDTO(account.getAddress(), new AddressDTO()));
		}

		mapToEntity(accountDTO, account);
		accountRepository.save(account);
	}

	public void delete(final Long id) {
		accountRepository.deleteById(id);
	}

	AccountDTO mapToDTO(final Account account, final AccountDTO accountDTO) {
		accountDTO.setId(account.getId());
		accountDTO.setEmail(account.getEmail());
		accountDTO.setFullName(account.getFullName());
		accountDTO.setBirthdate(account.getBirthdate());
		accountDTO.setTaxNumber(account.getTaxNumber());
		accountDTO.setIdNumber(account.getIdNumber());
		accountDTO.setBalance(account.getBalance());
		accountDTO.setIdentifier(account.getIdentifier());
		accountDTO.setIban(IBANGenerator.generateIBAN(account.getId()));
		accountDTO
				.setAddress(account.getAddress() == null ? null : AddressService.toDTO(account.getAddress(), new AddressDTO()));
		return accountDTO;
	}

	public Account mapToEntity(final AccountDTO accountDTO, final Account account) {
		account.setEmail(accountDTO.getEmail());
		account.setHash(passwordEncoder.encode(accountDTO.getHash()));
		account.setFullName(accountDTO.getFullName());
		account.setBirthdate(accountDTO.getBirthdate());
		account.setTaxNumber(accountDTO.getTaxNumber());
		account.setIdNumber(accountDTO.getIdNumber());
		account.setBalance(accountDTO.getBalance());
		final Address address = accountDTO.getAddress() == null ? null
				: addressRepository.findById(accountDTO.getAddress().getId())
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "address not found"));
		account.setAddress(address);
		return account;
	}

	public boolean emailExists(final String email) {
		return accountRepository.existsByEmailIgnoreCase(email);
	}

	/**
	 * Checks if balance of the {@link Account} is positive
	 * 
	 * @param accountDTO
	 * @return
	 */
	public boolean checkBalance(final AccountDTO accountDTO) {
		return accountDTO.getBalance() > 0;

	}

	public void removeMoney(Account account, Long amount) {
		accountRepository.addToAccountBalance(account.getId(), -amount);
	}

	public void addMoney(final Account account, Long amount) {
		accountRepository.addToAccountBalance(account.getId(), amount);
	}
}
