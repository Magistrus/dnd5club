$(document).ready(function() {
	if (selectedCondition){
		localStorage.setItem('selected_condition', selectedCondition);
	}
	var selected = localStorage.getItem('selected_condition');
	if (selected !== 'undefined'){
		var element = $('#'+selected)[0];
		var rightContainer = document.getElementById('container_card');
		rightContainer.classList.add('block_information', selected);
		setActiveCondition(element, selected);
	}
});
$('.card').on('click', 	function() {
	var englishName = this.id.replace(' ', '_');
	var rightContainer = document.getElementById('container_card');
	var conditionName = this.querySelector("#condition_id").textContent;
	document.getElementById('condition_name').innerHTML = conditionName;
	document.title = className;
	history.pushState('data to be passed', conditionName, '/conditions/' + englishName);
	// проверяем открыта ли правая панель
	if (rightContainer.classList.contains('block_information')) {
		if (rightContainer.classList.contains(englishName)) {
			rightContainer.classList.remove('block_information', englishName);
			$(".card").removeClass('active');
			localStorage.removeItem('selected_condition');
			history.pushState('data to be passed', '', '/conditions/');
		} else {
			rightContainer.classList.add('block_information', englishName);
			setActiveCondition(this, englishName);
		}
	} else {
		rightContainer.classList.add('block_information', englishName);
		setActiveCondition(this, englishName);
	}
});
function setActiveCondition(element, englishName){
	localStorage.setItem('selected_condition', englishName);
	$(".card").removeClass('active');
	element.classList.toggle('active');
	var url = '/conditions/fragment/' + englishName;
	$(".content_block").load(url);
}
$('#btn_close').on('click', function() {
	document.getElementById('container_card').classList.toggle('block_information');
});