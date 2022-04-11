$(document).ready(function () {
    document.getElementById('list_page_two_block').classList.remove('block_information');
    var scrollEventHeight = 0;
    var rowSelectIndex = 0;
    let table = $('#races').DataTable({
        ajax: '/data/races',
        dom: 'tS',
        serverSide: true,
        deferRender: true,
        iDisplayLength: 80,
        scrollCollapse: true,
        select: true,
        select: {
            style: 'single',
            toggleable: false
        },
        order: [ [ 0, 'asc' ] ],
        columns: [
            {
                data: "name",
                render: function (data, type, row) {
                    if (type === 'display') {
                        let result = '<div class="wrapper"><i class="info_block">' + row.icon + '</i>';
                        result += '<div class="content"><h3 class="row_name"><span><span class="name">' + row.name;
                        result += '</span> <ename>[' + row.englishName + ']</ename></span><span class="books tip" title="' + row.book + '">' + row.bookshort + '</span></h3>';
                        result += '<div class="two_row"><span>' + row.ability + '</span></div></div>';
                        if (row.hasSubraces == true) {
                            result += '<button class="open tip" title="Разновидности" data-tipped-options="position: \'left\'"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M6 14L11.2929 19.2929C11.6834 19.6834 12.3166 19.6834 12.7071 19.2929L18 14M12 11V11C13.6569 11 15 9.65685 15 8V8C15 6.34315 13.6569 5 12 5V5C10.3431 5 9 6.34315 9 8V8C9 9.65685 10.3431 11 12 11Z" stroke="#4D4DAA" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg></button></div>';
                            result += '<div class="archetypes"><div class="main">';
                            if (row.subraces.length > 0) {
                                result += '<div class="archetype_list"><h4>Основное:</h4><ul>';
                                row.subraces.forEach(function (item, i, arr) {
                                    result += '<li class="archetype_item" id="' + item.englishName.split(' ')
                                    .join('_') + '" data-arch-source="'+item.bookshort+'"' + '" data-name="'+item.name+'"'  + '" data-english-name="'+item.englishName+'"><i class="add_favorites"></i><p>' + item.name + ' <span class="tip" title="' + item.book + '">' + item.bookshort + '</span><span> / ' + item.englishName + '</span></p></li>';
                                });
                                result += '</ul></div>';
                            }
                            if (row.settingSubraces.length > 0) {
                                result += '<div class="archetype_list setting_source ' + (!isSettingsShowed('races') ? 'hide_block' : '') + '"><h4>Сеттинги:</h4><ul>';
                                row.settingSubraces.forEach(function (item, i, arr) {
                                    result += '<li class="archetype_item" id="' + item.englishName.split(' ')
                                    .join('_') + '" data-arch-source="'+item.bookshort+'"' + '" data-name="'+item.name+'"'  + '" data-english-name="'+item.englishName+'"><i class="add_favorites"></i><p>' + item.name + ' <span class="tip" title="' + item.book + '">' + item.bookshort + '</span><span> / ' + item.englishName + '</span></p></li>';
                                });
                                result += '</ul></div></div>';
                            }
                            if (row.moduleSubraces.length > 0) {
                                result += '<div class="archetype_list setting_source ' + (!isSettingsShowed('races') ? 'hide_block' : '') + '"><h4>Приключения:</h4><ul>';
                                row.moduleSubraces.forEach(function (item, i, arr) {
                                    result += '<li class="archetype_item" id="' + item.englishName.split(' ')
                                    .join('_') + '" data-arch-source="'+item.bookshort+'"' + '" data-name="'+item.name+'"'  + '" data-english-name="'+item.englishName+'"><i class="add_favorites"></i><p>' + item.name + ' <span class="tip" title="' + item.book + '">' + item.bookshort + '</span><span> / ' + item.englishName + '</span></p></li>';
                                });
                                result += '</ul></div></div>';
                            }
                            if (row.homebrewSubraces.length > 0) {
                                result += '<div class="homebrew_list archetype_list custom_source ' + (!isHomebrewShowed('races') ? 'hide_block' : '') + '"><h4>Homebrew:</h4><ul>';
                                row.homebrewSubraces.forEach(function (item, i, arr) {
                                    result += '<li class="archetype_item" id="' + item.englishName.split(' ')
                                    .join('_') + '" data-arch-source="'+item.bookshort+'"' + '" data-name="'+item.name+'"'  + '" data-english-name="'+item.englishName+'"><i class="add_favorites"></i><p>' + item.name + ' <span class="tip" title="' + item.book + '">' + item.bookshort + '</span><span> / ' + item.englishName + '</span></p></li>';
                                });
                                result += '</ul></div>';
                            }
                        }
                        return result;
                    }
                    return data;
                }
            },
            {
                data: "englishName",
            },
            {
                data: "ability",
                searchable: false,
            },
            {
                data: 'bookshort',
                searchable: false,
            },
        ],
        searchCols: [
            null,
            null,
            getSearchColumn('ability', 'races'),
            getSearchColumn('book', 'races'),
        ],
        columnDefs: [
            {
                "targets": [ 0 ],
                "visible": true
            },
            {
                "targets": [ 1, 2, 3 ],
                "visible": false
            },
        ],
        language: {
            processing: "Загрузка...",
            searchPlaceholder: "Поиск ",
            search: "_INPUT_",
            lengthMenu: "Показывать _MENU_ записей на странице",
            zeroRecords: "Ничего не найдено",
            info: "Показано _TOTAL_",
            infoEmpty: "Нет доступных записей",
            infoFiltered: "из _MAX_",
            loadingRecords: "Загрузка...",
        },
        initComplete: function (settings, json) {
            restoreFilter('races');
        },
        drawCallback: function (settings) {
            addEventListeners();

            if (selectedRace) {
                selectRace(selectedRace);
                var rowIndexes = [];
                table.rows(function (idx, data, node) {
                    if (data.id === selectedRace.id) {
                        rowIndexes.push(idx);
                    }
                    return false;
                });
                rowSelectIndex = rowIndexes[0];
                $('#races tbody tr:eq(' + rowSelectIndex + ')').click();
                table.row(':eq(' + rowSelectIndex + ')', { page: 'current' }).select();
            }
        },
        createdRow: function (row, data, dataIndex) {
            if (data.homebrew) {
                $(row).addClass('custom_source');
                if (!isHomebrewShowed('races')) {
                    $(row).addClass('hide_block');
                }
            } else if (data.setting) {
                $(row).addClass('setting_source');
                if (!isSettingsShowed('races')) {
                    $(row).addClass('hide_block');
                }
            }
        },
    });
    $('#races tbody').on('mouseup', 'tr', function (e) {
        if (e.which == 2) {
            var tr = $(this).closest('tr');
            var row = table.row(tr);
            rowSelectIndex = row.index();
            var data = row.data();
            window.open('/races/' + data.englishName.split(' ').join('_'));
        }
    });
    $('#races tbody').on('click', 'tr', function (e) {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        var data = row.data();
        if ((window.navigator.userAgent.indexOf("Mac") !== -1 && e.metaKey) || e.ctrlKey) {
            window.open('/races/' + data.englishName.split(' ').join('_'));
        }
        rowSelectIndex = row.index();
        if ($(e.target).closest('li').length != 0) {
            let liTareget = $(e.target).closest('li')[0];
            if (liTareget.classList.contains('select_point')) {
                liTareget.classList.remove('select_point');
                selectedSubrace = null;
                selectRace(selectedRace);
            } else {
                $('li').removeClass('select_point');
                liTareget.classList.add('select_point');
                selectedSubrace = null;
                setActiveSubrace(data, data.englishName.split(' ').join('_'), liTareget.id);
            }
            if (!document.getElementById('list_page_two_block').classList.contains('block_information')) {
                document.getElementById('list_page_two_block').classList.add('block_information');
            }
        } else if (e.target.tagName == 'BUTTON' || e.target.parentNode.tagName == 'BUTTON' || e.target.parentNode.parentNode.tagName == 'BUTTON') {
            tr[0].classList.toggle('open');
            SimpleBar.instances.get(document.querySelector('[data-simplebar]')).recalculate();
        } else {
            $('li').removeClass('select_point');
            $('tr').removeClass('open');
            tr[0].classList.add('open');
            selectRace(data);
            if (!document.getElementById('list_page_two_block').classList.contains('block_information')) {
                document.getElementById('list_page_two_block').classList.add('block_information');
            }
        }
        if (!$(e.target).closest('li').length) {
            setTimeout(function () {
                e.target.closest('.simplebar-content-wrapper')
                 .scrollTo({
                     top: e.target.closest('tr').offsetTop - 16,
                     behavior: "smooth"
                 });
            }, 300)
        }
    });
});

