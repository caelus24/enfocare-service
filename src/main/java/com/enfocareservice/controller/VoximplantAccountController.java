package com.enfocareservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enfocareservice.model.VoximplantAccount;
import com.enfocareservice.service.VoximplantAccountService;

@RestController
@RequestMapping("/enfocare/voximplant")
public class VoximplantAccountController {

	@Autowired
	private VoximplantAccountService voximplantAccountService;

	@PostMapping("/create")
	public ResponseEntity<VoximplantAccount> createAccount(@RequestBody VoximplantAccount voximplantAccount) {

		VoximplantAccount savedAccount = voximplantAccountService.createAccount(voximplantAccount);

		return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
	}

	@GetMapping("/{email}")
	public ResponseEntity<VoximplantAccount> getAccountByEmail(@PathVariable String email) {

		VoximplantAccount voxAccount = voximplantAccountService.getAccountByEmail(email);

		return new ResponseEntity<>(voxAccount, HttpStatus.CREATED);

	}

}
