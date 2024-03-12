package onetoone.Analytics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Owen Kim
 *
 */

@RestController
public class AnalyticController {

    @Autowired
    AnalyticRepository analyticRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/analytics")
    List<Analytic> getAllAnalytics(){
        return analyticRepository.findAll();
    }

    @GetMapping(path = "/analytics/{id}")
    Analytic getAnalyticById(@PathVariable int id){
        return analyticRepository.findById(id);
    }

    @PostMapping(path = "/analytics")
    String createAnalytic(@RequestBody Analytic Analytic){
        if (Analytic == null)
            return failure;
        analyticRepository.save(Analytic);
        return success;
    }

    @PutMapping(path = "/analytics/{id}")
    Analytic updateAnalytic(@PathVariable int id, @RequestBody Analytic request){
        Analytic analytic = analyticRepository.findById(id);
        if(analytic == null)
            return null;
        analyticRepository.save(request);
        return analyticRepository.findById(id);
    }

    @DeleteMapping(path = "/analytic/{id}")
    String deleteAnalytic(@PathVariable int id){
        analyticRepository.deleteById(id);
        return success;
    }
}