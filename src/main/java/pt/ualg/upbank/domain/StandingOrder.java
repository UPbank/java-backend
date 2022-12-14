package pt.ualg.upbank.domain;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pt.ualg.upbank.model.Frequency;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class StandingOrder {

	@Id
	@Column(nullable = false, updatable = false)
	@SequenceGenerator(name = "primary_sequence", sequenceName = "primary_sequence", allocationSize = 1, initialValue = 10000)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "primary_sequence")
	private Long id;

	@Column(nullable = false)
	private Long amount;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Frequency frequency;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sender_id", nullable = false)
	private Account sender;

	@Column(nullable = false)
	private String iban;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private OffsetDateTime dateCreated;

	@LastModifiedDate
	@Column(nullable = false)
	private OffsetDateTime lastUpdated;

}
