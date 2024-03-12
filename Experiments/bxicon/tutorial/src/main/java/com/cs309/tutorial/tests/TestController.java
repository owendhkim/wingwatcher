package com.cs309.tutorial.tests;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
public class TestController {

	HashMap<String, TestData> testList = new HashMap<>();

	@GetMapping("/getTest")
	public String getTest(@RequestParam(value = "username", defaultValue = "user") String message) {
		TestData test = testList.get(message);

		if(test != null)
			return String.format("Hello, %s! You sent a get request with a parameter!\nSearch Result: ", message) + test.getMessage();
		else
			return String.format("Hello, %s! You sent a get request with a parameter!\n But there is no result with that parameter.", message);
	}
	
	@PostMapping("/postTest1")
	public String postTest1(@RequestParam(value = "username", defaultValue = "user") String message) {
		TestData test = new TestData(message);
		testList.put(test.getMessage(), test);
		return String.format("Hello, %s! You sent a post request with a parameter!\nMessage \"%s\" Saved.", message, message);
	}
	
	@PostMapping("/postTest2")
	public String postTest2(@RequestBody TestData testData) {
		testList.put(testData.getMessage(), testData);
		return String.format("Hello, %s! You sent a post request with a requestbody!\nMessage \"%s\" Saved.", testData.getMessage(), testData.getMessage());
	}
	
	@DeleteMapping("/deleteTest")
	public @ResponseBody String deleteTest() {
		testList.clear();
		return "You removed all messages from list!";
	}
	
	@PutMapping("/putTest/{message}")
	public @ResponseBody String putTest(@PathVariable String message, @RequestBody TestData testData) {
		testList.replace(message, testData);
		return "Updated " + message + ".";
	}
}
