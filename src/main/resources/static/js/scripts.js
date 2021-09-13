$(document).ready(function() {
	function checkWidth() {
	  var windowWidth = $('body').innerWidth(),
		  elem = $("#body"); // лучше сохранять объект в переменную, многократно чтобы не насиловать 
									  // страницу для поиска нужного элемента
	  if(windowWidth < 1400){
		elem.removeClass('compact_menu');
		elem.addClass('compact_menu');
	  }
	  else{
		elem.removeClass('compact_menu');
		elem.addClass('compact_menu');
	  }
	}
  
	checkWidth(); // проверит при загрузке страницы
  
	$(window).resize(function(){
	  checkWidth(); // проверит при изменении размера окна клиента
	});
  });