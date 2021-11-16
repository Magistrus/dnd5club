$(document).ready(function() {
	var theme = localStorage.getItem('theme');
	var element = document.getElementById('theme_css');
	if (theme === 'light' || theme === null){
		setThemeLight(element);
	}
	else if (theme === 'dark')
	{
		setThemeDark(element);
	}
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
}

function setThemeDark(element){
	document.getElementById('theme_mode').innerHTML= 'Светлая тема';
	document.getElementById('theme_mode_link').innerHTML= 'Светлая тема';
	element.href = '/resources/css/dark.css';
	element.classList.remove('light')
	element.classList.add('dark');
	saveThemeToSession('dark');
}

function setThemeLight(element){
	document.getElementById('theme_mode').innerHTML= 'Темная тема';
	document.getElementById('theme_mode_link').innerHTML= 'Темная тема';
	element.href = '/resources/css/light.css';
	element.classList.remove('dark')
	element.classList.add('light');
	saveThemeToSession('light');
}

function saveThemeToSession(theme){
	$.ajax({
	    type: 'POST',
	    url: '/session/theme',
	    data: { 
	        'theme': theme, 
	    },
	    success: function(msg){
	    }
	});	
}