function setFiltered() {
    let boxes = $('input:checkbox:checked.filter').map(function () {
        return this.value;
    }).get().join('|');
    if (boxes.length === 0) {
        $('#icon_filter').removeClass('active');
    } else {
        $('#icon_filter').addClass('active');
    }
}

function saveFilter(storageKey) {
    const filterContainer = document.querySelector('.filter_container');
    const filters = filterContainer.querySelectorAll('input:checked');
    const data = {};

    for (let filter of filters) {
        if (filter.name === 'settings_off' || filter.name === 'homebrew_off') {
            continue;
        }

        if (!Object.keys(data).includes(filter.name)) {
            data[filter.name] = [];
        }

        data[filter.name].push(filter.value);
    }

    const storageData = localStorage.getItem('dnd5club_saved_filter');
    const parsed = !!storageData && JSON.parse(storageData)
        ? JSON.parse(storageData)
        : {};

    parsed[storageKey] = data;

    localStorage.setItem('dnd5club_saved_filter', JSON.stringify(parsed));

    setFiltered();
}

function restoreFilter(storageKey) {
    const storageData = localStorage.getItem('dnd5club_saved_filter');

    if (!storageData) {
        return;
    }

    const parsed = !!storageData && JSON.parse(storageData)
        ? JSON.parse(storageData)
        : {};

    if (!parsed[storageKey] || !Object.keys(parsed[storageKey]).length) {
        return;
    }

    const savedFilter = parsed[storageKey];
    const filterContainer = document.querySelector('.filter_container');
    const filters = filterContainer.querySelectorAll('input');

    for (let filter of filters) {
        if (!Object.keys(savedFilter).includes(filter.name)) {
            filter.checked = false;

            continue;
        }

        if (!Array.isArray(savedFilter[filter.name]) || !savedFilter[filter.name].includes(filter.value)) {
            filter.checked = false;

            continue;
        }

        const resetBtn = filterContainer.querySelector(`#${filter.name}_clear_btn`);

        if (resetBtn.classList.contains('hide_block')) {
            resetBtn.classList.remove('hide_block');
        }

        const parentWrapper = filter.closest('.filter_crumbs_box');

        if (parentWrapper.classList.contains('hide')) {
            parentWrapper.classList.remove('hide');
        }

        if (!filter.checked) {
            filter.checked = true;
        }
    }

    restoreSources();
    setFiltered();
}

function restoreSources() {
    const storageSettings = localStorage.getItem('setting_source');
    const storageHomebrew = localStorage.getItem('homebrew_source');
    const settingsToggle = document.getElementById('filter_settings');
    const homebrewToggle = document.getElementById('filter_homebrew');

    settingsToggle.checked = !storageSettings || storageSettings === 'true';
    homebrewToggle.checked = !storageHomebrew || storageHomebrew === 'true';

    console.log(!storageSettings || storageSettings === 'true', storageSettings, settingsToggle);
    console.log(!storageHomebrew || storageHomebrew === 'true', storageHomebrew, homebrewToggle);

    switchToggle(settingsToggle, false);
    switchToggle(homebrewToggle, false);
    disableClassMainToggles();
}

function getSearchString(name, storageKey) {
    const storageData = localStorage.getItem('dnd5club_saved_filter');

    if (!storageData) {
        return "";
    }

    const parsed = !!storageData && JSON.parse(storageData)
        ? JSON.parse(storageData)
        : {};

    if (!parsed[storageKey] || !Object.keys(parsed[storageKey]).length) {
        return "";
    }

    const savedFilter = parsed[storageKey];

    if (!savedFilter[name] || !Array.isArray(savedFilter[name]) || !savedFilter[name].length) {
        return "";
    }

    return savedFilter[name].join('|')
}

function getSearchColumn(name, storageKey) {
    return {
        search: getSearchString(name, storageKey),
        caseInsensitive: true,
        regex: false,
        smart: false
    }
}

function toggleSources(toggle, mainToggle) {
    mainToggle.checked = toggle.checked;

    localStorage.setItem(mainToggle.getAttribute('id'), mainToggle.checked);

    disableClassMainToggles();
    switchToggle(toggle);
}

function switchToggle(el, dispatch = true) {
    const hasNextFilter = function (el) {
        return el && el.nextElementSibling && el.nextElementSibling.tagName === 'LABEL';
    }

    for (let element = el.closest('.separator_row'); hasNextFilter(element);) {
        element = element.nextElementSibling;

        const input = element.querySelector('input');

        if (dispatch) {
            input.checked = el.checked;
            input.dispatchEvent(new Event('change'));
        }

        if (el.checked) {
            input.removeAttribute('disabled');
        } else {
            input.setAttribute('disabled', 'disabled');
        }
    }
}

function disableClassMainToggles() {
    const settingsToggle = document.getElementById('setting_source');
    const homebrewToggle = document.getElementById('homebrew_source');
    const icon = document.getElementById('source_id');

    if (settingsToggle.checked || homebrewToggle.checked) {
        icon.classList.add('active')

        return;
    }

    icon.classList.remove('active');
}

function addToggleListeners() {
    const mainSettings = document.getElementById('setting_source');
    const mainHomebrew = document.getElementById('homebrew_source');
    const homebrewToggle = document.getElementById('filter_homebrew');
    const settingsToggle = document.getElementById('filter_settings');

    settingsToggle.addEventListener('change', function () {
        toggleSources(settingsToggle, mainSettings)
    });

    homebrewToggle.addEventListener('change', function () {
        toggleSources(homebrewToggle, mainHomebrew)
    });
}

document.addEventListener('DOMContentLoaded', addToggleListeners);
