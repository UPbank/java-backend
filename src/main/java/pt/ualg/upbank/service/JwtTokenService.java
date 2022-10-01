package pt.ualg.upbank.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtTokenService {

	private static final long JWT_TOKEN_VALIDITY = 20 * 60 * 1000; // 20 minutes

	private final Algorithm hmac512;
	private final JWTVerifier verifier;

	public JwtTokenService(@Value("${jwt.secret}") final String secret) {
		this.hmac512 = Algorithm.HMAC512(secret);
		this.verifier = JWT.require(this.hmac512).build();
	}

	public String generateToken(final UserDetails userDetails) {
		return JWT.create()
				.withSubject(userDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.sign(this.hmac512);
	}

	public String validateTokenAndGetUsername(final String token) {
		try {
			return verifier.verify(token).getSubject();
		} catch (final JWTVerificationException verificationEx) {
			log.warn("token invalid: {}", verificationEx.getMessage());
			return null;
		}
	}

}
