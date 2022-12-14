package pt.ualg.upbank.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import pt.ualg.upbank.IBAN.IBAN;
import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.StandingOrder;
import pt.ualg.upbank.model.Frequency;
import pt.ualg.upbank.model.IbanTransferDTO;
import pt.ualg.upbank.model.SimplePage;
import pt.ualg.upbank.model.StandingOrderDTO;
import pt.ualg.upbank.model.UpdateStandingOrderDTO;
import pt.ualg.upbank.repos.AccountRepository;
import pt.ualg.upbank.repos.StandingOrderRepository;

@Service
public class StandingOrderService {

	private final StandingOrderRepository standingOrderRepository;
	private final AccountRepository accountRepository;
	private final TransferService transferService;

	public StandingOrderService(final StandingOrderRepository standingOrderRepository,
			final AccountRepository accountRepository, final TransferService transferService) {
		this.standingOrderRepository = standingOrderRepository;
		this.accountRepository = accountRepository;
		this.transferService = transferService;
	}

	public SimplePage<StandingOrderDTO> findAll(final Long id, final Pageable pageable) {
		final Account account = accountRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "transfers.account.notFound"));
		final Page<StandingOrder> page = standingOrderRepository.findBySender(account, pageable);
		return new SimplePage<>(page.getContent()
				.stream()
				.map(standingOrder -> mapToDTO(standingOrder, new StandingOrderDTO()))
				.collect(Collectors.toList()),
				page.getTotalElements(), pageable);
	}

	public StandingOrderDTO get(final Long id) {
		return standingOrderRepository.findById(id)
				.map(standingOrder -> mapToDTO(standingOrder, new StandingOrderDTO()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	public Long create(final StandingOrderDTO standingOrderDTO) {
		final StandingOrder standingOrder = new StandingOrder();
		standingOrder.setDateCreated(OffsetDateTime.now());
		standingOrder.setLastUpdated(OffsetDateTime.now());
		mapToEntity(standingOrderDTO, standingOrder);
		return standingOrderRepository.save(standingOrder).getId();
	}

	public void update(final Long id, final UpdateStandingOrderDTO updateStandingOrder, final Long accountId) {
		final Account account = accountRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
		final StandingOrder standingOrder = standingOrderRepository.findBySenderAndId(account, id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		if (updateStandingOrder.getAmount() != null) {
			standingOrder.setAmount(updateStandingOrder.getAmount());
		}
		if (updateStandingOrder.getReceiverIban() != null) {
			standingOrder.setIban(updateStandingOrder.getReceiverIban());
		}
		if (updateStandingOrder.getFrequency() != null) {
			standingOrder.setFrequency(updateStandingOrder.getFrequency());
		}

		standingOrder.setLastUpdated(OffsetDateTime.now());
		standingOrderRepository.save(standingOrder);
	}

	public void delete(final Long id, final Long accountId) {

		Account sender = accountRepository.findById(accountId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "standingOrder.sender.notFound"));
		if (standingOrderRepository.existsBySenderAndId(sender, id)) {

			standingOrderRepository.deleteById(id);
		}
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "delete.standingOrder.notFound");
	}

	private StandingOrderDTO mapToDTO(final StandingOrder standingOrder,
			final StandingOrderDTO standingOrderDTO) {
		standingOrderDTO.setId(standingOrder.getId());
		standingOrderDTO.setAmount(standingOrder.getAmount());
		standingOrderDTO.setFrequency(standingOrder.getFrequency());
		standingOrderDTO.setSender(standingOrder.getSender() == null ? null : standingOrder.getSender().getId());
		standingOrderDTO.setIban(standingOrder.getIban() == null ? null : standingOrder.getIban());
		return standingOrderDTO;
	}

	private StandingOrder mapToEntity(final StandingOrderDTO standingOrderDTO,
			final StandingOrder standingOrder) {
		standingOrder.setAmount(standingOrderDTO.getAmount());
		standingOrder.setFrequency(standingOrderDTO.getFrequency());
		final Account sender = standingOrderDTO.getSender() == null ? null
				: accountRepository.findById(standingOrderDTO.getSender())
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sender not found"));
		standingOrder.setSender(sender);
		if (!new IBAN(standingOrderDTO.getIban()).validate()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "receiver not found");
		}
		standingOrder.setIban(standingOrderDTO.getIban());
		return standingOrder;
	}
	// segundo, minuto, hora, dia, m??s, dia da semana

	public void executeScheduledTransfers(Frequency frequency) {
		List<StandingOrder> transfers = standingOrderRepository.findByFrequency(frequency);
		for (StandingOrder so : transfers) {
			StandingOrderDTO soDTO = mapToDTO(so, new StandingOrderDTO());
			IbanTransferDTO ibanTransferDTO = new IbanTransferDTO();
			ibanTransferDTO.setAmount(soDTO.getAmount());
			ibanTransferDTO.setIban(soDTO.getIban());

			transferService.createFromIban(ibanTransferDTO, soDTO.getSender());
		}
	}

	// a cada dia
	@Scheduled(cron = "0 0 * * *", zone = "Europe/Portugal")
	public void scheduleDailyTask() {
		executeScheduledTransfers(Frequency.DAILY);
	}

	// a cada semana
	@Scheduled(cron = "0 0 * * 0", zone = "Europe/Portugal")
	public void scheduleWeeklyTask() {
		executeScheduledTransfers(Frequency.WEEKLY);
	}

	// a cada m??s
	@Scheduled(cron = "0 0 1 * *", zone = "Europe/Portugal")
	public void scheduleMonthlyTask() {
		executeScheduledTransfers(Frequency.MONTHLY);
	}

	// 0 0 1 1 *
	@Scheduled(cron = "0 0 1 1 *", zone = "Europe/Portugal")
	public void scheduleYearlyTask() {
		executeScheduledTransfers(Frequency.YEARLY);
	}

}
