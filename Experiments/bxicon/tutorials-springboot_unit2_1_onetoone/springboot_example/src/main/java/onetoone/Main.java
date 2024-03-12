package onetoone;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetoone.BirdInfo.BirdInfo;
import onetoone.BirdInfo.BirdInfoRepository;
import onetoone.Users.User;
import onetoone.Users.UserRepository;

/**
 * @author Brian Xicon
 */ 

@SpringBootApplication
class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    // Create 3 users with their machines
    /**
     * 
     * @param userRepository repository for the User entity
     * @param birdInfoRepository repository for the BirdInfo entity
     * Creates a commandLine runner to enter dummy data into the database
     * As mentioned in User.java just associating the Laptop object with the User will save it into the database because of the CascadeType
     */
    @Bean
    CommandLineRunner initUser(UserRepository userRepository, BirdInfoRepository birdInfoRepository) {
        return args -> {
            User user1 = new User("tester1", "john@somemail.com", "password", 0);
            User user2 = new User("tester2", "jane@somemail.com", "password", 1);
            User user3 = new User("tester3", "justin@somemail.com", "password", 3);
            BirdInfo bird1 = new BirdInfo("Turdus migratorius", "American Robin", "The American robin (Turdus migratorius), a large North American thrush, is one of the most familiar songbirds in the eastern United States.", "https://cdn.britannica.com/89/117189-050-FB791D73/American-robin.jpg?w=300", "https://www.allaboutbirds.org/guide/assets/photo/90628731-720px.jpg", "https://www.allaboutbirds.org/guide/American_Robin/sounds#");
            BirdInfo bird2 = new BirdInfo("Corvus brachyrhynchos", "American Crow", "It is a common bird found throughout much of North America. American crows are the New World counterpart to the carrion crow and the hooded crow of Eurasia; they all occupy the same ecological niche.", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/Corvus-brachyrhynchos-001.jpg/330px-Corvus-brachyrhynchos-001.jpg", "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/Corvus_brachyrhynchos_distribution.png/330px-Corvus_brachyrhynchos_distribution.png", "https://www.allaboutbirds.org/guide/American_Crow/sounds");
            BirdInfo bird3 = new BirdInfo("Haliaeetus leucocephalus", "Bald Eagle", "The bald eagle is an opportunistic feeder which subsists mainly on fish, which it swoops down upon and snatches from the water with its talons.", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/About_to_Launch_%2826075320352%29.jpg/330px-About_to_Launch_%2826075320352%29.jpg", "https://upload.wikimedia.org/wikipedia/commons/thumb/5/57/Distribution_H._leucocephalus.png/330px-Distribution_H._leucocephalus.png", "https://www.allaboutbirds.org/guide/Bald_Eagle/sounds");
            user1.setBirdInfo(bird1);
            user2.setBirdInfo(bird2);
            user3.setBirdInfo(bird3);
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

        };
    }

}
