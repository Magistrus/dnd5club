cntrlIsPressed = false;
sourceTypes = localStorage.getItem('first_visit');
$(document).ready(function() {
	function checkWidth() {
		let windowWidth = $('body').innerWidth(),
		elem = $("#body"); 
		var check = localStorage.getItem('compact_menu') 
		if(check === 'true' && windowWidth > 1200)
		{
			elem.addClass('compact_menu');
			$('#list_page_two_block').addClass('block_information');
			return;
		}
		if(windowWidth < 1400){
			elem.addClass('compact_menu');
		}
		else {
			elem.removeClass('compact_menu');
		}
		if(windowWidth < 1200){
			elem.removeClass('compact_menu');
			$('#body').removeClass('full_screen_right_block');
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
	              content: jqXHR.responseText
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
		$('#source_id').addClass('active');
	} else {
		localStorage.setItem('homebrew_source', 'false');
	}
	if(localStorage.getItem('setting_source') == 'true'){
		$('#setting_source').prop('checked', true);
		$('.setting_source').removeClass('hide_block');
		$('.module_source').removeClass('hide_block');
		$('#source_id').addClass('active');
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
	const spoilers = document.querySelectorAll('.header')
	for (let spoiler of spoilers) {
	  let div = spoiler.nextElementSibling
	  let divClass = div.classList
  	//   divClass.add('hide')
  	  spoiler.addEventListener('click', function (event) {
		if (divClass.contains("hide") ){
		   divClass.remove('hide')
		} else {
			var targetElement = event.target || event.srcElement;
			if (targetElement.tagName !== 'svg'){
				divClass.add('hide')
			}
		}
	  })
	}
});
$("#btn_full_screen, #btn_exet_full_screen").click(function () {
	$("#body").toggleClass("full_screen_right_block");
});
$('#homebrew_source').change(function() {
	localStorage.setItem('homebrew_source', $('#homebrew_source').is(':checked'));
	$('.custom_source').toggleClass('hide_block');
	$('#source_id').addClass('active');
	if ($('#homebrew_source').is(':checked')) {
		$('#source_id').addClass('active');
		SimpleBar.instances.get(document.querySelector('[data-simplebar]')).recalculate();
	} else if (!$('#setting_source').is(':checked')){
		$('#source_id').removeClass('active');
	}
});
$('#setting_source').change(function() {
	localStorage.setItem('setting_source', $('#setting_source').is(':checked'));
	$('.setting_source').toggleClass('hide_block');
	$('.module_source').toggleClass('hide_block');
	if ($('#setting_source').is(':checked')) {
		$('#source_id').addClass('active');
		SimpleBar.instances.get(document.querySelector('[data-simplebar]')).recalculate();
	} else if (!$('#homebrew_source').is(':checked')){
		$('#source_id').removeClass('active');
	}
});
$('li').click(function () {
	localStorage.setItem('selected_item_menu', this.id);
});
;(function($, window, document, undefined) {
	'use strict';
	var $html = $('html');
	$html.on('click.ui.dropdown', '.js-dropdown', function(e) {
		e.preventDefault();
		if($(this).hasClass('multiselect')){
			$(this).addClass('is-open');
		} else {
			$(this).toggleClass('is-open');	
		}
	});
	$html.on('click.ui.dropdown', '.js-dropdown [data-dropdown-value]', function(e) {
		e.preventDefault();
		var $item = $(this);
		var $dropdown = $item.parents('.js-dropdown');
		if($dropdown.hasClass('multiselect')){
			let $span = $dropdown.find('.js-dropdown__current');
			let $input = $dropdown.find('.js-dropdown__input');
			if(!$span.text().includes($item.text())){
				if($input.val() == ''){
					$span.html('<span class="selected_item">' + $item.text() + '</span>');
					$input.val($item.data('dropdown-value'));
				} else {
					$span.html($span.html() + '<span class="selected_item">' + $item.text() + '</span>');
					$input.val($input.val() + '|' + $item.data('dropdown-value'));
				}
			} else {
				$span.html($span.html().replace('<span class="selected_item">' + $item.text() + '</span>', ''));
				$input.val($input.val().replace('|' + $item.data('dropdown-value'), ''));
				$input.val($input.val().replace($item.data('dropdown-value'), ''));
				if($input.val().trim() == ''){
					$span.text($span.attr('title'));
				}
			}
			$item.toggleClass('selected');
		} else {
			$dropdown.find('.js-dropdown__current').text($item.text());
			$dropdown.find('.js-dropdown__input').val($item.data('dropdown-value'));
			$dropdown.find('li').removeClass('selected');
			$item.addClass('selected');
		}
		$dropdown.find('.js-dropdown__input').change();
	});
	$html.on('click.ui.dropdown', function(e) {
		var $target = $(e.target);
		if (!$target.parents().hasClass('js-dropdown')) {
			$('.js-dropdown').removeClass('is-open');
		}
	});
})(jQuery, window, document);
$(document).keydown(function(event){
	if(event.which=="17")
		cntrlIsPressed = true;
});
$(document).keyup(function(){
	cntrlIsPressed = false;
});

// Копирование ссылки в буфер
function copyToClipboard(text) {
	var inputc = document.body.appendChild(document.createElement("input"));
	inputc.value = window.location.href;
	inputc.focus();
	inputc.select();
	document.execCommand('copy');
	inputc.parentNode.removeChild(inputc);
	// alert("URL Copied.");
}
(function() {
    var cx = '';
    var $gcse = $('#script');
    $gcse.type = 'text/javascript';
    $gcse.async = true;
    $gcse.src = 'https://cse.google.com/cse.js?cx='+ cx;
    var s = document.getElementsByTagName('script')[0];
    if (s.parentNode === Node.ELEMENT_NODE){
    	s.parentNode.insertBefore($gcse, s);
    }
})();
window.onload = function(){
	if($('#gsc-i-id1')){
		$('#gsc-i-id1').attr("placeholder", "Поиск по классам, расам, монстрам и остальным мирам!");
	}
};