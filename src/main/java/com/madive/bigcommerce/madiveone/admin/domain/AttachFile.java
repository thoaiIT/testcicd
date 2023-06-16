package com.madive.bigcommerce.madiveone.admin.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AttachFile extends Search {

	private String af10Guid;
	private String af10OwnerTable;
	private String af10OwnerGuid;
	private String af10OrgName;
	private String af10SavePath;
	private String af10RegGuid;
	private LocalDateTime af10RegDate;

}