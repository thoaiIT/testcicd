
/**
 * @description Vue EventBus 통합
 */
Vue.prototype.$EventBus = new Vue();

/**
 * @description Vue Axios 통합 함수
 * @param obj(type, url), param, callback
 * @return callback
 */
Vue.prototype.$commonAxios = function(obj, param, callback) {
	
	
	axios.defaults.paramsSerializer = function(paramObj) {
    	const params = new URLSearchParams()
	    for (const key in paramObj) {
	        params.append(key, paramObj[key])
	    }
	
	    return params.toString()
	}
	
	
	
    if (!obj) {
		//TODO: error
    } else {
        if (obj.type !== 'GET') {
			//param = $.extend({}, {TODO: id: ~~~~~}, param);
        }
        let _param = param,
            _type = obj.type,
            _url = obj.url;
		
		if (obj.test) {
			_type = 'GET';
			_url = location.origin + contextPath + '/dummy/data/' + obj.test;
		}

		let opts = {
			method: _type,
			url: _url,
			headers: {
				"Content-Type" : "application/json"
			}
		};

		if (obj.type.toUpperCase() === 'GET')
			opts.params = _param;
		else
			opts.data = _param;

        axios(opts).then(response => {
			if(response.data.message == "LOGIN_FAILURE"){
				let option = {
			        title: 'Warning',
					subtitle: "Login Session Expired.",
			        onOk: function() {
						location.href = "/";
					}
			    };
				mscAlert(option);
				return;
			}
				
			
			if (callback) {
				callback(response.data);
			}
        }).catch(error => {
            console.log("error::", error.status);
            let option = {
		        title: 'Alert',
				subtitle: error.message,
		        onOk: null
		    };
			mscAlert(option);
        });
    }
};

/**
 * @description 페이지 로딩 함수
 */
Vue.prototype.$loading = function(type) {
	console.log("TODO: loading on, off");
};

/**
 *  @description alert 함수
 *  @param text - 알림문구 string
 *  @param callback
 */
Vue.prototype.$commonAlert = function(text, callback){ // title 알림으로 된 alert 창이 뜬다.
	//TODO: alert
	let option = {
        title: 'Warning',
		subtitle: text,
        onOk: callback
    };
	mscAlert(option);
};

/**
 * @description confirm 함수
 * @return callback
 */
Vue.prototype.$commonConfirm = function(text, callback){
    //TODO: confirm
	//mscConfirm({title: 'alert', subtitle: '내용입니다',});
	let option = {
        title: 'Confirm',
		subtitle: text,
        onOk: callback
    };
	mscConfirm(option);
};

Vue.prototype.$toDay = function(fmt) {
	return moment().format(fmt ? fmt : 'YYYY-MM-DD')
};

Vue.prototype.$formatDate = function(val, fmt) {
	return moment(val).format(fmt ? fmt : 'YYYY-MM-DD')
};

Vue.prototype.$onlyNumber = function($event) {
	let keyCode = ($event.keyCode ? $event.keyCode : $event.which);
	if ((keyCode < 48 || keyCode > 57) && keyCode !== 46) { // 46 is dot
		$event.preventDefault();
	}
};

/**
 * @description css selector (jquery $(selector) or $.find(selector))
 */
Vue.prototype.$find = function(selector) {
	const el = document.querySelectorAll(selector);
	return el.length === 1 ? el[0] : el;
};

Vue.prototype.$Ajax = function(opt, params, callback){
	$.ajax({
		dataType : "json",
    	contentType: "application/json; charset=utf-8",
		method: opt.type,
		url: opt.url,
		data: opt.type == 'POST' ? JSON.stringify(params) : params,
		success: function(data, status, xhr) {
			if(console) console.log('data => ', data);
			if(console) console.log('status => ', status);
			if(console) console.log('xhr => ', xhr);
			if(data.message == 'LOGIN_FAILURE'){
				let option = {
			        title: 'Warning',
					subtitle: "Login Session Expired.",
			        onOk: function() {
						location.href = "/";
					}
			    };
				mscAlert(option);
				return;
			}
			
			if(callback){
				callback.call(this, data, status, xhr);
			}
		},
		error: function(xhr, status, errorThrown) {
			if(console) console.log('xhr => ', xhr);
			if(console) console.log('status => ', status);
			if(console) console.log('errorThrown => ', errorThrown);
		},
		statusCode: {
			404: function() {
				let option = {
			        title: 'Warning',
					subtitle: "page not found",
			        onOk: function() {
						location.href = "/";
					}
			    };
				mscAlert(option);
			},
			405: function() {
				let option = {
			        title: 'Warning',
					subtitle: "bad request",
			        onOk: function() {
						location.href = "/";
					}
			    };
				mscAlert(option);
			},
			500: function() {
				let option = {
			        title: 'Warning',
					subtitle: "server error",
			        onOk: function() {
						location.href = "/";
					}
			    };
				mscAlert(option);
			}
		}
	});
}

/**
 * Vue filter for phone number format
 */
Vue.filter('phoneNumber', function(v) {
	return v.replace(/[^0-9]/g, '').replace(/(\d{3})(\d{3,4})(\d{4})/, '$1-$2-$3');
});

