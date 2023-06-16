package com.madive.bigcommerce.madiveone.admin.api.service;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@Slf4j
class FileServiceTest {

	@Autowired
	private FileService fileService;

	@Test
	public void testGetUrl() {
		String path = fileService.getUrl();
		log.debug("## path: {}", path);
	}
}