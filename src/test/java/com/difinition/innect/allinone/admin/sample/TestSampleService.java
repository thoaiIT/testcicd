package com.madive.bigcommerce.madiveone.admin.sample;
//package com.definition.innect.allinone.admin.sample;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestMethodOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.definition.innect.allinone.admin.sample.domain.Sample;
//import com.definition.innect.allinone.admin.sample.service.SampleService;
//
//@SpringBootTest
//@TestMethodOrder(OrderAnnotation.class)
//public class TestSampleService {
//	@Autowired
//	private SampleService service;
//
//	@Order(1)
//	@Test
//	public void add() throws Exception{
//		Sample sample = new Sample("test");
//		sample.setName("name");
//		service.add(sample);
//	}
//
//	@Order(2)
//	@Test
//	public void get() throws Exception{
//		Sample sample = new Sample("test");
//		Sample result = service.get(sample);
//		assertEquals("test", result.getId());
//	}
//
//	@Order(3)
//	@Test
//	public void remove() throws Exception{
//		Sample sample = new Sample("test");
//		sample.setName("name");
//		service.remove(sample);
//	}
//}
