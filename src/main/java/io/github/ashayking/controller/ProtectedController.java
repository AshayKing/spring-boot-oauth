package io.github.ashayking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.ashayking.model.Account;
import io.github.ashayking.service.AccountService;

/**
 * 
 * @author Ashay S Patil
 *
 */
@RestController
@PreAuthorize("isAuthenticated()")
public class ProtectedController {

	@Autowired
	private AccountService accountService;

	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	@GetMapping(path = "/api/me", produces = "application/json")
	public Account me() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return accountService.findAccountByUsername(username);
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("/api/admin")
	public ResponseEntity<?> helloAdmin() {
		String msg = String.format("Admin Area");
		return new ResponseEntity<Object>(msg, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping("/api/user")
	public ResponseEntity<?> helloUser() {
		String msg = String.format("User Area");
		return new ResponseEntity<Object>(msg, HttpStatus.OK);
	}

}
