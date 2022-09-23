package pt.ualg.upbank.rest;

import static pt.ualg.upbank.service.JwtUserDetailsService.ROLE_USER;

import java.util.Optional;
import java.util.regex.Matcher;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pt.ualg.upbank.model.AccountDTO;
import pt.ualg.upbank.model.AddressDTO;
import pt.ualg.upbank.service.AccountService;
import pt.ualg.upbank.service.RegistrationService;


@RestController
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('" + ROLE_USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class AccountResource {

		private final AccountService accountService;
		private final RegistrationService registrationService;
		

		public AccountResource(final AccountService accountService, final RegistrationService registrationService) {
				this.accountService = accountService;
				this.registrationService = registrationService;
				
		}

		public AccountDTO getRequestUser() {
				UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String email = userDetails.getUsername();
				return accountService.getByEmail(email);
		}

		@GetMapping("/")
		public ResponseEntity<AccountDTO> getAccount() {
				return ResponseEntity.ok(getRequestUser());
		}

		@PutMapping("/")
		public ResponseEntity<Void> updateAccount(@RequestBody @Valid final Optional<String> email, @RequestBody @Valid final Optional<AddressDTO> addressDTO) {
			final String newEmail = email==null ? null : email.get();
			if(email != null){
				
			}
			final Matcher mat = RegistrationResource.pattern.matcher(newEmail);
        if (!mat.matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "registration.email.invalid");
        }
		if (accountService.emailExists(newEmail)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "registration.register.taken");
        }

		final AddressDTO newAddressDTO = addressDTO==null ? null: addressDTO.get();
		
				accountService.update(getRequestUser().getId(), newEmail, newAddressDTO); // TODO: user can only update some fields

				// The system shall use the Account and Address tables. The system shall be able to accept changes in the email, phone number and address details. Address details shall be in conformity with the fields requested (Address line 1, Address line 2 (optional), Postal code (####-###), City, District). The system shall not be accepting changes in attributes such as name and tax number for security reasons. When changes in the account table are performed, the attribute updatedAt shall be automatically changed to the date of the performed changes.
				return ResponseEntity.ok().build();
		}

		@DeleteMapping("/")
		@ApiResponse(responseCode = "204")
		public ResponseEntity<Void> deleteAccount() {
			if(getRequestUser().getBalance()>0){
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "balance.notEmpty"); 
			}
			accountService.delete(getRequestUser().getId()); 
			
			return ResponseEntity.noContent().build();
		}

}
