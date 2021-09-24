$(document).ready(function() {
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

	if (selectedClass){
		localStorage.setItem('selected_class', selectedClass);
	}
	if (selectedArchetype){
		localStorage.setItem('selected_archetype', selectedArchetype);
	}
	var className = localStorage.getItem('selected_class');
	if (className !== 'undefined'){
		var element = $('#'+className)[0];
		var rightContainer = document.getElementById('container_card');
		rightContainer.classList.add('block_information', className);
		setActiveClass(element, className);
	}
});
$('#class_traits').on('click', function() {
	$('#class_description')[0].classList.remove('active');
	$('#class_spells')[0].classList.remove('active');
	this.classList.add('active');
	var selectedClass = $('.card.active')[0];
	var selectedArchetype = $('li.sub_menu.active'); 
	localStorage.setItem('class_info', 'traits');
	if(selectedArchetype.length === 1){
		setActiveArchetype(selectedArchetype[0], selectedClass.id, selectedArchetype[0].id);
	}
	else {
		setActiveClass(selectedClass, selectedClass.id);
	}
});
$('#class_description').on('click', function() {
	$('#class_traits')[0].classList.remove('active');
	$('#class_spells')[0].classList.remove('active');
	this.classList.add('active');
	loadDescription();
	localStorage.setItem('class_info', 'description');
});
function loadDescription(){
	var selectedClass = $('.card.active')[0];
	var selectedArchetype = $('li.sub_menu.active'); 
	if(selectedArchetype.length === 1){
		var url = '/classes/'+selectedClass.id+'/archetype/'+selectedArchetype[0].id+'/description';
		$(".content_block").load(url);
	}
	else {
		var url = '/classes/' + selectedClass.id + '/description';
		$(".content_block").load(url);
	}
}
$('#class_spells').on('click', function() {
	$('#class_description')[0].classList.remove('active');
	$('#class_traits')[0].classList.remove('active');
	this.classList.add('active');
	var selectedClass = $('.card.active')[0];
	localStorage.setItem('class_info', 'spells');
	// тут должна быть загрузка заклинаний класса
});
$('#btn_full_screen').on('click', function() {
	// тут должен быт код по раскрытию на весь экран
});
$('#btn_close').on('click', function() {
	document.getElementById('container_card').classList.toggle('block_information');
});
$('.card').on('click', 	function() {
	var englishName = this.id.replace(' ', '_');
	var rightContainer = document.getElementById('container_card');
	var className = this.querySelector("#classes_id").textContent;
	document.getElementById('class_name').innerHTML = className;
	document.title = className;
	history.pushState('data to be passed', className, '/classes/' + englishName);
	// проверяем открыта ли правая панель
	if (rightContainer.classList.contains('block_information')) {
		if (rightContainer.classList.contains(englishName)) {
			rightContainer.classList.remove('block_information', englishName);
			$(".card").removeClass('active');
			localStorage.removeItem('selected_class');
			localStorage.removeItem('selected_archetype');
			history.pushState('data to be passed', '', '/classes/');
		} else {
			rightContainer.className = 'block_information ' + englishName;
			localStorage.removeItem('selected_archetype');
			setActiveClass(this, englishName);
		}
	} else {
		rightContainer.classList.add('block_information', englishName);
		localStorage.removeItem('selected_archetype');
		setActiveClass($('#' + englishName)[0], englishName);
	}
});
function setActiveClass(element, englishName) {
	switch (localStorage.getItem('class_info')) {
	case 'description':
		$('#class_description')[0].classList.add('active');
		$('#class_traits')[0].classList.remove('active');
		break;
	case 'spells':
		$('#class_spells')[0].classList.add('active');
		$('#class_traits')[0].classList.remove('active');
		break;
	default:
		$('#class_traits')[0].classList.add('active');
	}
	$(".card").removeClass('active');
	element.classList.toggle('active');
	if (localStorage.getItem('class_info')==='description'){
		loadDescription();
	}
	else {
		var url = '/classes/fragment/' + englishName;
		$(".content_block").load(url);
	}
	localStorage.setItem('selected_class', element.id)
	var url = '/classes/' + englishName + '/architypes/list';
	$('#sub_menu').load(url, function() {
		var elements = $('.close_archetypes');
		for (var i = 0; i < elements.length; i++) {
			elements[i].addEventListener('click', function(event) {
				this.parentElement.classList.remove('active');
				var selecedClassName = $('.card.active')[0];
				var url = '/classes/fragment/' + selecedClassName.id;
				if (localStorage.getItem('class_info')==='description'){
					loadDescription();
				} else {
					$(".content_block").load(url);
					localStorage.setItem('selected_class', selecedClassName.id) 
				}
			});
		}
		var elements = $('li.sub_menu');
		for (var i = 0; i < elements.length; i++) {
			elements[i].addEventListener('click', function(event) {
				var selecedClassName = $('.card.active')[0];
				const checkLi = event.target.tagName;
				if (checkLi === 'SPAN' || checkLi === 'LI' || checkLi === 'DIV'){
					setActiveArchetype(this, selecedClassName.id, this.id);
					localStorage.setItem('selected_archetype',  this.id);
				} 
			});
		}
		var archetepyName = localStorage.getItem('selected_archetype');
		if (archetepyName){
			var selecedArchetypeName = $('#'+ archetepyName);
			setActiveArchetype(selecedArchetypeName[0], localStorage.getItem('selected_class'), archetepyName);
		}
	});
}
function setActiveArchetype(element, className, archetypeName) {
	$('li.sub_menu').removeClass('active');
	element.classList.add('active');
	if (localStorage.getItem('class_info')==='description'){
		loadDescription();
	}else {
		var url = '/classes/' + className + '/architypes/' + archetypeName;
		$(".content_block").load(url);
	}
	history.pushState('data to be passed', className, '/classes/' + className + '/' + archetypeName);

}