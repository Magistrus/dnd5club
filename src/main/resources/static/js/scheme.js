const SITE_THEME = {
    dark: {
        name: 'dark',
        icon: '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M20 12H22M12 20V22M4 12H2M12 4V2M17 3.33974L16.0007 5.07064M7.00001 20.6603L7.99934 18.9294M20.6603 17L18.9294 16.0007M3.33975 7L5.07064 7.99933M20.6603 7L18.9294 7.99934M3.33976 17L5.07064 16.0007M17 20.6603L16.0007 18.9294M7.00001 3.33974L7.99934 5.07064" stroke="#4D4DAA" stroke-linecap="round" stroke-linejoin="round"/><path d="M14.5004 7.66915C13.7649 7.24358 12.9109 7 12 7C11.0891 7 10.2351 7.24358 9.49959 7.66915C8.74084 8.10816 8.10816 8.74084 7.66915 9.49958C7.24358 10.2351 7 11.0891 7 12C7 12.9109 7.24358 13.7649 7.66915 14.5004C8.10816 15.2592 8.74084 15.8918 9.49959 16.3308C10.2351 16.7564 11.0891 17 12 17C12.9109 17 13.7649 16.7564 14.5004 16.3308C15.2592 15.8918 15.8918 15.2592 16.3308 14.5004C16.7564 13.7649 17 12.9109 17 12C17 11.0891 16.7564 10.2351 16.3308 9.49959C15.8918 8.74084 15.2592 8.10816 14.5004 7.66915Z" stroke="#4D4DAA" stroke-linecap="round" stroke-linejoin="round"/></svg>',
        label: 'Светлая тема'
    },
    light: {
        name: 'light',
        icon: '<svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M4 11.9609C4 16.4008 7.59921 20 12.0391 20C16.097 20 19.4527 16.9933 20 13.0862C18.8242 14.3709 17.1335 15.1766 15.2547 15.1766C11.7028 15.1766 8.82344 12.2972 8.82344 8.74531C8.82344 6.86649 9.62909 5.17585 10.9138 4C7.00665 4.54727 4 7.90298 4 11.9609Z" fill="#4D4DAA" stroke="#4D4DAA" stroke-linecap="round" stroke-linejoin="round"/></svg>',
        label: 'Темная тема'
    },
};

$(document).ready(function () {
    const theme = localStorage.getItem('theme');

    setTheme(theme);
});

function switchTheme() {
    const storageTheme = localStorage.getItem('theme');

    if (!storageTheme) {
        setTheme();

        return;
    }

    setTheme(
        storageTheme === SITE_THEME.dark.name
            ? SITE_THEME.light.name
            : SITE_THEME.dark.name
    )
}

function setTheme(name = 'dark') {
    const styleEl = document.getElementById('theme_css');
    const styleHref = styleEl.getAttribute('href');
    const iconEl = document.getElementById('icon_dark_mod');
    const submenuEl = document.getElementById('theme_mode_link');
    const labelEl = document.getElementById('theme_mode');
    const hasTheme = !name ? false : Object.keys(SITE_THEME).includes(name);
    const currentTheme = SITE_THEME[hasTheme ? name : 'dark'];
    const newHref = styleHref.replace(/^(.+)(\/.+)\.css(.+)/gi, `$1/${ currentTheme.name }.css$3`);

    iconEl.innerHTML = currentTheme.icon;
    submenuEl.innerText = currentTheme.label;
    labelEl.innerText = currentTheme.label;

    styleEl.setAttribute('class', currentTheme.name);
    styleEl.setAttribute('href', newHref);

    saveThemeToSession(currentTheme.name);
}

function saveThemeToSession(name) {
    localStorage.setItem('theme', name);

    $.ajax({
        type: 'POST',
        url: '/session/theme',
        data: {
            'theme': name,
        },
        success: function (msg) {
        }
    });
}
