package com.madive.bigcommerce.madiveone.admin.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@Slf4j
public class DataSourceTest {

	@Autowired
	DataSource dataSource;

	@Test
	public void testDataSource() throws SQLException {
		Connection connection = dataSource.getConnection();
		log.debug("@@ URL: {}", connection.getMetaData().getURL());
		log.debug("@@ UserName: {}", connection.getMetaData().getUserName());
	}
}