package pt.upbank.upbank.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.upbank.upbank.models.Account;

public interface AccountRepository extends JpaRepository<Account, Long>   {
    

    //If exists account with nib
    boolean existsById(long account);
    // boolean existsByNibId(long nib);

    //accounts of a user 
    Account  findByUserId(long userId);

    Account findByPhoneNumber(long phoneNumber);


    
}
