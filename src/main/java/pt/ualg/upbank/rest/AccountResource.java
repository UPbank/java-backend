package pt.ualg.upbank.rest;

import static pt.ualg.upbank.service.JwtUserDetailsService.ROLE_USER;
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
import pt.ualg.upbank.model.UpdateAccountDTO;
import pt.ualg.upbank.service.AccountService;


@RestController
@RequestMapping(value = "/api/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('" + ROLE_USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class AccountResource {

		private final AccountService accountService;

		public AccountResource(final AccountService accountService) {
				this.accountService = accountService;				
		}

		/**
		 * Gets the User details
		 * @return 
		 */
		public AccountDTO getRequestUser() {
				UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String email = userDetails.getUsername();
				return accountService.getByEmail(email);
		}

		/**
		 * Gets {@link Account} of the User Logged in
		 * @return {@link Account}
		 */
		@GetMapping("/")
		public ResponseEntity<AccountDTO> getAccount() {
				return ResponseEntity.ok(getRequestUser());
		}


		/**
		 * Updates the User´s {@link Account} 
		 * Validates email, and {@link Address}
		 * @param updateaccountDTO
		 * @return
		 */
		@PutMapping("/")
		public ResponseEntity<Void> updateAccount(@RequestBody @Valid final UpdateAccountDTO updateaccountDTO) {
			final String newEmail = updateaccountDTO.getEmail();
			if(newEmail != null) {
				final Matcher mat = RegistrationResource.pattern.matcher(newEmail);
	
				if (!mat.matches()) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "registration.email.invalid");
				}
				if (accountService.emailExists(newEmail)) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "registration.register.taken");
				}
			}

			final AddressDTO newAddressDTO = updateaccountDTO.getAddress();
			if( newAddressDTO!=null){
				final Matcher matZipCode = RegistrationResource.patternZipCode.matcher(newAddressDTO.getZipCode());
				if (!matZipCode.matches()) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "registration.zip-code.invalid");
				}
			}
			accountService.update(getRequestUser().getId(), newEmail, newAddressDTO); 
			return ResponseEntity.ok().build();
		}

		/**
		 * Deletes the User´s {@link Account}  
		 * Validates if balance of the {@link Account} is empty;
		 * @return
		 */
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
