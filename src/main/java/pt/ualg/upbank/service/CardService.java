package pt.ualg.upbank.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.Card;
import pt.ualg.upbank.model.CardDTO;
import pt.ualg.upbank.repos.AccountRepository;
import pt.ualg.upbank.repos.CardRepository;

@Service
public class CardService {

	private final CardRepository cardRepository;
	private final AccountRepository accountRepository;

	public CardService(final CardRepository cardRepository,
			final AccountRepository accountRepository) {
		this.cardRepository = cardRepository;
		this.accountRepository = accountRepository;
	}

	public List<CardDTO> findAll() {
		return cardRepository.findAll(Sort.by("id"))
				.stream()
				.map(card -> mapToDTO(card, new CardDTO()))
				.collect(Collectors.toList());
	}

	public List<CardDTO> get(final Long accountId) {
		Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "cards.account.notFound"));
		return cardRepository.findByAccount(account)
				.stream()
				.map(card -> mapToDTO(card, new CardDTO()))
				.collect(Collectors.toList());
	}

	public Long createFromRegister(final CardDTO cardDTO, String name, Long account, int pinCode) {
		final Card card = new Card();

		cardDTO.setAccount(account);
		cardDTO.setName(name);
		cardDTO.setPinCode(pinCode);

		// Set expiration date 2 years from now
		cardDTO.setExpirationDate(LocalDate.now().plusYears(2));

		mapToEntity(cardDTO, card);

		return cardRepository.save(card).getId();
	}

	public Long create(final CardDTO cardDTO) {
		final Card card = new Card();
		// Set expiration date 2 years from now
		cardDTO.setExpirationDate(LocalDate.now().plusYears(2));
		mapToEntity(cardDTO, card);

		return cardRepository.save(card).getId();
	}

	/**
	 * Transactional method updates the information for the {@link Card}, checks if
	 * a the {@link Account} exists, and the card exists in the {@link Account} and
	 * the pin as 4 digits. Updates Date of last updated
	 * 
	 * @param id             - of the {@link Card} to update
	 * @param nfcPayments
	 * @param onlinePayments
	 * @param pinCode
	 * @param accountId      - {@link Account} to confirm the id of the user
	 */
	@Transactional
	public void update(final Long id, final Boolean nfcPayments, final Boolean onlinePayments, final Integer pinCode,
			final Long accountId) {
		CardDTO cardDTO = new CardDTO();
		final Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "card.account.notFound"));
		final Card card = cardRepository.findByIdAndAccount(id, account)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "card.card.notFound"));

		if (nfcPayments == null) {
			cardDTO.setNfcPayments(card.getNfcPayments());
		} else {
			cardDTO.setNfcPayments(nfcPayments);
		}
		if (onlinePayments == null) {
			cardDTO.setOnlinePayments(card.getOnlinePayments());
		} else {
			cardDTO.setOnlinePayments(onlinePayments);
		}
		if (pinCode == null) {
			cardDTO.setPinCode(card.getPinCode());
		} else {
			if (pinCode.toString().length() != 4) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "card.update.invalidPin");
			}
			cardDTO.setPinCode(pinCode);
		}
		cardDTO.setExpirationDate(card.getExpirationDate());
		cardDTO.setName(card.getName());
		cardDTO.setAccount(card.getAccount().getId());
		mapToEntity(cardDTO, card);
		card.setLastUpdated(OffsetDateTime.now());
		cardRepository.save(card);
	}

	@Transactional
	public void delete(final Long id, final Long accountId) {
		final Account account = accountRepository.findById(accountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card.account.notFound"));
		final Card card = cardRepository.findByIdAndAccount(id, account)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Card.card.notFound"));

		cardRepository.deleteById(card.getId());
	}

	private CardDTO mapToDTO(final Card card, final CardDTO cardDTO) {
		cardDTO.setId(card.getId());
		cardDTO.setName(card.getName());
		cardDTO.setExpirationDate(card.getExpirationDate());
		cardDTO.setPinCode(card.getPinCode());
		cardDTO.setNfcPayments(card.getNfcPayments());
		cardDTO.setOnlinePayments(card.getOnlinePayments());
		cardDTO.setAccount(card.getAccount() == null ? null : card.getAccount().getId());
		return cardDTO;
	}

	private Card mapToEntity(final CardDTO cardDTO, final Card card) {
		card.setName(cardDTO.getName());
		card.setExpirationDate(cardDTO.getExpirationDate());
		card.setPinCode(cardDTO.getPinCode());
		card.setNfcPayments(cardDTO.getNfcPayments());
		card.setOnlinePayments(cardDTO.getOnlinePayments());
		final Account account = cardDTO.getAccount() == null ? null
				: accountRepository.findById(cardDTO.getAccount())
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "account not found"));
		card.setAccount(account);
		return card;
	}

	/**
	 * Luhn's algorithm implementation, calculates the last digit of the card, which
	 * is the card's check digit. The check digit is obtained by calculating the sum
	 * of the non-check digits, then the sum is multiplied by 9. The last digit of
	 * this sum is the check digit.
	 * 
	 * @param card - Is a 15 digit number consisting of the card prefix, zero(s) and
	 *             the id of the bank account
	 * @return - the check digit, which will make part of the final card number.
	 */
	public static String calculateCheckDigit(String card) {
		if (card == null)
			return null;
		String digit;

		int[] digits = new int[card.length()];
		for (int i = 0; i < card.length(); i++) {
			digits[i] = Character.getNumericValue(card.charAt(i));
		}
		for (int i = digits.length - 1; i >= 0; i -= 2) {
			digits[i] += digits[i];
			if (digits[i] >= 10) {
				digits[i] = digits[i] - 9;
			}
		}
		int sum = 0;
		for (int i = 0; i < digits.length; i++) {
			sum += digits[i];
		}
		sum = sum * 9;
		digit = sum + "";

		return digit.substring(digit.length() - 1);
	}

	/**
	 * NOT NEEDED IN THE CODE - FOR TESTING ONLY
	 * Luhn's algorithm card validation. Checks if the calculated check number is
	 * the same as the last digit of a 16-digit card.
	 * 
	 * @param card - 16-digit card number
	 * @return - true or false
	 */
	public static boolean luhnCheck(String card) {
		if (card == null)
			return false;
		char checkDigit = card.charAt(card.length() - 1);
		String digit = calculateCheckDigit(card.substring(0, card.length() - 1));
		return checkDigit == digit.charAt(0);
	}

	/**
	 * Card number generator, which receives an Account id. The card is made of
	 * fixed prefix numbers, zero(s)
	 * and the account id number. Returns the card number with added check digit.
	 * 
	 * @param id - bank account id
	 * @return - 16-digit card number
	 */
	public static String generateCardNumber(Long id) {
		final String cardPrefix = "436339";
		int idLen = id.toString().length();
		String cardNum = cardPrefix + "0".repeat(15 - cardPrefix.length() - idLen) + id;
		return cardNum + calculateCheckDigit(cardNum);
	}

}
