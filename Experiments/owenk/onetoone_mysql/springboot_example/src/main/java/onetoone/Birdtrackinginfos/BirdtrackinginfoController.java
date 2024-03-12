package onetoone.Birdtrackinginfos;

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
public class BirdtrackinginfoController {

    @Autowired
    BirdtrackinginfoRepository birdtrackinginfoRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/birdtrackinginfos")
    List<Birdtrackinginfo> getAllBirdtrackinginfos(){
        return birdtrackinginfoRepository.findAll();
    }

    @GetMapping(path = "/birdtrackinginfos/{id}")
    Birdtrackinginfo getBirdtrackinginfoById(@PathVariable int id){
        return birdtrackinginfoRepository.findById(id);
    }

    @PostMapping(path = "/birdtrackinginfos")
    String createBirdtrackinginfo(@RequestBody Birdtrackinginfo Birdtrackinginfo){
        if (Birdtrackinginfo == null)
            return failure;
        birdtrackinginfoRepository.save(Birdtrackinginfo);
        return success;
    }

    @PutMapping(path = "/birdtrackinginfos/{id}")
    Birdtrackinginfo updateBirdtrackinginfo(@PathVariable int id, @RequestBody Birdtrackinginfo request){
        Birdtrackinginfo birdtrackinginfo = birdtrackinginfoRepository.findById(id);
        if(birdtrackinginfo == null)
            return null;
        birdtrackinginfoRepository.save(request);
        return birdtrackinginfoRepository.findById(id);
    }

    @DeleteMapping(path = "/birdtrackinginfo/{id}")
    String deleteBirdtrackinginfo(@PathVariable int id){
        birdtrackinginfoRepository.deleteById(id);
        return success;
    }
}
