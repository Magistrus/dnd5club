sourceTypes = localStorage.getItem('first_visit');
$(document).ready(function() {
	function checkWidth() {
	  let windowWidth = $('body').innerWidth(),
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
		$('#body').removeClass('full_screen_right_block');
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
	if(localStorage.getItem('homebrew_source') == 'true'){
		$('#homebrew_source').prop('checked', true);
		$('.custom_source').removeClass('hide_block');
	}
	if(localStorage.getItem('setting_source') == 'true'){
		$('#setting_source').prop('checked', true);
		$('.setting_source').removeClass('hide_block');
	}
});
$("#btn_full_screen, #btn_exet_full_screen").click(function () {
	$("#body").toggleClass("full_screen_right_block");
});
$('#homebrew_source').change(function() {
	localStorage.setItem('homebrew_source', $('#homebrew_source').is(':checked'));
	$('.custom_source').toggleClass('hide_block');
});
$('#setting_source').change(function() {
	localStorage.setItem('setting_source', $('#setting_source').is(':checked'));
	$('.setting_source').toggleClass('hide_block');
});