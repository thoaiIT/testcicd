package com.madive.bigcommerce.madiveone.admin.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

	private String ad10Guid;
	private String ad10LoginId;
	private String ad10Pwd;
	private String ad10Name;
	private LocalDateTime ad10RegDate;
	
	private String ad10PwdIn;
}