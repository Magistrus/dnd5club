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
			$('#body').removeClass('full_screen_right_block');
		}

		if ($(window).width() < 1000) {
			$('#list_page_two_block').removeClass('block_information');
		} else {
			$('#list_page_two_block').addClass('block_information');
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
	let path = $(location).attr('pathname');
	if (path.startsWith('/classes') || path.startsWith('/races') || path.startsWith('/traits') || path.startsWith('/options') || path.startsWith('/backgrounds')){
		$('#charachter_item_menu').addClass('active');
	} else if (path.startsWith('/spells')){
		$('#spells_item_menu').addClass('active');
	} else if (path.startsWith('/treasures') || path.startsWith('/items/magic')){
		$('#treasury_item_menu').addClass('active');
	} else if (path.startsWith('/weapons') || path.startsWith('/armors') || path.startsWith('/items')){
		$('#items_item_menu').addClass('active');
	} else if (path.startsWith('/bestiary')){
		$('#bestiary_item_menu').addClass('active');
	} else if (path.startsWith('/gods')){
		$('#gods_item_menu').addClass('active');
	} else if (path.startsWith('/screens') || path.startsWith('/conditions') || path.startsWith('/rules')){
		$('#workshop_item_menu').addClass('active');
	} else if (path.startsWith('/tools/trader') || path.startsWith('/tools/encounters') || path.startsWith('/rules') || path.startsWith('/tools/treasury') || path.startsWith('/tools/tavern') || path.startsWith('/tools/wildmagic') || path.startsWith('/tools/madness') || path.startsWith('/books')){
		$('#instruments_item_menu').addClass('active');
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
$('li').click(function () {
	localStorage.setItem('selected_item_menu', this.id);
});