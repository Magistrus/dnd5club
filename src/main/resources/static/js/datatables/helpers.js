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

    if (!filters.length) {
        return;
    }

    for (let filter of filters) {
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

        if (!filter.checked) {
            filter.checked = true;
        }
    }

    setFiltered();
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
