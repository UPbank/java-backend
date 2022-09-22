package pt.ualg.upbank.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public CardDTO get(final Long id) {
        return cardRepository.findById(id)
                .map(card -> mapToDTO(card, new CardDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Long create(final CardDTO cardDTO) {
        final Card card = new Card();

        //Set expiration date 2 years from now
        cardDTO.setExpirationDate(LocalDate.now().plusYears(2));
        
        mapToEntity(cardDTO, card);
    
        return cardRepository.save(card).getId();
    }

    public void update(final Long id, final CardDTO cardDTO) {
        final Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(cardDTO, card);
        cardRepository.save(card);
    }

    public void delete(final Long id) {
        cardRepository.deleteById(id);
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
        final Account account = cardDTO.getAccount() == null ? null : accountRepository.findById(cardDTO.getAccount())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "account not found"));
        card.setAccount(account);
        return card;
    }

    /** 
     * Luhn's algorithm implementation, calculates the last digit of the card, which is the card's check digit. The check digit is obtained by calculating the sum of the non-check digits, then the sum is multiplied by 9. The last digit of this sum is the check digit. 
	 * @param card - Is a 15 digit number consisting of the card prefix, zero(s) and the id of the bank account
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
		for (int i = digits.length - 1; i >= 0; i -= 2)	{
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
    
    /** NOT NEEDED IN THE CODE - FOR TESTING ONLY
     * Luhn's algorithm card validation. Checks if the calculated check number is the same as the last digit of a 16-digit card.
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
     * Card number generator, which receives an Account id. The card is made of fixed prefix numbers, zero(s) 
     * and the account id number. Returns the card number with added check digit. 
     * @param id - bank account id
     * @return - 16-digit card number
     */
    public static String generateCardNumber(Long id){
        final String cardPrefix = "436339";
        int idLen = id.toString().length();
        String cardNum = cardPrefix + "0".repeat(15-cardPrefix.length()-idLen) + id;
        return cardNum + calculateCheckDigit(cardNum);
    }

}
