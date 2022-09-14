package pt.upbank.upbank.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//@EnableWebSecurity - para web security personalizada
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
			.antMatchers("/homePage").access("hasRole('ROLE_USER')")
			.and()
				.formLogin().loginPage("/loginPage")
				.defaultSuccessUrl("/homePage")
				.failureUrl("/loginPage?error")
				.usernameParameter("username").passwordParameter("password")				
			.and()
				.logout().logoutSuccessUrl("/loginPage?logout"); 
		
	}

    // in Memory Authentication

	// @Override
	// public void configure(AuthenticationManagerBuilder authenticationMgr) throws Exception {
	// 	authenticationMgr.inMemoryAuthentication()
	// 		.withUser("duarte")
	// 		.password(passwordEncoder().encode("jd@123"))
	// 		.password("jd@123")
	// 		.authorities("ROLE_USER");
	// }

	@Override
	protected void configure(AuthenticationManagerBuilder auth ) throws Exception {
    auth.userDetailsService (userDetailsService)
            .passwordEncoder(passwordEncoder( ));
}
	
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	
}
