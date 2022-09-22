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
import pt.ualg.upbank.model.DirectDebitDTO;
import pt.ualg.upbank.model.SimplePage;
import pt.ualg.upbank.service.DirectDebitService;


@RestController
@RequestMapping(value = "/api/directDebits", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('" + ROLE_USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class DirectDebitResource {

    private final DirectDebitService directDebitService;

    public DirectDebitResource(final DirectDebitService directDebitService) {
        this.directDebitService = directDebitService;
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
    public ResponseEntity<SimplePage<DirectDebitDTO>> getAllDirectDebits(
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(directDebitService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectDebitDTO> getDirectDebit(@PathVariable final Long id) {
        return ResponseEntity.ok(directDebitService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createDirectDebit(
            @RequestBody @Valid final DirectDebitDTO directDebitDTO) {
        return new ResponseEntity<>(directDebitService.create(directDebitDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDirectDebit(@PathVariable final Long id,
            @RequestBody @Valid final DirectDebitDTO directDebitDTO, @RequestBody Boolean bool) {
        directDebitService.update(id, directDebitDTO, bool);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDirectDebit(@PathVariable final Long id) {
        directDebitService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
