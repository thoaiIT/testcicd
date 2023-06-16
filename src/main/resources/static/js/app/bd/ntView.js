var global_params = fnGetParams();
$(document).ready(function(){
	//수정 데이터
	ntView = new Vue({
		el: '#ntView',
		data: {
			//Dummy
			tCate: [	// TODO 코드명 미정으로 추후 수정
				{ "code": "SY", "text": "System" },
				{ "code": "MK", "text": "Marketing" },
				{ "code": "CM", "text": "Common" }
			],
			tStat: [ 
				{ "id": "O", "text": "Open" },
				{ "id": "R", "text": "Ready" }, 
				{ "id": "C", "text": "Close" }
			],
			tType: [ 
				{ "id": "I", "text": "Important" },
				{ "id": "N", "text": "Normal" }
			],
			form: {}
		},
		methods: {
			init() {
				// 페이지 상세 작성
				this.getDetail();
			},
			getDetail(){
				if(Object.keys(global_params).length > 0) {
					this.$Ajax(apiRest.apiNtData(global_params.guid), null, this.detailCallBack);
				}
			},
			detailCallBack(data) {
				if (data.success) {
					this.form = data.data;
					
					this.form.category	= this.tCate.filter(item => item.code === data.data.bd10Category)[0].text;
					this.form.status	= this.tStat.filter(item => item.id === data.data.bd10Status)[0].text;
					this.form.type		= this.tType.filter(item => item.id === data.data.bd10Type)[0].text;
					this.form.regDate	= moment(data.data.bd10RegDate).format("YYYY-MM-DD hh:mm:ss");
					this.form.updDate	= moment(data.data.bd10UpdDate).format("YYYY-MM-DD hh:mm:ss");
					
					// 첨부파일 셋팅
					if(this.form.attachFiles){
						let files = this.form.attachFiles;
						for(let i=0; i<files.length; i++){
							this.createFileBtn(files[i].fileId, files[i].fileName);
						}
					}
				} else {
					this.$commonAlert(data.rtnMsg);
				}
			},
			createFileBtn(fileId, fileName){
				let $btn = $("<button class='badge rounded-pill bg-primary d-inline-block' />");
				$btn.append(fileName+'<i class="fas fa-download ms-2"></i>');
				if(fileId) $btn.data("fid", fileId);
				$btn.click(function(){
					if($(this).data("fid")){
						ntView.filedown($(this).data("fid"), $(this).text());
					}
				});
				
				$("div.badge-group").append($btn);
			},
			procCallBack(data) {
				if (data.success) {
					this.$commonAlert("Delete successful.", function(){ ntView.goList() });
				} else {
					this.$commonAlert(data.rtnMsg);
				}
			},
			goEdit() {
				location.href = '/bd/write?guid='+global_params.guid;
			},
			goDelete() {
				this.$commonConfirm("Are you sure you want to delete it?",function(){
					ntView.$commonAxios(apiRest.apiNtDelete(global_params.guid), null, ntView.procCallBack);
				});
			},
			goList(){
				sessionStorage.setItem('from','ntView');
				location.href='/bd/list';
			},
			filedown(fileId, fileName) {
				// TODO
				let $form = $("<form/>")
					.attr("method","POST")
					.attr("target","_blank")
					.attr("action","/cm/filedown");
				
				$form.append("<input type=hidden name=fileId value='"+fileId+"'>");
				
				$('body').append($form);
				$form[0].submit();
				$form.remove();
			}
		},
		created() {
		},
		mounted() {
			this.init();
		}
	});
});

var ntView = null;