package com.cs309.tutorial.tests;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class TestController {

	ArrayList<TestData> testArr = new ArrayList<TestData>();


	@GetMapping("/getTest")
	public String getTest(@RequestParam(value = "username", defaultValue = "World") String message) {
		return String.format("Hello, %s! You sent a get request with a parameter!", message);
	}
	
	@PostMapping("/postTest1")
	public String postTest1(@RequestParam(value = "username", defaultValue = "World") String message) {
		return String.format("Hello, %s! You sent a post request with a parameter!", message);
	}
	
	@PostMapping("/postTest2")
	public String postTest2(@RequestBody TestData testData) {
		return String.format("Hello, %s! You sent a post request with a requestbody!", testData.getMessage());
	}

	@PostMapping("/postTest3")
	public String postTest3(@RequestParam(value = "message", defaultValue = "Hi") String message) {
		TestData t = new TestData();
		t.setMessage(message);
		testArr.add(t);
		return "New message: "+ testArr.get(testArr.size()-1).getMessage() + " testArr.size(): " + testArr.size();
	}
	
	@DeleteMapping("/deleteTest")
	public String deleteTest() {
		testArr.remove(testArr.size()-1);
		return " testArr.size(): " + testArr.size();

	}
	
	@PutMapping("/putTest")
	public void putTest(@RequestParam int i, @RequestParam String message){
		testArr.set(i, new TestData(message));
	}

	@GetMapping("/getTest2")
	public String getTest2(@RequestParam int i) {
		return testArr.get(i).getMessage();
	}


}
