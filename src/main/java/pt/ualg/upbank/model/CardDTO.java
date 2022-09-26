package pt.ualg.upbank.model;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.ReadOnlyProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CardDTO {

    @ReadOnlyProperty
    private Long id;

    @Size(max = 255)
    private String name;

    @NotNull
    private LocalDate expirationDate;

    @NotNull
    @Size(max = 4)
    private Integer pinCode;

    private Boolean nfcPayments;

    private Boolean onlinePayments;

    @NotNull
    private Long account;

}
