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
                        result += '</span> <span>[' + row.englishName + ']</span></span><span class="books tip" title="' + row.book + '">' + row.bookshort + '</span></h3>';
                        result += '<div class="two_row"><span>' + row.ability + '</span></div></div>';
                        if (row.hasSubraces == true) {
                            result += '<button class="open tip" title="Разновидности" data-tipped-options="position: \'left\'"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M6 14L11.2929 19.2929C11.6834 19.6834 12.3166 19.6834 12.7071 19.2929L18 14M12 11V11C13.6569 11 15 9.65685 15 8V8C15 6.34315 13.6569 5 12 5V5C10.3431 5 9 6.34315 9 8V8C9 9.65685 10.3431 11 12 11Z" stroke="#4D4DAA" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg></button></div>';
                            result += '<div class="archetypes"><div class="main">';
                            if (row.subraces.length > 0) {
                                result += '<div class="archetype_list"><h4>Основное:</h4><ul>';
                                row.subraces.forEach(function (item, i, arr) {
                                    result += '<li class="archetype_item" id="' + item.englishName.split(' ')
                                                                                      .join('_') + '"><i class="add_favorites"></i><p>' + item.name + ' <span>' + item.bookshort + ' / ' + item.englishName + '</span></p></li>';
                                });
                                result += '</ul></div>';
                            }
                            if (row.settingSubraces.length > 0) {
                                result += '<div class="archetype_list setting_source ' + (localStorage.getItem('setting_source') != 'true' ? 'hide_block' : '') + '"><h4>Сеттинги:</h4><ul>';
                                row.settingSubraces.forEach(function (item, i, arr) {
                                    result += '<li class="archetype_item" id="' + item.englishName.split(' ')
                                                                                      .join('_') + '"><i class="add_favorites"></i><p>' + item.name + ' <span>' + item.bookshort + ' / ' + item.englishName + '</span></p></li>';
                                });
                                result += '</ul></div></div>';
                            }
                            if (row.homebrewSubraces.length > 0) {
                                result += '<div class="homebrew_list archetype_list custom_source ' + (localStorage.getItem('homebrew_source') != 'true' ? 'hide_block' : '') + '"><h4>Homebrew:</h4><ul>';
                                row.homebrewSubraces.forEach(function (item, i, arr) {
                                    result += '<li class="archetype_item" id="' + item.englishName.split(' ')
                                                                                      .join('_') + '"><i class="add_favorites"></i><p>' + item.name + ' <span>' + item.bookshort + ' / ' + item.englishName + '</span></p></li>';
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
        ],
        columnDefs: [
            {
                "targets": [ 0 ],
                "visible": true
            },
            {
                "targets": [ 1, 2 ],
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
                if (localStorage.getItem('homebrew_source') != 'true') {
                    $(row).addClass('hide_block');
                }
            } else if (data.setting) {
                $(row).addClass('setting_source');
                if (localStorage.getItem('setting_source') != 'true') {
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
    $('#races tbody').on('click', 'tr', function (event) {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        var data = row.data();
        if (cntrlIsPressed) {
            window.open('/races/' + data.englishName.split(' ').join('_'));
        }
        rowSelectIndex = row.index();
        if ($(event.target).closest('li').length != 0) {
            let liTareget = $(event.target).closest('li')[0];
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
        } else if (event.target.tagName == 'BUTTON' || event.target.parentNode.tagName == 'BUTTON' || event.target.parentNode.parentNode.tagName == 'BUTTON') {
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
        selectedRace = data;
        if ($(event.target).closest('li').length == 0) {
            event.target.scrollIntoView({ block: "center", behavior: "smooth" });
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
    if (selectedSubrace) {
        setActiveSubrace(data, selectedRace.englishName.replace(' ', '_'), selectedSubrace.englishName);
        $('#' + selectedSubrace.englishName.split(' ').join('_')).addClass('select_point');
        return;
    }
    $('#race_name').text(data.name);
    document.title = data.name + ' (' + data.englishName + ')' + ' | Классы D&D 5e';
    history.pushState('data to be passed', '', '/races/' + data.englishName.split(' ').join('_'));
    var url = '/races/fragment/' + data.id;
    $("#content_block").load(url, function () {
        if (localStorage.getItem('homebrew_source') == 'true') {
            $('#homebrew_source').prop('checked', true);
            $('.custom_source').removeClass('hide_block');
            $('#source_id').addClass('active');
        } 
        if (localStorage.getItem('setting_source') == 'true') {
            $('#setting_source').prop('checked', true);
            $('.setting_source').removeClass('hide_block');
            $('.module_source').removeClass('hide_block');
            $('#source_id').addClass('active');
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
    $('#race_name').text($('#' + subraceName).text());
    document.title = data.name + ' (' + subraceName + ') - ' + $('#' + subraceName).text() + ' | Подклассы D&D 5e';
    var url = '/races/' + data.englishName + '/subrace/' + subraceName.split(' ').join('_');
    $("#content_block").load(url, function () {
        $('#mobile_selector').change(function () {
            setActiveSubrace(data, raceName, $('#mobile_selector').val());
        });
        if (localStorage.getItem('homebrew_source') == 'true') {
            $('#homebrew_source').prop('checked', true);
            $('.custom_source').removeClass('hide_block');
            $('#source_id').addClass('active');
        } 
        if (localStorage.getItem('setting_source') == 'true') {
            $('#setting_source').prop('checked', true);
            $('.setting_source').removeClass('hide_block');
            $('.module_source').removeClass('hide_block');
            $('#source_id').addClass('active');
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
$('#btn_close').on('click', closeHandler);

function closeHandler() {
    document.getElementById('list_page_two_block').classList.remove('block_information');
    selectedRace = null;
    $('li').removeClass('select_point');

    $.magnificPopup.close();

    history.pushState('data to be passed', '', '/races/');
}

$('#btn_filters').on('click', function () {
    $('#searchPanes').toggleClass('hide_block');
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
    setFiltered();
});
$('#ability_clear_btn').on('click', function () {
    $('#ability_clear_btn').addClass('hide_block');
    $('.ability_checkbox').prop('checked', false);
    $('#races').DataTable().column(2).search("", true, false, false).draw();
    setFiltered();
});

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
