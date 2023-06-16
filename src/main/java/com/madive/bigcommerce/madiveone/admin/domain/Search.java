package com.madive.bigcommerce.madiveone.admin.domain;

import lombok.Data;

//TODO: 어드민 공통 검색

@Data
public class Search extends Common {
	
	private	int	currentPage = 1;						//목록의 페이지 번호(default : 1)
	
	private int	start = 1;						//목록의 조회 시 첫번 째 레코드 인덱스(default : 1)
	
	private int	cntPerPage = 10;				//목록의 페이시 사이즈(default : 10)
	
	//어드민 검색 구분
	private String fromDate;			//어드민 기간검색 시작일
	
	private String toDate;			//어드민 기간검색 종료일
		
	private String periodType;		//어드민 기간검색 유형(등록일, 수정일, 접속일)
	
	private String criteria;		//어드민 검색 유형 (사용자명, 소속부서, 등록자명, 수정자명)
	
	private String keyword;			//어드민 검색어
	
	public int getStart() {
		return ((currentPage-1) * cntPerPage);
	}
}
