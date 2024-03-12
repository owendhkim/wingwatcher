package coms309.birds;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.HashMap;

@RestController
public class BirdsController
{
    HashMap<String, Bird> birdList = new HashMap<>();

    //List Operation
    @GetMapping("/birds")
    public @ResponseBody HashMap<String, Bird> getAllBirds()
    {
        return birdList;
    }

    //Create Operation
    @PostMapping("/birds")
    public @ResponseBody String createBird(@RequestBody Bird bird)
    {
        System.out.println(bird);
        birdList.put(bird.getSpecies(), bird);
        return "New bird " + bird.getSpecies() + " Saved";
    }

    //Read Operation
    @GetMapping("/birds/{species}")
    public @ResponseBody Bird getBird(@PathVariable String species)
    {
        Bird b = birdList.get(species);
        return b;
    }

    //Update Operation
    @PutMapping("/birds/{species}")
    public @ResponseBody Bird updateBird(@PathVariable String species, @RequestBody Bird b)
    {
        birdList.replace(species, b);
        return birdList.get(species);
    }

    //Update Only Description Through Path Variable
    @PutMapping("/birds/updateDesc/{species}/{description}")
    public @ResponseBody Bird updateDesc(@PathVariable String species, @PathVariable String description)
    {
        Bird b = birdList.get(species);
        b.setDescription(description);
        return b;
    }

    //Delete Operation
    @DeleteMapping("/birds/{species}")
    public @ResponseBody HashMap<String, Bird> deleteBird(@PathVariable String species)
    {
        birdList.remove(species);
        return birdList;
    }
}