//pagingMixin 
const pagingMixin = {
	data() {
		return {
			parameterMap : {},
			listMehtodName : 'default',
			pagination:{
				currentPage: 1,
				cntPerPage: 10,
				pageSize: 10,
				totalRecordCount:0
			},
			list: []
		}	
	},
	created() {},
	methods: {
		setRowNum: function(idx) {
			return this.pagination.totalRecordCount - ((this.pagination.currentPage - 1) * this.pagination.cntPerPage) - idx;
		},
		goPage: function(pageNo) {
			this.pagination.currentPage = pageNo;
			console.log("this.pagination::", this.pagination);
			//TODO: 검색 데이터
			if (this.listMehtodName === 'default') {
				this.getList();
			} else {
				this[this.listMehtodName]();
			}
		}, setPagination: function(totalRecordCount) {
			$pInfo = this.pagination;
			$pInfo.totalRecordCount = totalRecordCount;
			$pInfo.totalPageCount = Math.floor((totalRecordCount - 1) / $pInfo.cntPerPage) + 1;
			if ($pInfo.currentPage > $pInfo.totalPageCount) {
				$pInfo.currentPage = $pInfo.totalPageCount;
			}
			$pInfo.firstPage = (($pInfo.currentPage - 1) / $pInfo.pageSize) * $pInfo.pageSize + 1;
			$pInfo.lastPage = $pInfo.firstPage + $pInfo.pageSize - 1;
			if ($pInfo.lastPage > $pInfo.totalPageCount) {
				$pInfo.lastPage = $pInfo.totalPageCount;
			}
			console.log("$pInfo::", $pInfo);
		}
	}
}

function fnGetParams(){
	let params = {};
	if(location.toString().indexOf('?') == -1){
		return params;
	}
	
	let queryString = location.toString().substring(location.toString().indexOf('?')+1);
	let arrParam = queryString.split('&');
	$.each(arrParam,function(){
		let obj = this.split('=');
		params[obj[0]] = obj[1];
	});
	
	return params;
}

function uiInit(){
	// 전체 옵션이 있는 체크박스 그룹 컨트롤
	// 사용법은 AD_OG_02.html 참조 (created by 김형진)
	$('input[type=checkbox].checkAll').off('click').on('click',function(){
		let me = this;
		$.each(me.classList, function(){
			if(this.indexOf('has-') > -1){
				let group = this.split('-');
				//$('input[type=checkbox].'+group[1]).prop('checked',me.checked);
				if(me.checked){
					$('input[type=checkbox].'+group[1]).prop('checked',false);
				}else{
					$('input[type=checkbox].'+group[1]).prop('checked',true);
				}
				$('input[type=checkbox].'+group[1]).click();
			}
		});
	});
	
	$('input[type=checkbox]').off('change').on('change',function(){
		let me = this, isAllCheck = true;
		$.each(me.classList, function(){
			let cls = this.toString();
			let allOption = $('input[type=checkbox].checkAll.has-'+cls);
			if(allOption.length > 0){
				$.each($('input[type=checkbox].'+cls), function(){
					if(!this.checked){
						isAllCheck = false;
						return false;
					}
				});
				
				allOption.prop('checked', isAllCheck);
			}
		});
	});
	
	if($('input[type=checkbox].checkAll').length > 0){
		$.each($('input[type=checkbox].checkAll'), function(){
			let me = this, isAllCheck = true;
			$.each(me.classList, function(){
				if(this.indexOf('has-') > -1){
					let group = this.split('-');
					$.each($('input[type=checkbox].'+group[1]), function(){
						if(!this.checked){
							isAllCheck = false;
							return false;
						}
					});
					
					me.checked = isAllCheck;
				}
			});
		});
	}
	// 전체 옵션이 있는 체크박스 그룹 컨트롤 
}

function fnGetDate( element ) {
	var date;
	try {
		date = $.datepicker.parseDate( "yy-mm-dd", element.value );
	} catch( error ) {
		date = null;
	}
	return date;
}

// 연속된 문자 체크
function fnChkConsChar(str, limit) {
    var o, d, p, n = 0, l = limit == null ? 3 : limit;
    for (var i = 0; i < str.length; i++) {
        var c = str.charCodeAt(i);
        if (i > 0 && (p = o - c) > -2 && p < 2 && (n = p == d ? n + 1 : 0) > l - 3) 
            return false;
        d = p, o = c;
    }
    return true;
}

$.datepicker.setDefaults({
    dateFormat: 'yy-mm-dd',
    prevText: '이전 달',
    nextText: '다음 달',
    monthNames: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
    monthNamesShort: ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'],
    dayNames: ['일', '월', '화', '수', '목', '금', '토'],
    dayNamesShort: ['일', '월', '화', '수', '목', '금', '토'],
    dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'],
    showMonthAfterYear: true,
    yearSuffix: '년'
});

// 특수문자 체크
function fnCheckSpecial(str) {
	const regExp = /[!?@#$%^&*():;+-=~{}<>\_\[\]\|\\\"\'\,\.\/\`\₩]/g;
	if(regExp.test(str)) {
		return true;
	}else{
		return false;
	}
}

/**
 * Json Object에서 데이터가 없는 key 제거 및 Date 객체 포맷변경
 */
function fnItemFiltering(jsonObj){
	var keys = Object.keys(jsonObj);
	
	for(var i=0; i<keys.length; i++){
		if(typeof(jsonObj[keys[i]]) == 'undefined'){
			delete jsonObj[keys[i]];
			continue;
		}
		
		if(jsonObj[keys[i]] === null || jsonObj[keys[i]] === ''){
			delete jsonObj[keys[i]];
			continue;
		}
		
		if(jsonObj[keys[i]].length == 0){
			delete jsonObj[keys[i]];
			continue;
		}
		
		if(jsonObj[keys[i]] instanceof Date){
			jsonObj[keys[i]].setDate(jsonObj[keys[i]].getDate()+1);
			jsonObj[keys[i]] = jsonObj[keys[i]].toISOString().substring(0,10);
		}
	}
}
