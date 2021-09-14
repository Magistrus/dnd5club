$(document).ready(function() {
	var theme = readCookie('theme');
	var element = document.getElementById('theme_css');
	if (theme === 'light'){
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
		document.cookie = "theme=dark";
		setThemeDark(element);
	} else {
		document.cookie = "theme=light";
		setThemeLight(element);
	}
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

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}