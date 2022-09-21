package pt.ualg.upbank.service;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.Period;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.Address;
import pt.ualg.upbank.model.RegistrationRequest;
import pt.ualg.upbank.repos.AccountRepository;


@Service
@Slf4j
public class RegistrationService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(final AccountRepository accountRepository,
            final PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(final RegistrationRequest registrationRequest) {
        return accountRepository.existsByEmailIgnoreCase(registrationRequest.getEmail());
    }

    //Verifies the user's age
    public static boolean hasAge (LocalDate birthdate) {
	    LocalDate currentDate = LocalDate.now();  
      Period userAge = Period.between(birthdate, currentDate) ; 
      return userAge.getYears() > 18;
  }


		@Transactional // Creates address and account in the same transaction
    public void register(final RegistrationRequest registrationRequest) {
        log.info("registering new user: {}", registrationRequest.getEmail());

        if (!hasAge(registrationRequest.getBirthdate())){
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "registration.age.underage");
        }

        final Account account = new Account();
        account.setEmail(registrationRequest.getEmail());
        account.setHash(passwordEncoder.encode(registrationRequest.getPassword()));
        account.setFullName(registrationRequest.getFullName());
        account.setBirthdate(registrationRequest.getBirthdate());
        account.setTaxNumber(registrationRequest.getTaxNumber());
        account.setIdNumber(registrationRequest.getIdNumber());
        account.setBalance((long) 10000); // TODO: Move default balance to an env variable
				account.setAddress(AddressService.toEntity(registrationRequest.getAddress(), new Address()));
        accountRepository.save(account);
    }

}
