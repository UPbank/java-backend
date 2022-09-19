package pt.ualg.upbank.rest;

import static pt.ualg.upbank.service.JwtUserDetailsService.ROLE_USER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ualg.upbank.model.SimplePage;
import pt.ualg.upbank.model.TransferDTO;
import pt.ualg.upbank.service.TransferService;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/transfers", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('" + ROLE_USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class TransferResource {

    private final TransferService transferService;

    public TransferResource(final TransferService transferService) {
        this.transferService = transferService;
    }

    @Operation(
            parameters = {
                    @Parameter(
                            name = "page",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "size",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = Integer.class)
                    ),
                    @Parameter(
                            name = "sort",
                            in = ParameterIn.QUERY,
                            schema = @Schema(implementation = String.class)
                    )
            }
    )
    @GetMapping
    public ResponseEntity<SimplePage<TransferDTO>> getAllTransfers(
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(transferService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferDTO> getTransfer(@PathVariable final Long id) {
        return ResponseEntity.ok(transferService.get(id));
    }

    //eliminar

    @PostMapping("payments/reference")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTransfer(@RequestBody Long entity, @RequestBody Long reference, @RequestBody Long amount) {
        //entity with 5 digits
        //reference with 9 digits
        //amount provided by the user
        return new ResponseEntity<>(transferService.create(transferDTO), HttpStatus.CREATED);
    }

    @PostMapping("payments/governament")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTransfer(@RequestBody Long reference,@RequestBody Long amount) {
        //reference with 15 digits
        //amount provided by the user
        return new ResponseEntity<>(transferService.create(transferDTO), HttpStatus.CREATED);
    }

    @PostMapping("payments/telco")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTransfer(@RequestBody String name, @RequestBody Short number) {
        //provider 
        //Lycamobile GT MOBile, MEO, MEO Card, MEO Card - PT HEllo/ PT Card, MEO CArd - Telefone Hello, MEO Escola Digital, Moche, NOS, NOS - Escola Digital, Sapo, Sapo ADSL, UZO, Via Card, Vodafone, WTF.
        //phone number provided by the user
        //phone number with 9 digits
        // phone number start with 91,92, 93 or 96
        //amount provided by the user
        return new ResponseEntity<>(transferService.create(transferDTO), HttpStatus.CREATED);
    }

    @PostMapping("payments/bankTransfers")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTransfer(@RequestBody String iban, @RequestBody Long amount,Optional<String> note) {
        // IBAN
        // If IBAN belongs to the same bank account, payment is immediatelly processed
        // If IBAN doesn´t belong to the same bank account, use external API to process payment
        // amount provided by the user
        // optional note given by the user

        return new ResponseEntity<>(transferService.create(transferDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTransfer(@PathVariable final Long id,
            @RequestBody @Valid final TransferDTO transferDTO) {
        transferService.update(id, transferDTO);
        return ResponseEntity.ok().build();
    }

}
