package Tables.Users;

import java.util.List;

import Tables.Analytics.Analytic;
import Tables.Analytics.AnalyticRepository;
import Tables.BirdTrackingInfo.BirdTrackingInfo;
import Tables.BirdTrackingInfo.BirdTrackingInfoRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Api(value = "UserController", description = "REST APIs related to User entity")
@RestController
public class UserController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;

    @Autowired
    BirdTrackingInfoRepository birdTrackingInfoRepository;

    @Autowired
    AnalyticRepository analyticRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    //Get All Users

    @ApiOperation(value = "Get list of Students in the System", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(path = "/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    //Get Specific User By ID
    @ApiOperation(value = "Get specific user by id", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(path = "/users/{id}")
    User getUserById( @PathVariable int id){
        return userRepository.findById(id);
    }

    //Create a New User
    @ApiOperation(value = "Create new user with json", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping(path = "/users")
    String createUser(@RequestBody User user){
        if (user == null)
            return failure;
        User newUser = new User(user.getUsername(),user.getEmail(),bCryptPasswordEncoder.encode(user.getPassword()),user.getPrivilege());
        userRepository.save(newUser);
        return success;
    }

    //Update an Existing User
    @ApiOperation(value = "Update an existing user", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })

    @PutMapping("/users/{id}")
    User updateUser(@PathVariable int id, @RequestBody User request){
        User user = userRepository.findById(id);
        if(user == null)
            return null;
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setPrivilege(request.getPrivilege());
        userRepository.save(user);
        return userRepository.findById(id);
    }

    //Delete a User
    @ApiOperation(value = "Delete a user", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @DeleteMapping(path = "/users/{id}")
    String deleteUser(@PathVariable int id){
        userRepository.deleteById(id);
        return success;
    }

    //Assign A BirdTrackingInfo Point to a User
    @ApiOperation(value = "Assign birdTrackingInfo to a user", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping("/users/{userId}/birdTrackingInfo/{birdTrackingInfoID}")
    String assignBirdTrackingInfoToUser(@PathVariable int userId, @PathVariable int birdTrackingInfoID) {
        User user = userRepository.findById(userId);
        BirdTrackingInfo birdTrackingInfo = birdTrackingInfoRepository.findById(birdTrackingInfoID);

        if (user == null || birdTrackingInfo == null)
            return failure;

        user.addBirdTrackingInfo(birdTrackingInfo);
        userRepository.save(user);

        return success;
    }

    //Assign An Analytic To A User
    @ApiOperation(value = "Assign analytic to user", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping("/users/{userId}/analytic/{analyticID}")
    String assignAnalyticToUser(@PathVariable int userId, @PathVariable int analyticID) {
        User user = userRepository.findById(userId);
        Analytic analytic = analyticRepository.findById(analyticID);

        if (user == null || analytic == null)
            return failure;

        user.addAnalytic(analytic);
        userRepository.save(user);

        return success;
    }

    //Get A User's Bird Tracking Info
    @ApiOperation(value = "Get specepfic user's birdTrackingInfo", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/users/{userId}/birdTrackingInfo")
    List<BirdTrackingInfo> getUserBirdTrackingInfo(@PathVariable int userId) {
        User user = userRepository.findById(userId);
        if (user == null)
            return null;

        return user.getBirdTrackingInfo(); // Assuming you have a method to get the associated bird tracking info from the user
    }

    //Get A User's Analytics
    @ApiOperation(value = "Get specefic user's analytics", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/users/{userId}/analytics")
    Analytic getUserAnalytics(@PathVariable int userId) {
        User user = userRepository.findById(userId);

        if (user == null) {
            return null;
        }

        return user.getAnalytics(); // Assuming you have a method to get the associated analytics from the user
    }
}
