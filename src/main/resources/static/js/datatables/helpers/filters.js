const STORAGE_KEY = 'dnd5club_saved_filter';
const TOGGLE_ID = {
    main: {
        settings: 'setting_source',
        homebrew: 'homebrew_source'
    },
    filter: {
        settings: 'filter_settings',
        homebrew: 'filter_homebrew'
    }
}

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

    const homebrewToggle = document.getElementById(TOGGLE_ID.filter.homebrew);
    const settingsToggle = document.getElementById(TOGGLE_ID.filter.settings);

    if (homebrewToggle) {
        data[TOGGLE_ID.filter.homebrew] = homebrewToggle.checked;
    }

    if (settingsToggle) {
        data[TOGGLE_ID.filter.settings] = settingsToggle.checked;
    }

    const storageData = localStorage.getItem(STORAGE_KEY);
    const parsed = !!storageData && JSON.parse(storageData)
        ? JSON.parse(storageData)
        : {};

    parsed[storageKey] = data;

    localStorage.setItem(STORAGE_KEY, JSON.stringify(parsed));

    setFiltered();
}

function restoreFilter(storageKey) {
    const storageData = localStorage.getItem(STORAGE_KEY);
    const restoreToggles = function (storage = {}) {
        const homebrewToggle = document.getElementById(TOGGLE_ID.filter.homebrew);
        const settingsToggle = document.getElementById(TOGGLE_ID.filter.settings);
        const mainHomebrew = document.getElementById(TOGGLE_ID.main.homebrew);
        const mainSettings = document.getElementById(TOGGLE_ID.main.settings);

        if (homebrewToggle) {
            const saved = TOGGLE_ID.filter.homebrew in storage;

            homebrewToggle.checked = saved
                ? storage[TOGGLE_ID.filter.homebrew]
                : mainHomebrew.checked;

            if (!homebrewToggle.checked) {
                switchFilters(homebrewToggle);
            }
        }

        if (settingsToggle) {
            const saved = TOGGLE_ID.filter.settings in storage;

            settingsToggle.checked = saved
                ? storage[TOGGLE_ID.filter.settings]
                : mainSettings.checked;

            if (!settingsToggle.checked) {
                switchFilters(settingsToggle);
            }
        }
    }

    if (!storageData) {
        restoreToggles();

        return;
    }

    const parsed = !!storageData && JSON.parse(storageData)
        ? JSON.parse(storageData)
        : {};

    if (!parsed[storageKey] || !Object.keys(parsed[storageKey]).length) {
        restoreToggles();

        return;
    }

    const savedFilter = parsed[storageKey];
    const filterContainer = document.querySelector('.filter_container');
    const filters = filterContainer.querySelectorAll('input');

    for (let filter of filters) {
        if (filter.name === 'settings_off' || filter.name === 'homebrew_off') {
            continue;
        }

        if (!Object.keys(savedFilter).includes(filter.name)) {
            filter.checked = false;

            continue;
        }

        if (!Array.isArray(savedFilter[filter.name]) || !savedFilter[filter.name].includes(filter.value)) {
            filter.checked = false;

            continue;
        }

        const resetBtn = filterContainer.querySelector(`#${ filter.name }_clear_btn`);

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

    restoreToggles(savedFilter);
    setFiltered();
}

function getSearchString(name, storageKey) {
    const storageData = localStorage.getItem(STORAGE_KEY);

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

function switchFilters(el, dispatch = false) {
    const hasNextFilter = function (el) {
        return el && el.nextElementSibling && el.nextElementSibling.tagName === 'LABEL';
    }

    for (let element = el.closest('.separator_row'); hasNextFilter(element);) {
        element = element.nextElementSibling;

        const input = element.querySelector('input');

        input.checked = el.checked;

        if (dispatch) {
            input.dispatchEvent(new Event('change'));
        }

        if (el.checked) {
            input.removeAttribute('disabled');
        } else {
            input.setAttribute('disabled', 'disabled');
        }
    }
}

function addToggleListeners() {
    const homebrewToggle = document.getElementById(TOGGLE_ID.filter.homebrew);
    const settingsToggle = document.getElementById(TOGGLE_ID.filter.settings);

    settingsToggle.addEventListener('change', function () {
        switchFilters(settingsToggle, true)
    });

    homebrewToggle.addEventListener('change', function () {
        switchFilters(homebrewToggle, true)
    });
}

document.addEventListener('DOMContentLoaded', addToggleListeners);

function isHomebrewShowed(storageKey) {
    const storage = localStorage.getItem(STORAGE_KEY);
    const homebrewToggle = document.getElementById(TOGGLE_ID.main.homebrew);
    const parsed = !!storage && JSON.parse(storage)
        ? JSON.parse(storage)
        : undefined;
    const sectionStorage = !!parsed && storageKey in parsed
        ? parsed[storageKey]
        : undefined;

    return !!sectionStorage && TOGGLE_ID.filter.homebrew in sectionStorage
        ? sectionStorage[TOGGLE_ID.filter.homebrew]
        : homebrewToggle.checked;
}

function isSettingsShowed(storageKey) {
    const storage = localStorage.getItem(STORAGE_KEY);
    const settingsToggle = document.getElementById(TOGGLE_ID.main.settings);
    const parsed = !!storage && JSON.parse(storage)
        ? JSON.parse(storage)
        : undefined;
    const sectionStorage = !!parsed && storageKey in parsed
        ? parsed[storageKey]
        : undefined;

    return !!sectionStorage && TOGGLE_ID.filter.settings in sectionStorage
        ? sectionStorage[TOGGLE_ID.filter.settings]
        : settingsToggle.checked;
}

