package pt.upbank.upbank.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Card implements Serializable {
private @Id @GeneratedValue Long id;
private String name;
private Account account;
private Date expirationDate;
}
