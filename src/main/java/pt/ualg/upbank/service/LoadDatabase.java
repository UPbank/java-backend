package pt.ualg.upbank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pt.ualg.upbank.domain.TelcoProvider;
import pt.ualg.upbank.model.TelcoProviderDTO;
import pt.ualg.upbank.repos.TelcoProviderRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.OffsetDateTime;



@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(TelcoProviderRepository telcoProviderRepository) {
    TelcoProvider altice= new TelcoProvider();
    altice.setId((long)1);
    altice.setName("Altice");
    altice.setDateCreated(OffsetDateTime.now());
    altice.setLastUpdated(OffsetDateTime.now());

    TelcoProvider nos= new TelcoProvider();
    nos.setId((long)1);
    nos.setName("Altice");
    nos.setDateCreated(OffsetDateTime.now());
    nos.setLastUpdated(OffsetDateTime.now());

    return args -> {
      log.info("Preloading " + telcoProviderRepository.save(altice));
      log.info("Preloading " + telcoProviderRepository.save(nos));


      
      log.info("Preloading " + repository.save(new Experience("MIEB", ExperienceType.WORK, "Desenvolvimento e caraterização de hidrogéis físicos e químicos a partir de polissacarídeos naturais", LocalDate.parse("2016-07-15"))));
    };
  }
}
