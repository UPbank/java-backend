package pt.ualg.upbank.service;

import java.util.Collections;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.repos.AccountRepository;


@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    public static final String USER = "USER";
    public static final String ROLE_USER = "ROLE_" + USER;

    private final AccountRepository accountRepository;

    public JwtUserDetailsService(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final Optional<Account> account = accountRepository.findByEmailIgnoreCase(username);
        if (!account.isPresent()) {
            log.warn("user not found: {}", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return new User(username, account.get().getHash(), Collections.singletonList(new SimpleGrantedAuthority(ROLE_USER)));
    }

}
