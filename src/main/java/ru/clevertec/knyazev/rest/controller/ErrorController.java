package ru.clevertec.knyazev.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class ErrorController {
	@GetMapping("/errors")
	public ResponseEntity<String> handleError(HttpServletRequest request) throws Exception {
		Integer status = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
		String errorMessage = (String) request.getAttribute("jakarta.servlet.error.message");
		
		if (errorMessage == null || "".equals(errorMessage)) {
			errorMessage = "";
		}
		
		if (status == 400) {
			return new ResponseEntity<String>("Bad Request. " + errorMessage, HttpStatus.BAD_REQUEST);
		}
		
		if (status == 404) {
			return new ResponseEntity<String>("Not Found. " + errorMessage, HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<String>("UNKNOWN_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}