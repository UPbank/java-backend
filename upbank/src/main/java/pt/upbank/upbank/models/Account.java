package pt.upbank.upbank.models;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;


@Data
@Entity
public class Account implements Serializable  {

    private@Id @GeneratedValue  Long id;

    public Long idNib;

    private BigDecimal balance;

    private Long userId;

    private String status;

    private Long nif;

    private String name;

    private String email;

    private String password;

    //Formato standard 
    // primeira linha obrigatoria
    // segunda linha opcional
    // codigo postal
    private String address1;

    private String address2;

    private String postalCode;

    private String country;

    private String city;

    private Integer phone;



}
