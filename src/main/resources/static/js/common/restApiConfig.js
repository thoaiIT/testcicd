/**
 * @description api 관련 내용을 정의
 */

/* API 정보를 작성 더미 호출 후 개발완료 시 전환 */
const apiRest = {
	/* -- START 공지 관리 -- */
	apiNtList : {type:"GET", url:contextPath + "/bd/nt/01", test:null},

	apiNtData : function(id){
		return {type:"GET", url:contextPath + "/bd/nt/02/"+id, test:null};
	},
	// 공지 등록
	apiNtWrite : {type:"POST", url:contextPath + "/bd/nt/03", test:null},
	// 공지 삭제 
	apiNtDelete : function(id){
		return {type:"DELETE", url:contextPath + "/bd/nt/04/"+id, test:null};
	},
	/* -- END 마케팅 > 공지 관리 -- */

	_: {}
};

