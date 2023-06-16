function movePage(obj,url){
    
    let objPparents = $(obj).parents('li');
    
    $('#left-accordion').children('li.active').removeClass('active');
    $('[data-bs-parent="#left-accordion"]').not(objPparents.parents('ul')).removeClass('show');
    $('[data-bs-parent="#left-accordion"]').children('li.active').not(objPparents).removeClass('active');
    objPparents.addClass('active');
    
    loadXml(url);
}

function loadXml(url){
	let xhr = new XMLHttpRequest();
	let box = document.getElementById('contents');

    xhr.onreadystatechange = function() {
        if (xhr.readyState === xhr.DONE) {
            if (xhr.status === 200 || xhr.status === 201) {
                content = xhr.responseText;
				box.innerHTML = content;
            } else {
                
            }
        }
    };
    xhr.open('GET', url);
    xhr.send();
}

function upStudy(){
	$('#up_slide').addClass('active');
	$('#player').addClass('active');
}

$('body').on('click', '#up_slide', function(){
	if($(this).hasClass('active')){
		$(this).removeClass('active');
		$('#player').removeClass('active');
	}else{
		$(this).addClass('active');
		$('#player').addClass('active');
	}
});