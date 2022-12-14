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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import pt.ualg.upbank.model.GovernmentTransferDTO;
import pt.ualg.upbank.model.IbanTransferDTO;
import pt.ualg.upbank.model.ServiceTransferDTO;
import pt.ualg.upbank.model.SimplePage;
import pt.ualg.upbank.model.TelcoTransferDTO;
import pt.ualg.upbank.model.TransferDTO;
import pt.ualg.upbank.model.UpdateTransferDTO;
import pt.ualg.upbank.service.TransferService;

import java.time.OffsetDateTime;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/transfers", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('" + ROLE_USER + "')")
@SecurityRequirement(name = "bearer-jwt")
public class TransferResource {
	// Added to request the account of the user to make transfers

	private final AccountResource accountResource;

	private final TransferService transferService;

	public TransferResource(final TransferService transferService, final AccountResource accountResource) {
		this.transferService = transferService;

		this.accountResource = accountResource;

	}

	@Operation(parameters = {
			@Parameter(name = "page", in = ParameterIn.QUERY, schema = @Schema(implementation = Integer.class)),
			@Parameter(name = "size", in = ParameterIn.QUERY, schema = @Schema(implementation = Integer.class)),
			@Parameter(name = "sort", in = ParameterIn.QUERY, schema = @Schema(implementation = String.class))
	})

	@GetMapping("/")
	public ResponseEntity<SimplePage<TransferDTO>> getTransfers(
			@Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable) {
		return ResponseEntity.ok(transferService.getAll(accountResource.getRequestUser().getId(), pageable));
	}

	@GetMapping("/filtered")
	public ResponseEntity<SimplePage<TransferDTO>> getFilterdTransfers(
			@Parameter(hidden = true) @SortDefault(sort = "id") @PageableDefault(size = 20) final Pageable pageable,
			@RequestParam("startDate") Optional<String> startDate, @RequestParam("endDate") Optional<String> endDate,
			@RequestParam("name") Optional<String> name) {
		String start = startDate == null ? null : startDate.get();
		OffsetDateTime startTime = OffsetDateTime.parse(start);

		String end = endDate == null ? null : endDate.get();
		OffsetDateTime endTime = OffsetDateTime.parse(end);

		String names = name == null ? null : name.get();

		if (startTime.isAfter(endTime) || startTime == null && endTime != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "filterByDate.endDateInvalid");
		}
		if (start == null && name != null) {

			return ResponseEntity.ok(transferService.getByName(accountResource.getRequestUser().getId(), names, pageable));
		}
		if (start != null && name == null) {
			if (endTime == null) {
				endTime = OffsetDateTime.now();
			}
			return ResponseEntity.ok(transferService.getByStartDateandEndDate(accountResource.getRequestUser().getId(),
					startTime, endTime, pageable));
		}
		// return
		// ResponseEntity.ok(transferService.getByStartDateAndEndDateAndName(accountResource)
		return null;
	}

	@PostMapping("/servicePayments")
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Long> createServicePayment(@RequestBody ServiceTransferDTO serviceTransferDTO) {

		if (!transferService.checkEntity(serviceTransferDTO.getEntity())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "entity.must.have.5.digits");
		}
		if (!transferService.checkReference(serviceTransferDTO.getReference())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "reference.must.have.9.digits");
		}

		return new ResponseEntity<>(transferService.createFromEntity(serviceTransferDTO.getEntity(),
				serviceTransferDTO.getReference(), serviceTransferDTO.getAmount(), accountResource.getRequestUser().getId()),
				HttpStatus.CREATED);
	}

	@PostMapping("/governmentPayments")
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Long> createTransfer(@RequestBody GovernmentTransferDTO governmentTransferDTO) {
		// reference with 15 digits
		// amount provided by the user
		if (!transferService.checkGovernamentReference(governmentTransferDTO.getReference())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "reference.must.have.15.digits");
		}
		if (!transferService.checkPositiveAmount(governmentTransferDTO.getAmount())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount.must.be.positive");
		}
		return new ResponseEntity<>(transferService.createFromGovernment(governmentTransferDTO.getReference(),
				governmentTransferDTO.getAmount(), accountResource.getRequestUser().getId()), HttpStatus.CREATED);
	}

	@PostMapping("/telcoPayments")
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Long> createTransfer(@RequestBody TelcoTransferDTO telcoTransferDTO) {
		// provider
		// Lycamobile GT MOBile, MEO, MEO Card, MEO Card - PT HEllo/ PT Card, MEO CArd -
		// Telefone Hello, MEO Escola Digital, Moche, NOS, NOS - Escola Digital, Sapo,
		// Sapo ADSL, UZO, Via Card, Vodafone, WTF.
		// phone number provided by the user
		// phone number with 9 digits
		// phone number start with 91,92, 93 or 96
		// amount provided by the user

		if (!transferService.checkTelcoProvider(telcoTransferDTO.getProvider())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "telco.provider.not.found");
		}
		if (!transferService.checkPhoneDigits(telcoTransferDTO.getPhoneNumber())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "phone.must.have.9.digits");
		}
		if (!transferService.checkPhoneNumberStartingDigits(telcoTransferDTO.getPhoneNumber())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "phoneNumber.invalid");
		}

		return new ResponseEntity<>(transferService.createFromPhoneNumber(telcoTransferDTO.getProvider(),
				telcoTransferDTO.getAmount(), accountResource.getRequestUser(), telcoTransferDTO.getPhoneNumber()),
				HttpStatus.CREATED);

	}

	@PostMapping("/bankTransfers")
	@ApiResponse(responseCode = "201")
	public ResponseEntity<Long> createTransfer(@RequestBody IbanTransferDTO ibanTransferDTO) {
		return new ResponseEntity<>(
				transferService.createFromIban(ibanTransferDTO, accountResource.getRequestUser().getId()), HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Void> updateTransfer(@PathVariable final Long id,
			@RequestBody @Valid final UpdateTransferDTO updatetransferDTO) {

		transferService.update(id, updatetransferDTO, accountResource.getRequestUser().getId());
		return ResponseEntity.ok().build();
	}

}
