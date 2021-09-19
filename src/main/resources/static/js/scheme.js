$(document).ready(function() {
	var theme = localStorage.getItem('theme');
	var element = document.getElementById('theme_css');
	if (theme === 'light'){
		setThemeLight(element);
	}
	else if (theme === 'dark')
	{
		setThemeDark(element);
	}
	saveThemeToSession(element);
});

function switchTheme() {
	var element = document.getElementById('theme_css');
	var check = element.classList[0] === 'light';
	if (check) {
		localStorage.setItem('theme', 'dark');
		Tipped.setDefaultSkin('dark');
		setThemeDark(element);
	} else {
		localStorage.setItem('theme', 'light');
		Tipped.setDefaultSkin('light');
		setThemeLight(element);
	}
	saveThemeToSession(element);
}

function setThemeDark(element){
	element.href = 'resources/css/dark.css';
	element.classList.remove('light')
	element.classList.add('dark');
}

function setThemeLight(element){
	element.href = 'resources/css/light.css';
	element.classList.remove('dark')
	element.classList.add('light');
}

function saveThemeToSession(element){
	$.ajax({
	    type: 'POST',
	    url: '/session/theme',
	    data: { 
	        'theme': element.classList[0], 
	    },
	    success: function(msg){
	    }
	});	
}