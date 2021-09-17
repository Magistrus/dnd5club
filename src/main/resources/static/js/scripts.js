$(document).ready(function() {
	function checkWidth() {
	  var windowWidth = $('body').innerWidth(),
	  elem = $("#body"); 
	  var check = localStorage.getItem('compact_menu') 
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

	// $('#title_sub_menu').stickySidebar({
	// 	containerSelector: '.sub_menu_list',
	// 	innerWrapperSelector: '.sidebar__inner',
	// 	topSpacing: 120,
	// 	bottomSpacing: 20
	// });

	$('#sidebar').stickySidebar({
		containerSelector: '#main-content',
        innerWrapperSelector: '.sidebar__inner',
        topSpacing: 20,
        bottomSpacing: 20
	});
});