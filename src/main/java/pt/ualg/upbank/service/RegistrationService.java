package pt.ualg.upbank.service;

import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.Period;
import javax.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.Address;
import pt.ualg.upbank.model.CardDTO;
import pt.ualg.upbank.model.RegistrationRequest;
import pt.ualg.upbank.repos.AccountRepository;
import pt.ualg.upbank.repos.AddressRepository;


@Service
@Slf4j
public class RegistrationService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardService cardService;
    private final AddressRepository addressRepository;

    public RegistrationService(final AccountRepository accountRepository,
            final PasswordEncoder passwordEncoder, final CardService cardService, final AddressRepository addressRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.cardService = cardService;
        this.addressRepository = addressRepository;
    }

    public boolean emailExists(final RegistrationRequest registrationRequest) {
        return accountRepository.existsByEmailIgnoreCase(registrationRequest.getEmail());
    }

    //Verifies the user's age
    public  boolean hasAge (LocalDate birthdate) {
	    LocalDate currentDate = LocalDate.now();  


      Period userAge = Period.between(birthdate, currentDate) ; 
      return userAge.getYears() > 18;
  }

  public static boolean isNifValid(String nif) {
    final int max=9;
    if (!nif.matches("[\\d]+") || nif.length()!=max) {
    return false;
    }
    int checkSum=0;
    for (int i = 0; i < max-1; i++){
        checkSum += (nif.charAt(i)-'0')*(max-i);
    }
    int checkDigit = 11 - (checkSum % 11);
    if (checkDigit > 9) {
        checkDigit = 0;
    }
    return checkDigit == nif.charAt(max-1)-'0';
}



		@Transactional // Creates address and account in the same transaction
    public void register(final RegistrationRequest registrationRequest) {
        log.info("registering new user: {}", registrationRequest.getEmail());


        final Account account = new Account();
        account.setEmail(registrationRequest.getEmail());
        account.setHash(passwordEncoder.encode(registrationRequest.getPassword()));
        account.setFullName(registrationRequest.getFullName());
        account.setBirthdate(registrationRequest.getBirthdate());
        account.setTaxNumber(registrationRequest.getTaxNumber());
        account.setIdNumber(registrationRequest.getIdNumber());
        account.setBalance((long) 10000); // TODO: Move default balance to an env variable
        final Address address =AddressService.toEntity(registrationRequest.getAddress(), new Address());
        addressRepository.save(address);
				account.setAddress(address);
        
                
                accountRepository.save(account);
               
                CardDTO cardPhysical = new CardDTO();
                CardDTO cardVirtual = new CardDTO();
                cardService.createFromRegister(cardPhysical,"Physical Card",account.getId(), 0000);
                cardService.createFromRegister(cardVirtual,"Virtual Card",account.getId(),0000);
            }

}
