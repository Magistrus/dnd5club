$(document).ready(function() {
	let className = null;
	if (selectedClass){
		className = selectedClass;
	}
	if (selectedArchetype){
		localStorage.setItem('selected_archetype', selectedArchetype);
	}
	if (className){
		var element = $('#'+className)[0];
		var rightContainer = document.getElementById('container_card');
		rightContainer.classList.add('block_information', className);
		setActiveClass(element, className);
		document.getElementById('class_name').innerHTML = className;
	}
});
$('#class_traits').on('click', function() {
	$('.btn_class').removeClass('active');
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
	$('.btn_class').removeClass('active');
	this.classList.add('active');
	loadDescription();
	localStorage.setItem('class_info', 'description');
});
$('#class_images').on('click', function() {
	$('.btn_class').removeClass('active');
	this.classList.add('active');
	loadImages();
	localStorage.setItem('class_info', 'images');
})
function loadDescription(){
	var selectedClass = $('.card.active')[0];
	var selectedArchetype = $('li.sub_menu.active');
	var url;
	if(selectedArchetype.length === 1){
		url = '/classes/'+selectedClass.id+'/archetype/'+selectedArchetype[0].id+'/description';
	}
	else {
		url = '/classes/' + selectedClass.id + '/description';
	}
	loadContent("content_block", url);
}
function loadImages(){
	var selectedClass = $('.card.active')[0];
	var englishName = selectedClass.id.replace(' ', '_');
	var url = '/classes/images/' + englishName;
	loadContent("content_block", url);
}
$('#class_spells').on('click', function() {
	$('.btn_class').removeClass('active');
	this.classList.add('active');
	var selectedClass = $('.card.active')[0];
	localStorage.setItem('class_info', 'spells');
	// тут должна быть загрузка заклинаний класса
});
$('#btn_close').on('click', function() {
	document.getElementById('container_card').classList.toggle('block_information');
	$(".card").removeClass('active');
	localStorage.removeItem('selected_class');
	localStorage.removeItem('selected_archetype');
	history.pushState('data to be passed', 'Классы', '/classes/');
});
$('.card').on('click', 	function() {
	var englishName = this.id.replace(' ', '_');
	var rightContainer = document.getElementById('container_card');
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
	var className = element.querySelector("#classes_id").textContent;
	document.getElementById('class_name').innerHTML = className;
	document.title = className;
	history.pushState('data to be passed', className, '/classes/' + englishName);
	$(".card").removeClass('active');
	element.classList.toggle('active');
	switch (localStorage.getItem('class_info')) {
	case 'description':
		$('.btn_class').removeClass('active');
		$('#class_description')[0].classList.add('active');
		loadDescription();
		break;
	case 'spells':
		$('.btn_class').removeClass('active');
		$('#class_spells')[0].classList.add('active');
		break;
	case 'images':
		$('.btn_class').removeClass('active');
		$('#class_images')[0].classList.add('active');
		loadImages();
		break;
	default:
		$('.btn_class').removeClass('active');
		$('#class_traits')[0].classList.add('active');
		var url = '/classes/fragment/' + englishName;
		$("#content_block").load(url, function() {
			$('#mobile_selector').change(function () {
				setActiveArchetype(element, englishName, $('#mobile_selector').val());
			});
		});
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
					loadContent("content_block", url);
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
		$("#content_block").load(url, function() {
			$('#mobile_selector').change(function () {
				setActiveArchetype(element, className, $('#mobile_selector').val());
			});
		});
	}
	history.pushState('data to be passed', className, '/classes/' + className + '/' + archetypeName);
}
function loadContent(id, url){
	$('#'+id).load(url);
}