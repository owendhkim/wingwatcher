package Tables.BirdTrackingInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;

/**
 *
 * @author Owen Kim
 *
 */
@Api(value = "BirdTrackingInfoController", description = "REST APIs related to BirdTrackingInfo entity")
@RestController
public class BirdTrackingInfoController {

    @Autowired
    BirdTrackingInfoRepository birdTrackingInfoRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get all list of birdTrackingInfos", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(path = "/birdtrackinginfos")
    List<BirdTrackingInfo> getAllBirdtrackinginfos(){
        return birdTrackingInfoRepository.findAll();
    }

    @ApiOperation(value = "Get specepfic birdTrackingInfo by id", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(path = "/birdtrackinginfos/{id}")
    BirdTrackingInfo getBirdtrackinginfoById(@PathVariable int id){
        return birdTrackingInfoRepository.findById(id);
    }

    @ApiOperation(value = "Create new birdTrackingInfo", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping(path = "/birdtrackinginfos")
    String createBirdtrackinginfo(@RequestBody BirdTrackingInfo Birdtrackinginfo){
        if (Birdtrackinginfo == null)
            return failure;
        birdTrackingInfoRepository.save(Birdtrackinginfo);
        return success;
    }

    @ApiOperation(value = "Update specific birdTrackingInfo by id", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping(path = "/birdtrackinginfos/{id}")
    BirdTrackingInfo updateBirdtrackinginfo(@PathVariable int id, @RequestBody BirdTrackingInfo request){
        BirdTrackingInfo birdtrackinginfo = birdTrackingInfoRepository.findById(id);
        if(birdtrackinginfo == null)
            return null;
        birdTrackingInfoRepository.save(request);
        return birdTrackingInfoRepository.findById(id);
    }

    @ApiOperation(value = "Delete specific birdTrackingInfo by id", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @DeleteMapping(path = "/birdtrackinginfo/{id}")
    String deleteBirdtrackinginfo(@PathVariable int id){
        birdTrackingInfoRepository.deleteById(id);
        return success;
    }
}
