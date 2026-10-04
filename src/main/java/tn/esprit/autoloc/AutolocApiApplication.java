package tn.esprit.autoloc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tn.esprit.autoloc.domain.CategorieVehicule;
import tn.esprit.autoloc.domain.StatutVehicule;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.repository.VehiculeRepository;

import java.math.BigDecimal; // Import important !

@SpringBootApplication
public class AutolocApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutolocApiApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(VehiculeRepository vehiculeRepository) {
        return args -> {
            if (vehiculeRepository.count() == 0) {
                Vehicule v1 = new Vehicule();
                v1.setImmatriculation("200-TUN-1234");
                v1.setMarque("Renault");
                v1.setModele("Clio 5");
                v1.setCategorie(CategorieVehicule.CITADINE);
                v1.setStatut(StatutVehicule.DISPONIBLE);
                v1.setTarifJournalier(BigDecimal.valueOf(90.0)); // Correction ici

                Vehicule v2 = new Vehicule();
                v2.setImmatriculation("210-TUN-5678");
                v2.setMarque("Peugeot");
                v2.setModele("208");
                v2.setCategorie(CategorieVehicule.CITADINE);
                v2.setStatut(StatutVehicule.DISPONIBLE);
                v2.setTarifJournalier(BigDecimal.valueOf(110.0)); // Correction ici

                vehiculeRepository.save(v1);
                vehiculeRepository.save(v2);

                System.out.println("Vehicules de demonstration insérés avec succès !");
            }
        };
    }
}