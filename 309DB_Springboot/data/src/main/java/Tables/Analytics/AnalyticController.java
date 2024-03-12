package Tables.Analytics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;

@Api(value = "AnalyticController", description = "REST APIs related to BirdInfo entity")
@RestController
public class AnalyticController {

    @Autowired
    AnalyticRepository analyticRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @ApiOperation(value = "Get list of all analytics", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(path = "/analytics")
    List<Analytic> getAllAnalytics(){
        return analyticRepository.findAll();
    }

    @ApiOperation(value = "Get specepfic analytic by id", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @GetMapping(path = "/analytics/{id}")
    Analytic getAnalyticById(@PathVariable int id){
        return analyticRepository.findById(id);
    }

    @ApiOperation(value = "Create new analytic", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PostMapping(path = "/analytics")
    String createAnalytic(@RequestBody Analytic Analytic){
        if (Analytic == null)
            return failure;
        analyticRepository.save(Analytic);
        return success;
    }

    @ApiOperation(value = "Update specific analytic by id", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @PutMapping(path = "/analytics/{id}")
    Analytic updateAnalytic(@PathVariable int id, @RequestBody Analytic request){
        Analytic analytic = analyticRepository.findById(id);
        if(analytic == null)
            return null;
        analyticRepository.save(request);
        return analyticRepository.findById(id);
    }

    @ApiOperation(value = "Delete specific analytic by id", response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success!"),
            @ApiResponse(code = 401, message = "not authorized, credentials does not match"),
            @ApiResponse(code = 403, message = "forbidden, no privilege granted"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @DeleteMapping(path = "/analytic/{id}")
    String deleteAnalytic(@PathVariable int id){
        analyticRepository.deleteById(id);
        return success;
    }
}