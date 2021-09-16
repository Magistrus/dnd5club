$(document).ready(function() {
	function checkWidth() {
	  var windowWidth = $('body').innerWidth(),
	  elem = $("#body"); 
	  var check =localStorage.getItem('compact_menu') 
	  if(check === 'true')
	  {
		  elem.addClass('compact_menu');
		  return;
	  }
	  if(windowWidth < 1400){
		elem.addClass('compact_menu');
	  }
	  else {
		elem.removeClass('compact_menu');
	  }
	}
	checkWidth(); // проверит при загрузке страницы
	$(window).resize(function(){
	  checkWidth(); // проверит при изменении размера окна клиента
	});
});