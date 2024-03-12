package Tables;

import Tables.Analytics.Analytic;
import Tables.Analytics.AnalyticRepository;
import Tables.BirdInfo.BirdInfo;
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
import Tables.Populator.BirdInfoPopulator;
import java.util.List;


/**
 * @author Brian Xicon
 */

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
            //User Creation
//            User user1 = new User("tester1", "john@somemail.com", "password", 0);
//            User user2 = new User("tester2", "jane@somemail.com", "password", 1);
//            User user3 = new User("tester3", "justin@somemail.com", "password", 3);
//            //Bird Tracking Info Creation
//            BirdTrackingInfo track1 = new BirdTrackingInfo(42.505508, -90.667313, "10/04/2023", "13:58:01", "https://cdn.britannica.com/89/117189-050-FB791D73/American-robin.jpg?w=300");
//            BirdTrackingInfo track2 = new BirdTrackingInfo(42.026798, -93.620178, "10/03/2023", "14:03:25", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Corvus-brachyrhynchos-001.jpg/330px-Corvus-brachyrhynchos-001.jpg");
//            BirdTrackingInfo track3 = new BirdTrackingInfo(14.646400, -90.736770, "10/02/2023", "18:38:04", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/About_to_Launch_%2826075320352%29.jpg/330px-About_to_Launch_%2826075320352%29.jpg");
//            //Bird Info Creation
//            BirdInfo bird1 = new BirdInfo("Turdus migratorius", "American Robin", "The American robin (Turdus migratorius), a large North American thrush, is one of the most familiar songbirds in the eastern United States.", "https://cdn.britannica.com/89/117189-050-FB791D73/American-robin.jpg?w=300", "https://www.allaboutbirds.org/guide/assets/photo/90628731-720px.jpg", "https://www.allaboutbirds.org/guide/American_Robin/sounds#");
//            BirdInfo bird2 = new BirdInfo("Corvus brachyrhynchos", "American Crow", "It is a common bird found throughout much of North America. American crows are the New World counterpart to the carrion crow and the hooded crow of Eurasia; they all occupy the same ecological niche.", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Corvus-brachyrhynchos-001.jpg/330px-Corvus-brachyrhynchos-001.jpg", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Corvus_brachyrhynchos_distribution.png/330px-Corvus_brachyrhynchos_distribution.png", "https://www.allaboutbirds.org/guide/American_Crow/sounds");
//            BirdInfo bird3 = new BirdInfo("Haliaeetus leucocephalus", "Bald Eagle", "The bald eagle is an opportunistic feeder which subsists mainly on fish, which it swoops down upon and snatches from the water with its talons.", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/About_to_Launch_%2826075320352%29.jpg/330px-About_to_Launch_%2826075320352%29.jpg", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/57/Distribution_H._leucocephalus.png/330px-Distribution_H._leucocephalus.png", "https://www.allaboutbirds.org/guide/Bald_Eagle/sounds");
//            //Analytic Creation
            Analytic analytic1 = new Analytic("10/04/2023", 123, 1203);
//
//            //Saving the new BirdTrackingInfo objects to the repository.
//            birdTrackingInfoRepository.save(track1);
//            birdTrackingInfoRepository.save(track2);
//            birdTrackingInfoRepository.save(track3);
//
//            //Linking the bird tracking info objects to the users.
//            user1.addBirdTrackingInfo(track1);
//            user1.addBirdTrackingInfo(track2);
//            user2.addBirdTrackingInfo(track3);
//            //Linking the bird tracking info objects to specific birds.
//            bird1.addBirdTrackingInfo(track1);
//            bird2.addBirdTrackingInfo(track2);
//            bird3.addBirdTrackingInfo(track3);
//            //Linking the analytic to the user.
//            user1.addAnalytic(analytic1);
//
//            //Saving the users to the repository.
//            userRepository.save(user1);
//            userRepository.save(user2);
//            userRepository.save(user3);
//            //Saving the birds to the repository
//            birdInfoRepository.save(bird1);
//            birdInfoRepository.save(bird2);
//            birdInfoRepository.save(bird3);
//
//            //Saving the analytic to the repository and re-saving the user.
//            analyticRepository.save(analytic1);
//            userRepository.save(user1);

//            //Adds the scientific & common name for all birdInfo
//            List<BirdInfo> birdInfoList = BirdInfoPopulator.readSpeciesList("speciesList.txt");
////            //Adds the description & image for all birdInfo
//            BirdInfoPopulator.populateBirdInfoFromWikipedia(birdInfoList);
//            birdInfoRepository.saveAll(birdInfoList);
//            int ID = 0;
//            for (BirdInfo birdInfo : birdInfoList) {
//                System.out.println("Common Name: " + birdInfo.getName());
//                System.out.println("Scientific Name: " + birdInfo.getScientificName());
//                System.out.println("Short Desc: " + birdInfo.getShortDesc());
//                System.out.println("Image: " + birdInfo.getImage());
//                System.out.println("Range Map: " + birdInfo.getRangeMap());
//                System.out.println("Call Sound: " + birdInfo.getCallSound());
//                ID++;
//                System.out.println("ID: "+ ID);
//                System.out.println();
//            }
//            //printWikiInfo("Anna's Hummingbird");
        };
    }

}
