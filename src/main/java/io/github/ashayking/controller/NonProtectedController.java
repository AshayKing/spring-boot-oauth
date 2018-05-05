package io.github.ashayking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * @author Ashay S Patil
 *
 */
@RestController
public class NonProtectedController {

	@RequestMapping("/")
	public String home() {
		return "Hello World";
	}
}
