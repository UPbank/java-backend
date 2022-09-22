package pt.ualg.upbank.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pt.ualg.upbank.domain.Account;
import pt.ualg.upbank.domain.StandingOrder;
import pt.ualg.upbank.model.Frequency;
import pt.ualg.upbank.model.SimplePage;
import pt.ualg.upbank.model.StandingOrderDTO;
import pt.ualg.upbank.repos.AccountRepository;
import pt.ualg.upbank.repos.StandingOrderRepository;


@Service
public class StandingOrderService {

    private final StandingOrderRepository standingOrderRepository;
    private final AccountRepository accountRepository;

    public StandingOrderService(final StandingOrderRepository standingOrderRepository,
            final AccountRepository accountRepository) {
        this.standingOrderRepository = standingOrderRepository;
        this.accountRepository = accountRepository;
    }

    public SimplePage<StandingOrderDTO> findAll(final Pageable pageable) {
        final Page<StandingOrder> page = standingOrderRepository.findAll(pageable);
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
        mapToEntity(standingOrderDTO, standingOrder);
        return standingOrderRepository.save(standingOrder).getId();
    }

    public void update(final Long id, final StandingOrderDTO standingOrderDTO) {
        final StandingOrder standingOrder = standingOrderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(standingOrderDTO, standingOrder);
        standingOrderRepository.save(standingOrder);
    }

    public void delete(final Long id) {
        standingOrderRepository.deleteById(id);
    }

    private StandingOrderDTO mapToDTO(final StandingOrder standingOrder,
            final StandingOrderDTO standingOrderDTO) {
        standingOrderDTO.setId(standingOrder.getId());
        standingOrderDTO.setAmount(standingOrder.getAmount());
        standingOrderDTO.setFrequency(standingOrder.getFrequency());
        standingOrderDTO.setSender(standingOrder.getSender() == null ? null : standingOrder.getSender().getId());
        standingOrderDTO.setReceiver(standingOrder.getReceiver() == null ? null : standingOrder.getReceiver().getId());
        return standingOrderDTO;
    }

    private StandingOrder mapToEntity(final StandingOrderDTO standingOrderDTO,
            final StandingOrder standingOrder) {
        standingOrder.setAmount(standingOrderDTO.getAmount());
        standingOrder.setFrequency(standingOrderDTO.getFrequency());
        final Account sender = standingOrderDTO.getSender() == null ? null : accountRepository.findById(standingOrderDTO.getSender())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sender not found"));
        standingOrder.setSender(sender);
        final Account receiver = standingOrderDTO.getReceiver() == null ? null : accountRepository.findById(standingOrderDTO.getReceiver())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "receiver not found"));
        standingOrder.setReceiver(receiver);
        return standingOrder;
    }
    //segundo, minuto, hora, dia, mês, dia da semana

    public void executeScheduledTransfers(Frequency frequency) {
        List<StandingOrder> transfers = standingOrderRepository.findByFrequency(frequency);
        for (StandingOrder so : transfers) {
            createFromIban(so.getIban(), so.getAmount(), so.getSender());
        }
    }

    //a cada dia 
    @Scheduled(cron = "0 0 * * *", zone = "Europe/Portugal")
    public void scheduleDailyTask() {
        executeScheduledTransfers(Frequency.DAILY);
    }

    //a cada semana
    @Scheduled(cron = "0 0 * * 0", zone="Europe/Portugal")
    public void scheduleWeeklyTask() {
        executeScheduledTransfers(Frequency.WEEKLY);
    }

    // a cada mês
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