function addEventListeners(force = false) {
    $(document).ready(function () {
        onDeselectListener();
    });

    $(window).resize(function () {
        onDeselectListener();
    });

    if (force) {
        onDeselectListener();
    }
}

function onDeselectListener() {
    const element = $('#races');
    const table = element.dataTable().api();

    if (window.innerWidth < 1200 && !element.hasClass('has-deselect-handler')) {
        table.on('deselect.dt', closeHandler);
        element.addClass('has-deselect-handler');

        return
    }

    if (window.innerWidth >= 1200) {
        table.off('deselect.dt', closeHandler);
        element.removeClass('has-deselect-handler');
    }
}

function selectRace(data) {
    selectedRace = data;
    if (selectedSubrace) {
        setActiveSubrace(data, selectedRace.englishName.replace(' ', '_'), selectedSubrace.englishName);
        $('#' + selectedSubrace.englishName.split(' ').join('_')).addClass('select_point');
        return;
    }
    $('#race_name').text(data.name);
    $('#english_name').html(data.englishName);

    document.title = data.name + ' (' + data.englishName + ')' + ' | Классы D&D 5e';
    history.pushState('data to be passed', '', '/races/' + data.englishName.split(' ').join('_'));
    var url = '/races/fragment/' + data.id;
    $("#content_block").load(url, function () {
        if (isHomebrewShowed('races')) {
            $('.custom_source').removeClass('hide_block');
            $('#source_id').addClass('active');
        }
        if (isSettingsShowed('races')) {
            $('.setting_source').removeClass('hide_block');
            $('.module_source').removeClass('hide_block');
        }
        $('#mobile_selector').change(function () {
            setActiveSubrace(data, data.englishName.replace(' ', '_'), $('#mobile_selector').val());
        });
        $('.image-container').magnificPopup({
            delegate: 'a',
            type: 'image',
            gallery: {
                enabled: true
            }
        })
    });
}

