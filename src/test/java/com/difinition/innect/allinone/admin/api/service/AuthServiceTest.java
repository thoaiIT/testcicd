package com.madive.bigcommerce.madiveone.admin.api.service;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.madive.bigcommerce.madiveone.admin.api.domain.TokenRequest;
import com.madive.bigcommerce.madiveone.admin.api.domain.TokenResponse;
import com.madive.bigcommerce.madiveone.admin.api.domain.ApiUser;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@Slf4j
public class AuthServiceTest {

	@Autowired
	private AuthService authService;

	@Test
	public void testGetAccessToken() {
		TokenRequest request = new TokenRequest();
		request.setUsername("admin001");
		request.setPassword("admin001");
		TokenResponse token = authService.getAccessToken(request);
		log.debug("## token: {}", token);
	}

	@Test
	public void testGetUser() {
		String accessToken = "4b19224e-32e9-4b3f-9201-56f234785b79";
		ApiUser user = authService.getUser(accessToken);
		log.debug("## user: {}", user);
	}

	@Test
	public void testGetUserByGuid() {
		ApiUser user = authService.getUserByGuid("e80b0735a4f349a7bf81cb491a17e3de");
		log.debug("## user: {}", user);
	}
}