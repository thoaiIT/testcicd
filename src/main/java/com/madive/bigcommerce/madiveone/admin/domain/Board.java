package com.madive.bigcommerce.madiveone.admin.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Board extends Search {

	private String bd10Guid;
	private String bd10Category;
	private char bd10Status;
	private char bd10Type;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate bd10Sdate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate bd10Edate;
	private String bd10Subject;
	private String bd10Body;
	private String bd10RegGuid;
	private LocalDateTime bd10RegDate;
	private String bd10UpdGuid;
	private LocalDateTime bd10UpdDate;
	private String bd10DelYn;
	
	// AttachFile
	private String path;
	private List<Map<String, String>> attachFiles;
	private String[] deleteFiles;
	
	// for Search
	private String category;
	private String[] status;
	private String[] type;
	
}