package com.madive.bigcommerce.madiveone.admin.api;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.madive.bigcommerce.madiveone.admin.api.domain.TokenRequest;
import com.madive.bigcommerce.madiveone.admin.api.domain.TokenResponse;
import com.madive.bigcommerce.madiveone.admin.config.props.ApiProperties;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@Slf4j
public class ApiServiceTest {

	@Autowired
	private ApiProperties apiProperties;

	@Autowired
	private ApiService apiService;

	@Test
	public void testProperties() {
		log.debug("## Url: {}", apiProperties.getUrl());
		log.debug("## ClientId: {}", apiProperties.getClientId());
		log.debug("## ClientSecret: {}", apiProperties.getClientSecret());
		log.debug("## ConnectTimeoutMillis: {}", apiProperties.getConnectTimeoutMillis());
		log.debug("## ReadTimeoutMillis: {}", apiProperties.getReadTimeoutMillis());
	}

	@Test
	public void testSelect() {
		TokenRequest request = new TokenRequest();
		request.setUsername("admin001");
		request.setPassword("admin001");
		TokenResponse token = apiService.select(Api.AUTH_TOKEN, request, TokenResponse.class);
		log.debug("## token: {}", token);
	}
}