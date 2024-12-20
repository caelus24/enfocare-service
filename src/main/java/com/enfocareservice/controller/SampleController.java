package com.enfocareservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enfocare")
public class SampleController {

	@GetMapping
	public ResponseEntity<String> sayHello() {

		return ResponseEntity.ok("FUCK YOU");

	}

}
