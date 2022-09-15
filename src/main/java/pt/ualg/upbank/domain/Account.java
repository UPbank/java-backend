package pt.ualg.upbank.domain;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Account {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String hash;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private String taxNumber;

    @Column(nullable = false)
    private String idNumber;

    @Column(nullable = false)
    private Long balance;

    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "receiver")
    private Set<DirectDebit> receiverDirectDebits;

    @OneToMany(mappedBy = "sender")
    private Set<DirectDebit> senderDirectDebits;

    @OneToMany(mappedBy = "sender")
    private Set<StandingOrder> senderStandingOrders;

    @OneToMany(mappedBy = "receiver")
    private Set<StandingOrder> receiverStandingOrders;

    @OneToMany(mappedBy = "account")
    private Set<Card> accountCards;

    @OneToMany(mappedBy = "sender")
    private Set<Transfer> senderTransfers;

    @OneToMany(mappedBy = "receiver")
    private Set<Transfer> receiverTransfers;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
