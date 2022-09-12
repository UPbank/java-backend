package pt.upbank.upbank.models;

import java.math.BigDecimal;

import org.yaml.snakeyaml.constructor.Construct;





public class Account {

    private Long id;

    //public NIB idNib;

    private BigDecimal balance;


    private Long userId;

    // private AccountStatus status;

    private String name;


    public Account(Long id, BigDecimal balance, AccountStatus status, String name) {
        this.id = id;
        this.balance = balance;
        this.userId = status;
        this.name = name;
    }

}