function setActiveSubrace(data, raceName, subraceName) {
	let $name = $('#' + subraceName);
    $('#race_name').text($name.attr('data-name'));
    $('#english_name').html(data.englishName + ' ' + $name.attr('data-english-name'));
    document.title = data.name + ' (' + subraceName + ') - ' + $('#' + subraceName).text() + ' | Подклассы D&D 5e';
    var url = '/races/' + data.englishName + '/subrace/' + subraceName.split(' ').join('_');
    $("#content_block").load(url, function () {
        $('#mobile_selector').change(function () {
            setActiveSubrace(data, raceName, $('#mobile_selector').val());
        });
        if (isHomebrewShowed('races')) {
            $('.custom_source').removeClass('hide_block');
        }
        if (isSettingsShowed('races')) {
            $('.setting_source').removeClass('hide_block');
            $('.module_source').removeClass('hide_block');
        }
        selectedSubrace = null;
        $('.image-container').magnificPopup({
            delegate: 'a',
            type: 'image',
            gallery: {
                enabled: true
            }
        });
    });
    history.pushState('data to be passed', data.englishName, '/races/' + data.englishName + '/' + subraceName);
}

$('#search').on('keyup click', function () {
    if ($('#search').val()) {
        $('#text_clear').show();
    } else {
        $('#text_clear').hide();
    }
    selectedRace = null;
    $('#races').DataTable().tables().search($(this).val()).draw();
});
$('#text_clear').on('click', function () {
    $('#search').val('');
    selectedRace = null;
    $('#text_clear').hide();
    $('#races').DataTable().tables().search($(this).val()).draw();
});
$('#btn_close').on('click', function () {
    if (window.innerWidth < 1200) {
        $('#races').dataTable().api().rows().deselect();
        return;
    }
    closeHandler();
});

function closeHandler() {
    document.getElementById('list_page_two_block').classList.remove('block_information');
    $('li').removeClass('select_point');
    selectedRace = null;

    $.magnificPopup.close();

    history.pushState('data to be passed', '', '/races/');
}

$('#btn_filters').on('click', function () {
    $('#searchPanes').toggleClass('hide_block');

    $('#btn_filters').toggleClass('open');
});
$('.ability_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="ability"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#races').DataTable().column(2).search(properties, true, false, false).draw();
    if (properties) {
        $('#ability_clear_btn').removeClass('hide_block');
    } else {
        $('#ability_clear_btn').addClass('hide_block');
    }
    saveFilter('races');
});
$('#ability_clear_btn').on('click', function () {
    $('#ability_clear_btn').addClass('hide_block');
    $('.ability_checkbox').prop('checked', false);
    $('#races').DataTable().column(2).search("", true, false, false).draw();
    saveFilter('races');
});
$('.book_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="book"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#races').DataTable().column(3).search(properties, true, false, false).draw();
    if (properties) {
        $('#book_clear_btn').removeClass('hide_block');
    } else {
        $('#book_clear_btn').addClass('hide_block');
    }
    saveFilter('races');
});
$('#book_clear_btn').on('click', function () {
    $('#book_clear_btn').addClass('hide_block');
    $('.book_checkbox').prop('checked', true);
    $('#races').DataTable().column(3).search("", true, false, false).draw();
    saveFilter('races');
});
