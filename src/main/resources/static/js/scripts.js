$(document).ready(function() {
	function checkWidth() {
	  var windowWidth = $('body').innerWidth(),
	  elem = $("#body"); 
	  var check = localStorage.getItem('compact_menu') 
	  if(check === 'true' && windowWidth > 1000)
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
	  if(windowWidth < 1000){
		elem.removeClass('compact_menu');
		$('#list_page_two_block').removeClass('block_information');
	  }
	}
	checkWidth(); // проверит при загрузке страницы
	$(window).resize(function(){
	  checkWidth(); // проверит при изменении размера окна клиента
	});

	Tipped.delegate('.tip', {
		skin: 'dnd5',
	});
	Tipped.delegate('.tip_scroll', {
		skin: 'dnd5',
		afterUpdate: function(content, element) {
			content.classList.add('tooltip_scroll');
		},
		onShow: function(content, element) {
			var simpleBar = new SimpleBar(content);
	    	simpleBar.recalculate();
	    },
	});
    Tipped.delegate('.tip_spell', {
	    ajax: {
	        url: '/spells/id',
	        type: 'get',
	        success: function(data, textStatus, jqXHR) {
	            return {
	              title: ' <em>' + data.level + ' уровень</em> / ' + data.name,
	              content: data.description
	        	};
	        }
	    },
		afterUpdate: function(content, element) {
			content.classList.add('tooltip_scroll');
		},
		onShow: function(content, element) {
			var simpleBar = new SimpleBar(content);
		   	simpleBar.recalculate();
		},
	    skin: 'dnd5',
	});
	$.sidebarMenu($('.sidebar-menu'));
	$('.ajax-popup-link').magnificPopup({
		  type: 'ajax',
		  closeOnBgClick: true,
	});
	if(!localStorage.getItem('first_visit')){
		$('#popup1').addClass('visible');
		localStorage.setItem('first_visit', ' ')
	}
});