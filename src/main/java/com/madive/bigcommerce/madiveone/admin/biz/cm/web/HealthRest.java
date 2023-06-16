package com.madive.bigcommerce.madiveone.admin.biz.cm.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("health")
public class HealthRest {

	@GetMapping
	public ResponseEntity<String> get() {
		return ResponseEntity.ok("OK");
	}
}