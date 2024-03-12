package Tables.BirdInfo;

import java.util.List;

import Tables.BirdTrackingInfo.BirdTrackingInfo;
import Tables.BirdTrackingInfo.BirdTrackingInfoRepository;
import Tables.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Brian Xicon
 */
@Api(value = "BirdInfoController", description = "REST APIs related to BirdInfo entity")
@RestController
public class BirdInfoController {

    @Autowired
    BirdInfoRepository birdInfoRepository;

    @Autowired
    BirdTrackingInfoRepository birdTrackingInfoRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get all list of birdInfos", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(path = "/birdInfo")
    List<BirdInfo> getAllBirdInfo() {
        return birdInfoRepository.findAll();
    }

    @ApiOperation(value = "Get specific birdInfo by id", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(path = "/birdInfo/{id}")
    BirdInfo getBirdInfoByID(@PathVariable int id) {
        return birdInfoRepository.findById(id);
    }

    @ApiOperation(value = "Create new birdInfo", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping(path = "/birdInfo")
    String createBirdInfo(@RequestBody BirdInfo birdInfo) {
        if (birdInfo == null)
            return failure;
        birdInfoRepository.save(birdInfo);
        return birdInfo.getId() + "";
    }

    @ApiOperation(value = "Update specific birdInfo by id", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping(path = "/birdInfo/{id}")
    BirdInfo updateBirdInfo(@PathVariable int id, @RequestBody BirdInfo request) {
        BirdInfo birdInfo = birdInfoRepository.findById(id);
        if (birdInfo == null)
            return null;
        birdInfo.setScientificName(request.getScientificName());
        birdInfo.setName(request.getName());
        birdInfo.setShortDesc(request.getShortDesc());
        birdInfo.setImage(request.getImage());
        birdInfo.setRangeMap(request.getRangeMap());
        birdInfo.setCallSound(request.getCallSound());
        birdInfoRepository.save(birdInfo);
        return birdInfoRepository.findById(id);
    }

    @ApiOperation(value = "Delete specific birdInfo by id", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @DeleteMapping(path = "/birdInfo/{id}")
    String deleteBirdInfo(@PathVariable int id) {
        birdInfoRepository.deleteById(id);
        return success;
    }


    //Assign A BirdTrackingInfo Point to a Bird
    @ApiOperation(value = "Assign birdTrackingInfo to birdInfo", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping("/birdInfo/{birdInfoId}/birdTrackingInfo/{birdTrackingInfoID}")
    String assignBirdTrackingInfoToBird(@PathVariable int birdInfoId, @PathVariable int birdTrackingInfoID) {
        BirdInfo birdInfo = birdInfoRepository.findById(birdInfoId);
        BirdTrackingInfo birdTrackingInfo = birdTrackingInfoRepository.findById(birdTrackingInfoID);

        if (birdInfo == null || birdTrackingInfo == null)
            return failure;

        birdInfo.addBirdTrackingInfo(birdTrackingInfo);
        birdInfoRepository.save(birdInfo);

        return success;
    }

    //Get A Bird's BirdTrackingInfo
    @ApiOperation(value = "Get specific birdInfo's birdTrackingInfo", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping("/birdInfo/{birdInfoId}/birdTrackingInfo")
    List<BirdTrackingInfo> getBirdInfoBirdTrackingInfo(@PathVariable int birdInfoId) {
        BirdInfo birdInfo = birdInfoRepository.findById(birdInfoId);
        if (birdInfo == null)
            return null;

        return birdInfo.getBirdTrackingInfo();
    }
}