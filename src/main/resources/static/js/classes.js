$('#btn_close').on('click', function() {
	document.getElementById('container_card').classList.toggle('block_information');
});
$('.card').on('click', 	function() {
	var englishName = this.id.replace(' ', '_');
	var rightContainer = document.getElementById('container_card');
	document.getElementById('class_name').innerHTML = this.querySelector("#header_class_name").textContent;
	// проверяем открыта ли правая панель
	if (rightContainer.classList.contains('block_information')) {
		if (rightContainer.classList.contains(englishName)) {
			rightContainer.classList.remove('block_information',englishName);
			$(".card").removeClass('active');
		} else {
			rightContainer.className = 'block_information ' + englishName;
					setActiveClass(this, englishName);
		}
	} else {
		rightContainer.classList.add('block_information', englishName);
		setActiveClass(this, englishName);
	}
});
function setActiveClass(element, englishName) {
	$(".card").removeClass('active');
	element.classList.toggle('active');
	var url = '/classes/fragment/' + englishName;
	$(".content_block").load(url);
	var url = '/classes/' + englishName + '/architypes/list';
	$('#title_sub_menu').load(url, function() {
		var elements = $('li.sub_menu');
		for (var i = 0; i < elements.length; i++) {
			elements[i].addEventListener('click', function(event) {
				var selecedClassName = $('.card.active')[0].id;
				setActiveArchetype(this, selecedClassName, this.id);
			});
		}
	});
}
function setActiveArchetype(element, className, archetypeName) {
	$('li.sub_menu').removeClass('active');
	element.classList.add('active');
	var url = '/classes/' + className + '/architypes/' + archetypeName;
	$(".content_block").load(url);
}