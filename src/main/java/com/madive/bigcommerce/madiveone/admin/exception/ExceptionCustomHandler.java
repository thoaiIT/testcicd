package com.madive.bigcommerce.madiveone.admin.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.madive.bigcommerce.madiveone.admin.domain.Result;

@ControllerAdvice
public class ExceptionCustomHandler {
	@SuppressWarnings("unused")
	private static Logger L = LoggerFactory.getLogger(ExceptionCustomHandler.class);

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> runtime(RuntimeException e) {
		e.printStackTrace();
		L.error("## RuntimeException = {}", e.getMessage());
		return ResponseEntity.ok(Result.FAILURE(e.getMessage()));
	}
	
	@ExceptionHandler(AdminCustomException.class)
	public ResponseEntity<?> admin(AdminCustomException e) {
		e.printStackTrace();
		L.error("## AdminCustomException = {}", e.getMessage());
		return ResponseEntity.ok(Result.FAILURE(e.getMessage()));
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ModelAndView exception500(Exception e) {
		e.printStackTrace();
		L.error("exception500 : " + e);

		ModelAndView mv = new ModelAndView("500");
		// TODO ... use constants class or use exception class instance
		// TODO ... json 일 경우 처리 header.contextType 을 조회하여 으로 처리 가능함.
		mv.addObject(new Exception(e.toString()));
		return mv;
	}
}