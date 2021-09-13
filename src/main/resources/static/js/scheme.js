function switchTheme() {
	var check = document.getElementById('theme_css').classList[0] === 'light';
	var element = document.getElementById('theme_css');
	if (check) {
		element.href = 'resources/css/dark.css';
		element.classList.remove('light')
		element.classList.add('dark');
	} else {
		element.href = 'resources/css/light.css';
		element.classList.remove('dark')
		element.classList.add('light');
	}
}