$(document).ready(function() {
	let selected = null;
	if (selectedCondition){
		selected = selectedCondition;
	}
	if (selected){
		var element = $('#'+selected)[0];
		var rightContainer = document.getElementById('container_card');
		rightContainer.classList.add('block_information', selected);
		setActiveCondition(element, selected);
	}
});
$('.card').on('click', 	function() {
	var englishName = this.id.replace(' ', '_');
	var rightContainer = document.getElementById('container_card');
	// проверяем открыта ли правая панель
	if (rightContainer.classList.contains('block_information')) {
		if (localStorage.getItem('selected_condition') === englishName) {
			rightContainer.classList.remove('block_information');
			$(".card").removeClass('active');
			localStorage.removeItem('selected_condition');
			history.pushState('data to be passed', '', '/conditions/');
		} else {
			rightContainer.classList.add('block_information');
			setActiveCondition(this, englishName);
		}
	} else {
		rightContainer.classList.add('block_information');
		setActiveCondition(this, englishName);
	}
});
function setActiveCondition(element, englishName){
	var conditionName = element.querySelector("#condition_id").textContent;
	document.getElementById('condition_name').innerHTML = conditionName;
	history.pushState('data to be passed', conditionName, '/conditions/' + englishName);
	document.title = conditionName;
	localStorage.setItem('selected_condition', englishName);
	$(".card").removeClass('active');
	element.classList.toggle('active');
	var url = '/conditions/fragment/' + englishName;
	$("#content_block").load(url);
}
$('#btn_close').on('click', function() {
	document.getElementById('container_card').classList.toggle('block_information');
	$(".card").removeClass('active');
	localStorage.removeItem('selected_condition');
	history.pushState('data to be passed', 'Классы', '/conditions/');
});