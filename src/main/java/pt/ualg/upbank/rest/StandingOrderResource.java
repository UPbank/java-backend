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
import pt.ualg.upbank.model.UpdateStandingOrderDTO;
import pt.ualg.upbank.service.StandingOrderService;

@RestController
@RequestMapping(value = "/api/standingOrders", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('" + ROLE_USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class StandingOrderResource {

	private final StandingOrderService standingOrderService;
	private final AccountResource accountResource;

	public StandingOrderResource(final StandingOrderService standingOrderService, final AccountResource accountResource) {
		this.standingOrderService = standingOrderService;
		this.accountResource = accountResource;
	}

	@Operation(parameters = {
			@Parameter(name = "page", in = ParameterIn.QUERY, schema = @Schema(implementation = Integer.class)),
			@Parameter(name = "size", in = ParameterIn.QUERY, schema = @Schema(implementation = Integer.class)),
			@Parameter(name = "sort", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
	})
	@GetMapping("/")
	public ResponseEntity<SimplePage<StandingOrderDTO>> getAllStandingOrders(
			@Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
		return ResponseEntity.ok(standingOrderService.findAll(accountResource.getRequestUser().getId(), pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<StandingOrderDTO> getStandingOrder(@PathVariable final Long id) {
		return ResponseEntity.ok(standingOrderService.get(id));
	}

	@PostMapping("/")
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Long> createStandingOrder(
			@RequestBody @Valid final StandingOrderDTO standingOrderDTO) {
		standingOrderDTO.setSender(accountResource.getRequestUser().getId());
		return new ResponseEntity<>(standingOrderService.create(standingOrderDTO), HttpStatus.CREATED);

	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateStandingOrder(@PathVariable final Long id,
			@RequestBody @Valid final UpdateStandingOrderDTO updateStandingOrderDTO) {
		standingOrderService.update(id, updateStandingOrderDTO, accountResource.getRequestUser().getId());
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{id}")
	@ApiResponse(responseCode = "204")
	public ResponseEntity<Void> deleteStandingOrder(@PathVariable final Long id) {
		standingOrderService.delete(id, accountResource.getRequestUser().getId());
		return ResponseEntity.noContent().build();
	}

}
