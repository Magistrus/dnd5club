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
		setThemeDark(element);
	} else {
		localStorage.setItem('theme', 'light');
		setThemeLight(element);
	}
}
function setThemeDark(element){
	document.getElementById('icon_dark_mod').innerHTML= '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"> <path d="M20 12C20 10.5426 19.6103 9.17618 18.9294 7.99934M20 12C20 13.4574 19.6103 14.8238 18.9294 16.0007M20 12H22M12 20V22M12 20C13.4574 20 14.8238 19.6103 16.0007 18.9294M12 20C10.5426 20 9.17618 19.6103 7.99934 18.9294M4 12C4 13.4574 4.38972 14.8238 5.07064 16.0007M4 12C4 10.5426 4.38972 9.17618 5.07064 7.99933M4 12H2M12 4C13.4574 4 14.8238 4.38972 16.0007 5.07064M12 4C10.5426 4 9.17618 4.38972 7.99934 5.07064M12 4V2M17 3.33974L16.0007 5.07064M7.00001 20.6603L7.99934 18.9294M20.6603 17L18.9294 16.0007M3.33975 7L5.07064 7.99933M20.6603 7L18.9294 7.99934M3.33976 17L5.07064 16.0007M17 20.6603L16.0007 18.9294M7.00001 3.33974L7.99934 5.07064M16.0007 5.07064C17.2147 5.77306 18.2269 6.78534 18.9294 7.99934M18.9294 16.0007C18.2269 17.2147 17.2147 18.2269 16.0007 18.9294M7.99934 18.9294C6.78534 18.2269 5.77306 17.2147 5.07064 16.0007M5.07064 7.99933C5.77306 6.78534 6.78534 5.77305 7.99934 5.07064" stroke="#7B61FF" stroke-linecap="round" stroke-linejoin="round"/></svg>';
	document.getElementById('theme_mode').innerHTML= 'Светлая тема';
	document.getElementById('theme_mode_link').innerHTML= 'Светлая тема';
	element.href = '/resources/css/dark.css';
	element.classList.remove('light')
	element.classList.add('dark');
	saveThemeToSession('dark');
}

function setThemeLight(element){
	document.getElementById('icon_dark_mod').innerHTML= '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M4 11.9609C4 16.4008 7.59921 20 12.0391 20C16.097 20 19.4527 16.9933 20 13.0862C18.8242 14.3709 17.1335 15.1766 15.2547 15.1766C11.7028 15.1766 8.82344 12.2972 8.82344 8.74531C8.82344 6.86649 9.62909 5.17585 10.9138 4C7.00665 4.54727 4 7.90298 4 11.9609Z" fill="#4D4DAA" stroke="#4D4DAA" stroke-linecap="round" stroke-linejoin="round"/></svg>';
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