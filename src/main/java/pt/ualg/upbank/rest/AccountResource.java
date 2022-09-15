package pt.ualg.upbank.rest;

import static pt.ualg.upbank.service.JwtUserDetailsService.ROLE_USER;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import javax.validation.Valid;
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

import pt.ualg.upbank.model.AccountDTO;
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

		private AccountDTO getRequestUser() {
				UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String email = userDetails.getUsername();
				return accountService.getByEmail(email);
		}

		@GetMapping("/")
		public ResponseEntity<AccountDTO> getAccount() {
				return ResponseEntity.ok(getRequestUser());
		}

		@PutMapping("/")
		public ResponseEntity<Void> updateAccount(@RequestBody @Valid final AccountDTO accountDTO) {
				accountService.update(getRequestUser().getId(), accountDTO); // TODO: user can only update some fields
				return ResponseEntity.ok().build();
		}

		@DeleteMapping("/")
		@ApiResponse(responseCode = "204")
		public ResponseEntity<Void> deleteAccount() {
				accountService.delete(getRequestUser().getId()); // TODO: Deletion logic
				return ResponseEntity.noContent().build();
		}

}
