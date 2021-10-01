$(document).ready(function() {
	if (selectedRace){
		localStorage.setItem('selected_race', selectedRace);
	}
	if (selectedSubrace){
		localStorage.setItem('selected_subrace', selectedSubrace);
	}
	var raceName = localStorage.getItem('selected_race');
	if (raceName !== 'undefined'){
		var element = $('#'+raceName)[0];
		var rightContainer = document.getElementById('container_card');
		rightContainer.classList.add('block_information', raceName);
		setActiveRace(element, raceName);
	}
});
$('#race_traits').on('click', function() {
	$('#race_description')[0].classList.remove('active');
	this.classList.add('active');
	var selectedRace = $('.card.active')[0];
	var selectedSubrace = $('li.sub_menu.active'); 
	localStorage.setItem('race_info', 'traits');
	if(selectedSubrace.length === 1){
		setActiveSubrace(selectedSubrace[0], selectedRace.id, selectedSubrace[0].id);
	}
	else {
		setActiveRace(selectedRace, selectedRace.id);
	}
});
$('#race_description').on('click', function() {
	$('#race_traits')[0].classList.remove('active');
	this.classList.add('active');
	loadDescription();
	localStorage.setItem('race_info', 'description');
});
function loadDescription(){
	var selectedRace = $('.card.active')[0];
	var selectedSubrace = $('li.sub_menu.active'); 
	if(selectedSubrace.length === 1){
		var url = '/races/'+selectedRace.id+'/subrace/'+selectedSubrace[0].id+'/description';
		$(".content_block").load(url);
	}
	else {
		var url = '/races/' + selectedRace.id + '/description';
		$(".content_block").load(url);
	}
}
$('#btn_full_screen').on('click', function() {
	// тут должен быт код по раскрытию на весь экран
});
$('#btn_close').on('click', function() {
	document.getElementById('container_card').classList.toggle('block_information');
	$(".card").removeClass('active');
	localStorage.removeItem('selected_race');
	localStorage.removeItem('selected_subrace');
	history.pushState('data to be passed', 'Расы', '/races/');
});
$('.card').on('click', 	function() {
	var englishName = this.id.replace(' ', '_');
	var rightContainer = document.getElementById('container_card');
	var raceName = this.querySelector("#race_id").textContent;
	document.getElementById('race_name').innerHTML = raceName;
	document.title = raceName;
	history.pushState('data to be passed', raceName, '/races/' + englishName);
	// проверяем открыта ли правая панель
	if (rightContainer.classList.contains('block_information')) {
		if (rightContainer.classList.contains(englishName)) {
			rightContainer.classList.remove('block_information', englishName);
			$(".card").removeClass('active');
			localStorage.removeItem('selected_race');
			localStorage.removeItem('selected_subrace');
			history.pushState('data to be passed', '', '/races/');
		} else {
			rightContainer.raceName = 'block_information ' + englishName;
			localStorage.removeItem('selected_subrace');
			setActiveRace(this, englishName);
		}
	} else {
		rightContainer.classList.add('block_information', englishName);
		localStorage.removeItem('selected_subrace');
		setActiveRace($('#' + englishName)[0], englishName);
	}
});
function setActiveRace(element, englishName) {
	switch (localStorage.getItem('race_info')) {
	case 'description':
		$('#race_description')[0].classList.add('active');
		$('#race_traits')[0].classList.remove('active');
		break;
	default:
		$('#race_traits')[0].classList.add('active');
	}
	$(".card").removeClass('active');
	element.classList.toggle('active');
	if (localStorage.getItem('race_info')==='description'){
		loadDescription();
	}
	else {
		var url = '/races/fragment/' + englishName;
		$(".content_block").load(url);
	}
	localStorage.setItem('selected_race', element.id)
	var url = '/races/' + englishName + '/subraces/list';
	$('#sub_menu').load(url, function() {
		var elements = $('.close_archetypes');
		for (var i = 0; i < elements.length; i++) {
			elements[i].addEventListener('click', function(event) {
				this.parentElement.classList.remove('active');
				var selecedRaceName = $('.card.active')[0];
				var url = '/races/fragment/' + selecedRaceName.id;
				if (localStorage.getItem('race_info')==='description'){
					loadDescription();
				} else {
					$(".content_block").load(url);
					localStorage.setItem('selected_race', selecedRaceName.id) 
				}
			});
		}
		var elements = $('li.sub_menu');
		for (var i = 0; i < elements.length; i++) {
			elements[i].addEventListener('click', function(event) {
				var selecedRaceName = $('.card.active')[0];
				const checkLi = event.target.tagName;
				if (checkLi === 'SPAN' || checkLi === 'LI' || checkLi === 'DIV'){
					setActiveSubrace(this, selecedRaceName.id, this.id);
					localStorage.setItem('selected_subrace',  this.id);
				} 
			});
		}
		var archetepyName = localStorage.getItem('selected_subrace');
		if (archetepyName){
			var selecedSubraceName = $('#'+ archetepyName);
			setActiveSubrace(selecedSubraceName[0], localStorage.getItem('selected_race'), archetepyName);
		}
	});
}
function setActiveSubrace(element, raceName, archetypeName) {
	$('li.sub_menu').removeClass('active');
	element.classList.add('active');
	if (localStorage.getItem('race_info')==='description'){
		loadDescription();
	}else {
		var url = '/races/' + raceName + '/subrace/' + archetypeName;
		$(".content_block").load(url);
	}
	history.pushState('data to be passed', raceName, '/races/' + raceName + '/' + archetypeName);
}