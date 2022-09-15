package pt.ualg.upbank.config;

import java.nio.charset.Charset;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StreamUtils;
import pt.ualg.upbank.UpbankApplication;
import pt.ualg.upbank.repos.AccountRepository;
import pt.ualg.upbank.repos.AddressRepository;
import pt.ualg.upbank.repos.CardRepository;
import pt.ualg.upbank.repos.DirectDebitRepository;
import pt.ualg.upbank.repos.StandingOrderRepository;
import pt.ualg.upbank.repos.TelcoProviderRepository;
import pt.ualg.upbank.repos.TransferRepository;


/**
 * Abstract base class to be extended by every IT test. Starts the Spring Boot context, with all data
 * wiped out before each test.
 */
@SpringBootTest(
        classes = UpbankApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@ActiveProfiles("it")
@Sql({"/data/clearAll.sql", "/data/addressData.sql", "/data/accountData.sql"})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public abstract class BaseIT {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public DirectDebitRepository directDebitRepository;

    @Autowired
    public StandingOrderRepository standingOrderRepository;

    @Autowired
    public CardRepository cardRepository;

    @Autowired
    public TransferRepository transferRepository;

    @Autowired
    public AccountRepository accountRepository;

    @Autowired
    public AddressRepository addressRepository;

    @Autowired
    public TelcoProviderRepository telcoProviderRepository;

    @SneakyThrows
    public String readResource(final String resourceName) {
        return StreamUtils.copyToString(getClass().getResourceAsStream(resourceName), Charset.forName("UTF-8"));
    }

    public String bearerToken() {
        // user bootify, expires 2040-01-01
        return "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9." +
                "eyJzdWIiOiJib290aWZ5IiwiZXhwIjoyMjA4OTg4ODAwfQ." +
                "hmIChYYjZsmeKRvbUiLXScwloZtDQtFvRwkv8rZ1XIjn_LqVA5YzH4tmHAsidG_HNsHu2GjH0ArLp2gsiqGPoQ";
    }

}
