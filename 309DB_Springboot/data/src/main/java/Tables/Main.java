package Tables;

import Tables.Analytics.AnalyticRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import Tables.BirdInfo.BirdInfoRepository;
import Tables.Users.UserRepository;
import Tables.BirdTrackingInfo.BirdTrackingInfoRepository;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaRepositories
@EnableWebSecurity
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    /**
     * 
     * @param userRepository repository for the User entity
     * @param birdInfoRepository repository for the BirdInfo entity
     * @param birdTrackingInfoRepository repository for the BirdTrackingInfo entity
     * @param analyticRepository repository for the Analytic entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */

    @Bean

    CommandLineRunner initUser(UserRepository userRepository, BirdInfoRepository birdInfoRepository, BirdTrackingInfoRepository birdTrackingInfoRepository, AnalyticRepository analyticRepository) {
        return args -> {
        };
    }

}
