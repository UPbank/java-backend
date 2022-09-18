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
import pt.ualg.upbank.model.StandingOrderDTO;
import pt.ualg.upbank.service.StandingOrderService;


@RestController
@RequestMapping(value = "/api/standingOrders", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('" + ROLE_USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class StandingOrderResource {

    private final StandingOrderService standingOrderService;

    public StandingOrderResource(final StandingOrderService standingOrderService) {
        this.standingOrderService = standingOrderService;
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
    public ResponseEntity<SimplePage<StandingOrderDTO>> getAllStandingOrders(
            @Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
        return ResponseEntity.ok(standingOrderService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StandingOrderDTO> getStandingOrder(@PathVariable final Long id) {
        return ResponseEntity.ok(standingOrderService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createStandingOrder(
            @RequestBody @Valid final StandingOrderDTO standingOrderDTO) {
        return new ResponseEntity<>(standingOrderService.create(standingOrderDTO), HttpStatus.CREATED);

        //If a transfer is repeated weekly, the system shall process the transfers exactly 7 days apart, on the same week day.
        // If a transfer is repeated monthly, the system shall process the transfers on the same day of the month. If the day doesn't exist in a given month, the last day of the month shall be used instead.
        // If a transfer is repeated yearly, the system shall process transfers on the exact same day, If the given day is the 29th of February and a given year isn't a leap year, the payment shall be processed on the 28th of February that year."
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateStandingOrder(@PathVariable final Long id,
            @RequestBody @Valid final StandingOrderDTO standingOrderDTO) {
        standingOrderService.update(id, standingOrderDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteStandingOrder(@PathVariable final Long id) {
        standingOrderService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
