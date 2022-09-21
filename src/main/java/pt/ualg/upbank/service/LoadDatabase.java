package pt.ualg.upbank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pt.ualg.upbank.domain.Address;
import pt.ualg.upbank.domain.TelcoProvider;
import pt.ualg.upbank.model.AccountDTO;
import pt.ualg.upbank.model.AddressDTO;
import pt.ualg.upbank.model.TelcoProviderDTO;
import pt.ualg.upbank.repos.TelcoProviderRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.OffsetDateTime;



@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(TelcoProviderRepository telcoProviderRepository, TelcoProviderService telcoProviderService, AccountService accountService) {
    // TelcoProvider altice = new TelcoProvider();
    // altice.setId((long)1);
    // altice.setName("Altice");
    // altice.setDateCreated(OffsetDateTime.now());
    // altice.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider nos = new TelcoProvider();
    // nos.setId((long)2);
    // nos.setName("NOS");
    // nos.setDateCreated(OffsetDateTime.now());
    // nos.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider moche = new TelcoProvider();
    // moche.setId((long)3);
    // moche.setName("Moche");
    // moche.setDateCreated(OffsetDateTime.now());
    // moche.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider sapo = new TelcoProvider();
    // sapo.setId((long)4);
    // sapo.setName("Sapo");
    // sapo.setDateCreated(OffsetDateTime.now());
    // sapo.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider uzo = new TelcoProvider();
    // uzo.setId((long)5);
    // uzo.setName("UZO");
    // uzo.setDateCreated(OffsetDateTime.now());
    // uzo.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider viaCard = new TelcoProvider();
    // viaCard.setId((long)6);
    // viaCard.setName("Via Card");
    // viaCard.setDateCreated(OffsetDateTime.now());
    // viaCard.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider vodafone = new TelcoProvider();
    // vodafone.setId((long)7);
    // vodafone.setName("Vodafone");
    // vodafone.setDateCreated(OffsetDateTime.now());
    // vodafone.setLastUpdated(OffsetDateTime.now());

    // TelcoProvider wtf = new TelcoProvider();
    // wtf.setId((long)8);
    // wtf.setName("WTF");
    // wtf.setDateCreated(OffsetDateTime.now());
    // wtf.setLastUpdated(OffsetDateTime.now());

    return args -> {
      // log.info("Preloading " + telcoProviderRepository.save(altice));
      // log.info("Preloading " + telcoProviderRepository.save(nos));
      // log.info("Preloading " + telcoProviderRepository.save(moche));
      // log.info("Preloading " + telcoProviderRepository.save(sapo));
      // log.info("Preloading " + telcoProviderRepository.save(uzo));
      // log.info("Preloading " + telcoProviderRepository.save(viaCard));
      // log.info("Preloading " + telcoProviderRepository.save(vodafone));
      // log.info("Preloading " + telcoProviderRepository.save(wtf));

      log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)1, "Altice")));

      log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)2, "Nos")));

      log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)3, "Moche")));

      log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)4, "Sapo")));

      log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)5, "Uzo")));
      

      log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)6, "Via Card")));

      log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)7, "Vodafone")));

      log.info("Preloading " + telcoProviderService.create(new TelcoProviderDTO((long)8, "WTF")));

      //governo
      log.info("Preloading " + accountService.create(new AccountDTO((long)9, "gov@gov.pt", "1234", "Governo de Portugal", LocalDate.of(1143, 10, 5) , "9999999", "0000000001", (long)0, new AddressDTO()))); //NOSONAR

      //entidade
      log.info("Preloading " + accountService.create(new AccountDTO((long)10, "pdrchrld@gmail.com", "2345", "Pedro Charlito", LocalDate.of(1994, 2, 3) , "020202020", "0000000002", (long)0, new AddressDTO()))); //NOSONAR

      //telecomunicações
      log.info("Preloading " + accountService.create(new AccountDTO((long)11, "telco@telco.pt", "3456", "Telecomunicações", LocalDate.of(1990, 1, 1) , "030303030", "0000000003", (long)0, new AddressDTO()))); //NOSONAR

    };

      
  }
}
