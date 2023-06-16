var global_params = fnGetParams();
var ntWrite = null;
$(document).ready(function(){
	//수정 데이터
	ntWrite = new Vue({
		el: '#ntWrite',
    	components: {
			vuejsDatepicker
		},
		data: {
			//Dummy
			category: [	// TODO 코드명 미정으로 추후 수정
				{ "code": "SY", "text": "System" },
				{ "code": "MK", "text": "Marketing" },
				{ "code": "CM", "text": "Common" }
			],
			from : null,
			to : null,
			selectFiles: [],
			form: {
				bd10Guid:"",
				bd10Category : "",
				bd10Type : "N",
				bd10Status : "O",
				bd10Subject : "",
				bd10Body : "",
				bd10Sdate : "",
				bd10Edate : "",
				path : "bd/nt/",
				deleteFiles : []
			}
		},
		methods: {
			init() {
				this.from = $("#from").datepicker({}).on( "change", function() {
					ntWrite.to.datepicker( "option", "minDate", fnGetDate( this ) );
				}),
				this.to = $( "#to" ).datepicker({}).on( "change", function() {
					ntWrite.from.datepicker( "option", "maxDate", fnGetDate( this ) );
				});
				
				$('#summernote').summernote({
					placeholder: 'Please input body',
					tabsize: 2,
					height: 200,
					toolbar: [
					['style', ['style']],
					['font', ['bold', 'underline', 'clear']],
					['color', ['color']],
					['para', ['ul', 'ol', 'paragraph']],
					['table', ['table']],
					['insert', ['link', 'video']],
					['view', ['fullscreen', 'codeview', 'help']]
					]
				});
				
				// 페이지 상세 작성
				this.getDetail();
			},
			setEndDate(event) {
				this.form.bd10Edate = "";
				$(".endDate").removeAttr("disabled");
				if(event.target.checked){
					this.form.bd10Edate = "9999-12-31";
					$(".endDate").attr("disabled",true);
				}
			},
			getDetail(){
				if(Object.keys(global_params).length > 0) {
					this.$Ajax(apiRest.apiNtData(global_params.guid), null, this.detailCallBack);
				}
			},
			detailCallBack(data) {
				if (data.success) {
					this.form = Object.assign(this.form, data.data);
					$('#summernote').summernote('code', this.form.bd10Body);
					
					if(this.form.attachFiles){
						let files = this.form.attachFiles;
						for(let i=0; i<files.length; i++){
							this.createFileBtn(files[i].fileId, files[i].fileName);
						}
					}
				} else {
					this.$commonAlert(data.message);
				}
			},
			fileSelect(obj) {
				console.log(obj);
				
				let acceptCount = 5;
				if(acceptCount < ($(".fileDiv").find("button").length + obj.target.files.length)){
					this.$commonAlert("You can upload up to 5 attachments.");
					return;
				}
				
				let acceptExt = ["jpg","jpeg","png","svg","xls","xlsx","doc","docx","ppt","pptx","pdf","hwp"];
				
				for(let i=0; i<obj.target.files.length; i++){
					let fileName = obj.target.files[i].name;
					let ext = fileName.substring(fileName.lastIndexOf(".")+1);
					if(!acceptExt.includes(ext.toLowerCase())){
						this.$commonAlert("You have selected a file that is not allowed.");
						return;
					}
				}
				
				for(let i=0; i<obj.target.files.length; i++){
					this.createFileBtn(null, obj.target.files[i].name);
					this.selectFiles.push(obj.target.files[i]);
				}
			},
			createFileBtn(fileId, fileName){
				let $btn = $("<button class='badge rounded-pill bg-primary d-inline-block' />");
				$btn.append(fileName+'<i class="fas fa-times ms-2"></i>');
				if(fileId) $btn.data("fid", fileId);
				$btn.click(function(){
					if($(this).data("fid")){
						ntWrite.form.deleteFiles.push($(this).data("fid"));
					}
					
					$(this).remove();
				});
				
				$(".fileDiv").append($btn);
			},
			save() {
				if(!this.validation()){
					return;
				}
				
				this.$commonConfirm("Do you want to register?",function(){
					// TODO 로딩 이미지 띄우기
					ntWrite.fileUpload();
				});
			},
			validation(){
				let content = $('#summernote').summernote('code');
				this.form.bd10Body = (content == '<p><br></p>') ? "" : content;
				
				if(this.form.bd10Category.trim() == ""){
					this.$commonAlert("Please select a category");
					return false;
				}
				
				if(this.form.bd10Subject.trim() == ""){
					this.$commonAlert("Please input subject", function(){
						$("#bd10subject").focus();
					});
					return false;
				}
				
				if(this.form.bd10Body == ""){
					this.$commonAlert("Plese input body", function(){
						$('#summernote').summernote('focus');
					});
					return false;
				}
				
				if(this.form.bd10Sdate == "" || this.form.bd10Edate == ""){
					this.$commonAlert("Please check Publishing period");
					return false;
				}
				
				return true;
			},
			fileUpload(){
				const formData = new FormData();
				
				if(this.selectFiles.length > 0){
					for(let i=0; i<this.selectFiles.length; i++){
						formData.append('files', this.selectFiles[i]);
					}
					
					formData.append('path', ntWrite.form.path);
	
					try {
						$.ajax({
							method: "POST",
					    	url: "/cm/upload",
					    	processData: false,
					    	contentType: false,
							data: formData,
							success: function(data, status, xhr) {
								if(data.success) {
									console.log("filelist : ", data.LIST);
									ntWrite.form.attachFiles = data.LIST;
									
									let params = Object.assign({}, ntWrite.form);
									fnItemFiltering(params);
									
									console.log("form : ", ntWrite.form);
									ntWrite.$Ajax(apiRest.apiNtWrite, params, ntWrite.procCallBack);
								} else {
									ntWrite.$commonAlert(data.message);
								}
							},
							error: function(xhr, status, errorThrown) {
								ntWrite.$commonAlert("Error.");
							}
						});
						
//						axios.post('/cm/upload', formData, {
//							headers: {
//								'Content-Type': 'multipart/form-data'
//							}
//						}).then(function(res){
//							let data = res.data;
//							if(data.success) {
//								console.log("filelist : ", data.LIST);
//								ntWrite.form.attachFiles = data.LIST;
//								
//								let params = Object.assign({}, ntWrite.form);
//								fnItemFiltering(params);
//								
//								console.log("form : ", ntWrite.form);
//								ntWrite.$Ajax(apiRest.apiNtWrite, params, ntWrite.procCallBack);
//							} else {
//								ntWrite.$commonAlert(data.message);
//							}
//						}).catch(function(res){
//							ntWrite.$commonAlert("Error.");
//						});
					} catch(err) {
						console.log(err);
					}
				}else{
					let params = Object.assign({}, ntWrite.form);
					fnItemFiltering(params);
					
					ntWrite.$Ajax(apiRest.apiNtWrite, params, ntWrite.procCallBack);
				}
			},
			procCallBack(data) {
				if (data.success) {
					this.$commonAlert("Registration successful.", function(){ ntWrite.goList() });
				} else {
					this.$commonAlert(data.message);
				}
			},
			goDelete(id) {
				this.$commonConfirm("Are you sure you want to delete it?",function(){
					ntWrite.$commonAxios(apiRest.apiNtDelete(id), null, ntWrite.procCallBack);
				});
			},
			goList(){
				sessionStorage.setItem('from','ntWrite');
				location.href='/bd/list';
			}
		},
		created() {
		},
		mounted: function (e) {
			this.init();
		}
	});
});