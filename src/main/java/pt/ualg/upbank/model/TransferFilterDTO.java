package pt.ualg.upbank.model;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransferFilterDTO {

    private String sender;

    private String receiver;
    
    private OffsetDateTime startDate;
    
    private OffsetDateTime endDate;
    

}
