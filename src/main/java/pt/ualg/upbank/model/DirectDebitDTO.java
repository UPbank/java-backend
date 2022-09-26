package pt.ualg.upbank.model;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.ReadOnlyProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DirectDebitDTO {
    
    @ReadOnlyProperty
    private Long id;

    @NotNull
    private Boolean active;

    private LocalDate lastDebit;

    @NotNull
    private Long receiver;

    @NotNull
    private Long sender;

}
