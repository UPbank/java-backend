package pt.ualg.upbank.model;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DirectDebitDTO {

    private Long id;

    @NotNull
    private Boolean active;

    private LocalDate lastDebit;

    @NotNull
    private Long receiver;

    @NotNull
    private Long sender;

}
