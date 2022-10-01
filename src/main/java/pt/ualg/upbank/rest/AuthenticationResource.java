package pt.ualg.upbank.rest;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pt.ualg.upbank.model.AuthenticationRequest;
import pt.ualg.upbank.model.AuthenticationResponse;
import pt.ualg.upbank.service.JwtTokenService;
import pt.ualg.upbank.service.JwtUserDetailsService;

@RestController
public class AuthenticationResource {

	private final AuthenticationManager authenticationManager;
	private final JwtUserDetailsService jwtUserDetailsService;
	private final JwtTokenService jwtTokenService;

	public AuthenticationResource(final AuthenticationManager authenticationManager,
			final JwtUserDetailsService jwtUserDetailsService,
			final JwtTokenService jwtTokenService) {
		this.authenticationManager = authenticationManager;
		this.jwtUserDetailsService = jwtUserDetailsService;
		this.jwtTokenService = jwtTokenService;
	}

	@PostMapping("/authenticate")
	public AuthenticationResponse authenticate(
			@RequestBody @Valid final AuthenticationRequest authenticationRequest) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getEmail(), authenticationRequest.getPassword()));
		} catch (final BadCredentialsException ex) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}

		final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		final AuthenticationResponse authenticationResponse = new AuthenticationResponse();
		authenticationResponse.setAccessToken(jwtTokenService.generateToken(userDetails));
		return authenticationResponse;
	}

}
