package pt.upbank.upbank.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service ;

import pt.upbank.upbank.models.User;
import pt.upbank.upbank.repositories.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository ;
                                                      
    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername (username)
        .orElseThrow(( ) -> new UsernameNotFoundException(" User Not Found with username : " + username ) ) ;
    return user ;
   }    
}
