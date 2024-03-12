package onetoone.BirdInfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import onetoone.Users.User;
import onetoone.Users.UserRepository;

/**
 * @author Brian Xicon
 */

@RestController
public class BirdInfoController {

    @Autowired
    BirdInfoRepository birdInfoRepository;

    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/birdInfo")
    List<BirdInfo> getAllBirdInfo(){
        return birdInfoRepository.findAll();
    }

    @GetMapping(path = "/birdInfo/{id}")
    BirdInfo getBirdInfoByID(@PathVariable int id){
        return birdInfoRepository.findById(id);
    }

    @PostMapping(path = "/birdInfo")
    String createBirdInfo(@RequestBody BirdInfo BirdInfo){
        if (BirdInfo == null)
            return failure;
        birdInfoRepository.save(BirdInfo);
        return success;
    }

    @PutMapping(path = "/birdInfo/{id}")
    BirdInfo updateBirdInfo(@PathVariable int id, @RequestBody BirdInfo request){
        BirdInfo birdInfo = birdInfoRepository.findById(id);
        if(birdInfo == null)
            return null;
        birdInfoRepository.save(request);
        return birdInfoRepository.findById(id);
    }

    @DeleteMapping(path = "/birdInfo/{id}")
    String deleteBirdInfo(@PathVariable int id){
        // Check if there is an object depending on user and then remove the dependency
        User user = userRepository.findByBirdInfo_Id(id);
        user.setBirdInfo(null);
        userRepository.save(user);

        // delete the laptop if the changes have not been reflected by the above statement
        birdInfoRepository.deleteById(id);
        return success;
    }
}
