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

function switchFilters(el, dispatch = false) {
    const hasNextFilter = function (el) {
        return el && el.nextElementSibling && el.nextElementSibling.tagName === 'LABEL';
    }

    for (let element = el.closest('.separator_row'); hasNextFilter(element);) {
        element = element.nextElementSibling;

        const input = element.querySelector('input');

        input.checked = el.checked;
    }


    if (!dispatch) {
        return;
    }


    for (let element = el.closest('.separator_row'); hasNextFilter(element);) {
        element = element.nextElementSibling;

        const input = element.querySelector('input');

        input.dispatchEvent(new Event('change'));
    }
}

function getFilters(el) {
    const hasNextFilter = function (el) {
        return el && el.nextElementSibling && el.nextElementSibling.tagName === 'LABEL';
    }
    const filters = [];

    for (let element = el.closest('.separator_row'); hasNextFilter(element);) {
        element = element.nextElementSibling;

        const input = element.querySelector('input');

        if (input) {
            filters.push(input);
        }
    }

    return filters;
}

function checkFilters(el) {
    const filters = getFilters(el);

    let status = false;

    for (let index = 0; index < filters.length && !status; index++) {
        status = filters[index].checked;
    }

    return status;
}

function restoreSourceContainer() {
    const filterContainer = document.querySelector('.filter_container');
    const sources = filterContainer.querySelector('.sources');
    const filters = sources.querySelectorAll('input');

    let status = true;

    for (let index = 0; index < filters.length && status; index++) {
        status = filters[index].checked;
    }

    const resetBtn = sources.querySelector(`#${filters[0].name}_clear_btn`);

    if (!status) {
        resetBtn.classList.remove('hide_block');

        return;
    }

    resetBtn.classList.add('hide_block');
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
        const checked = checkFilters(homebrewToggle);

        homebrewToggle.checked = checked;
        data[TOGGLE_ID.filter.homebrew] = checked;
    }

    if (settingsToggle) {
        const checked = checkFilters(settingsToggle);

        settingsToggle.checked = checked;
        data[TOGGLE_ID.filter.settings] = checked;
    }

    const storageData = localStorage.getItem(STORAGE_KEY);
    const parsed = !!storageData && JSON.parse(storageData)
        ? JSON.parse(storageData)
        : {};

    parsed[storageKey] = data;

    localStorage.setItem(STORAGE_KEY, JSON.stringify(parsed));

    restoreSourceContainer();
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
        restoreSourceContainer();

        return;
    }

    const parsed = !!storageData && JSON.parse(storageData)
        ? JSON.parse(storageData)
        : {};

    if (!parsed[storageKey] || !Object.keys(parsed[storageKey]).length) {
        restoreToggles();
        restoreSourceContainer();

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
    restoreSourceContainer();
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

function addToggleListeners() {
    const homebrewToggle = document.getElementById(TOGGLE_ID.filter.homebrew);
    const settingsToggle = document.getElementById(TOGGLE_ID.filter.settings);

    if (settingsToggle) {
        settingsToggle.addEventListener('change', function () {
            switchFilters(settingsToggle, true)
        });
    }

    if (homebrewToggle) {
        homebrewToggle.addEventListener('change', function () {
            switchFilters(homebrewToggle, true)
        });
    }
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
