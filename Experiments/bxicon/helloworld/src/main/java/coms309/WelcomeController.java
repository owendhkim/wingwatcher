package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    //Changed the welcome text.
    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309- Brian Xicon";
    }

    //Updated path. (Got annoyed with it constantly thinking my path was a name)
    @GetMapping("/pathName/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello and welcome to COMS 309: " + name;
    }

    //Added new path.
    @GetMapping("/test")
    public String test() {
        return "This is a test page.";
    }

    //Numbers in path instead of String.
    @GetMapping("/pathNum/{num}")
    public String num_test(@PathVariable int num) {
        int times2 = num * 2;
        return "Number is: " + num + "\nNumber multiplied by 2 is: " + times2;
    }

    //Trying out RequestParam
    @GetMapping("/pathRequest")
    public String request_test(@RequestParam int age){
        return "Your age is: " + age;
    }
}
