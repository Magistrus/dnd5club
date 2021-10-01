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
	  }
	}
	checkWidth(); // проверит при загрузке страницы
	$(window).resize(function(){
	  checkWidth(); // проверит при изменении размера окна клиента
	});

	Tipped.delegate('.tip', {
		skin: localStorage.getItem('theme'),
	});
	Tipped.delegate('.tip_scroll', {
		skin: localStorage.getItem('theme'),
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
	        url: '/spells',
	        type: 'post',
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
	    skin: localStorage.getItem('theme'),
	});
	$.sidebarMenu($('.sidebar-menu'));
});