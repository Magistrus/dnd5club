$(document).ready(function () {
    var scrollEventHeight = 0;
    var rowSelectIndex = 0;
    var table = $('#spells').DataTable({
        ajax: '/data/spells?sourceTypes=' + sourceTypes,
        dom: 't',
        serverSide: true,
        deferRender: true,
        scrollCollapse: true,
        iDisplayLength: 80,
        order: [ [ 0, 'asc' ], [ 2, 'asc' ] ],
        select: true,
        select: {
            style: 'single',
            toggleable: false
        },
        columns: [
            {
                data: 'level',
                searchable: false,
            },
            {
                data: 'school',
                searchable: false,
            },
            {
                data: "name",
                render: function (data, type, row) {
                    if (type === 'display') {
                        var result = '<div class="tip info_block" title="' + (row.level === 0 ? 'Заговор' : row.level + ' уровень заклинания') + '">' + (row.level === 0 ? '◐' : row.level) + '</div>';
                        result += '<div class="content"><h3 class="row_name"><span>' + row.name;
                        result += '</span><span>[' + row.englishName + ']</span></h3>';
                        result += '<div class="content_description"><div class="secondary_name s1">';
                        if (row.concentration) {
                            result += '<span class="tip concentration" title="Концентрация">К</span>';
                        }
                        if (row.ritual) {
                            result += '<span class="tip ritual" title="Ритуал">Р</span>';
                        }
                        result += '<p class="capitalize_text">' + row.school + '</p></div>';
                        result += '<div class="secondary_name s2 l_alg_left">';
                        if (row.verbal) {
                            result += '<span class="tip excretion" title="Вербальный">' + row.verbal + '</span>';
                        }
                        if (row.somatic) {
                            result += '<span class="tip excretion" title="Соматический">' + row.somatic + '</span>';
                        }
                        if (row.material) {
                            result += '<span class="tip excretion" title="Материальный">' + row.material + '</span>';
                        }
                        result += '</div></div></div>';

                        return result;
                    }
                    return data;
                }
            },
            {
                data: 'englishName',
            },
            {
                data: "classes",
                searchable: false,
            },
            {
                data: "damageType",
                searchable: false
            },
            {
                data: "ritual",
                searchable: false
            },
            {
                data: "concentration",
                searchable: false
            },
            {
                data: 'timeCast',
                searchable: false,
            },
            {
                data: 'components',
                searchable: false,
            },
            {
                data: 'bookshort',
                searchable: false,
            },
        ],
        searchCols: [
            getSearchColumn('level', 'spells'),
            getSearchColumn('school', 'spells'),
            null,
            null,
            getSearchColumn('class', 'spells'),
            getSearchColumn('damage', 'spells'),
            getSearchColumn('ritual', 'spells'),
            getSearchColumn('concentration', 'spells'),
            getSearchColumn('timecast', 'spells'),
            getSearchColumn('component', 'spells'),
            getSearchColumn('book', 'spells'),
        ],
        columnDefs: [
            {
                "targets": [ 0, 1, 3, 4, 5, 6, 7, 8, 9, 10 ],
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
            restoreFilter('spells');

            scrollEventHeight = document.getElementById('scroll_load_simplebar').offsetHeight - 300;
            const simpleBar = new SimpleBar(document.getElementById('scroll_load_simplebar'));
            simpleBar.getScrollElement().addEventListener('scroll', function (event) {
                if (simpleBar.getScrollElement().scrollTop > scrollEventHeight) {
                    table.page.loadMore();
                    simpleBar.recalculate();
                    scrollEventHeight += 750;

                    addEventListeners(true);
                }
            });
        },
        createdRow: function (row, data, dataIndex) {
            if (data.homebrew) {
                $(row).addClass('custom_source');

                if (!isHomebrewShowed('spells')) {
                    $(row).addClass('hide_block');
                }
            }

            if (data.setting) {
                $(row).addClass('setting_source');

                if (!isSettingsShowed('spells')) {
                    $(row).addClass('hide_block');
                }
            }
        },
        drawCallback: function (settings) {
            addEventListeners();

            if (selectedSpell === null) {
                if (!$('#list_page_two_block').hasClass('block_information') && selectedSpell === null) {
                    return;
                }
            }
            if (localStorage.getItem('homebrew_source') == 'false' && selectedSpell === null) {
                for (; rowSelectIndex < table.data().count(); rowSelectIndex++) {
                    if (!table.rows(rowSelectIndex).data()[0].homebrew) {
                        $('#spells tbody tr:eq(' + rowSelectIndex + ')').click();
                        break;
                    }
                }
            }
            if (table.data().count() == 1) {
                $('#spells tbody tr:eq(0)').click();
                table.row(':eq(0)', { page: 'current' }).select();
                return;
            }
            if (selectedSpell) {
                selectSpell(selectedSpell);
                let rowIndexes = [];
                table.rows(function (idx, data, node) {
                    if (data.id === selectedSpell.id) {
                        rowIndexes.push(idx);
                    }
                    return false;
                });
                rowSelectIndex = rowIndexes[0];
            }
            $('#spells tbody tr:eq(' + rowSelectIndex + ')').click();
            table.row(':eq(' + rowSelectIndex + ')', { page: 'current' }).select();
        }
    });
    $('#spells tbody').on('mouseup', 'tr', function (e) {
        if (e.which == 2) {
            var tr = $(this).closest('tr');
            var row = table.row(tr);
            rowSelectIndex = row.index();
            var data = row.data();
            window.open('/spells/' + data.englishName.split(' ').join('_'));
        }
    });
    $('#spells tbody').on('click', 'tr', function (e) {
        if (!document.getElementById('list_page_two_block').classList.contains('block_information')) {
            document.getElementById('list_page_two_block').classList.add('block_information');
        }
        var tr = $(this).closest('tr');
        var table = $('#spells').DataTable();
        var row = table.row(tr);
        var data = row.data();
        if ((window.navigator.userAgent.indexOf("Mac") !== -1 && e.metaKey) || e.ctrlKey) {
            window.open('/spells/' + data.englishName.split(' ').join('_'));
        }
        rowSelectIndex = row.index();
        selectSpell(data);
        selectedSpell = data;
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
    const element = $('#spells');
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

function selectSpell(data) {
    $('#row_name').html(data.name);
    document.title = data.name + ' (' + data.englishName + ')' + ' | Заклинания D&D 5e';
    history.pushState('data to be passed', '', '/spells/' + data.englishName.split(' ').join('_'));
    const url = '/spells/fragment/' + data.id;
    $("#content_block").load(url);
}

var timer, delay = 300;
$('#search').bind('keydown blur change', function (e) {
    var _this = $(this);
    clearTimeout(timer);
    timer = setTimeout(function () {
        if ($('#search').val()) {
            $('#text_clear').show();
        } else {
            $('#text_clear').hide();
        }
        const table = $('#spells').DataTable();
        selectedSpell = null;
        rowSelectIndex = 0;
        table.tables().search($('#search').val()).draw();
    }, delay);
});
$('#text_clear').on('click', function () {
    $('#search').val('');
    const table = $('#spells').DataTable();
    table.tables().search($(this).val()).draw();
    $('#text_clear').hide();
});
$('#btn_close').on('click', function () {
    $('#spells').dataTable().api().rows().deselect();
});

function closeHandler() {
    document.getElementById('list_page_two_block').classList.remove('block_information');
    localStorage.removeItem('selected_spell');
    selectedSpell = null;

    $.magnificPopup.close();

    history.pushState('data to be passed', 'Заклинания', '/spells/');
}

$('#btn_filters').on('click', function () {
    $('#searchPanes').toggleClass('hide_block');
});
$('.level_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="level"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#spells').DataTable().column(0).search(properties, true, false, false).draw();
    if (properties) {
        $('#level_clear_btn').removeClass('hide_block');
    } else {
        $('#level_clear_btn').addClass('hide_block');
    }
    saveFilter('spells');
});
$('#level_clear_btn').on('click', function () {
    $('#level_clear_btn').addClass('hide_block');
    $('.level_checkbox').prop('checked', false);
    $('#spells').DataTable().column(0).search("", true, false, false).draw();
    saveFilter('spells');
});
$('.class_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="class"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#spells').DataTable().column(4).search(properties, true, false, false).draw();
    if (properties) {
        $('#class_clear_btn').removeClass('hide_block');
    } else {
        $('#class_clear_btn').addClass('hide_block');
    }
    saveFilter('spells');
});
$('#class_clear_btn').on('click', function () {
    $('#class_clear_btn').addClass('hide_block');
    $('.class_checkbox').prop('checked', false);
    $('#spells').DataTable().column(4).search("", true, false, false).draw();
    saveFilter('spells');
});
$('.school_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="school"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#spells').DataTable().column(1).search(properties, true, false, false).draw();
    if (properties) {
        $('#school_clear_btn').removeClass('hide_block');
    } else {
        $('#school_clear_btn').addClass('hide_block');
    }
    saveFilter('spells');
});
$('#school_clear_btn').on('click', function () {
    $('#school_clear_btn').addClass('hide_block');
    $('.school_checkbox').prop('checked', false);
    $('#spells').DataTable().column(1).search("", true, false, false).draw();
    saveFilter('spells');
});
$('.timecast_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="timecast"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#spells').DataTable().column(8).search(properties, true, false, false).draw();
    if (properties) {
        $('#timecast_clear_btn').removeClass('hide_block');
    } else {
        $('#timecast_clear_btn').addClass('hide_block');
    }
    saveFilter('spells');
});
$('#timecast_clear_btn').on('click', function () {
    $('#timecast_clear_btn').addClass('hide_block');
    $('.timecast_checkbox').prop('checked', false);
    $('#spells').DataTable().column(8).search("", true, false, false).draw();
    saveFilter('spells');
});
$('.component_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="component"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#spells').DataTable().column(9).search(properties, true, false, false).draw();
    if (properties) {
        $('#component_clear_btn').removeClass('hide_block');
    } else {
        $('#component_clear_btn').addClass('hide_block');
    }
    saveFilter('spells');
});
$('#component_clear_btn').on('click', function () {
    $('#component_clear_btn').addClass('hide_block');
    $('.component_checkbox').prop('checked', false);
    $('#spells').DataTable().column(9).search("", true, false, false).draw();
    saveFilter('spells');
});
$('.ritual_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="ritual"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#spells').DataTable().column(7).search(properties, true, false, false).draw();
    if (properties) {
        $('#ritual_clear_btn').removeClass('hide_block');
    } else {
        $('#ritual_clear_btn').addClass('hide_block');
    }
    saveFilter('spells');
});
$('#ritual_clear_btn').on('click', function () {
    $('#ritual_clear_btn').addClass('hide_block');
    $('.ritual_checkbox').prop('checked', false);
    $('#spells').DataTable().column(7).search("", true, false, false).draw();
    saveFilter('spells');
});
$('.concentration_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="concentration"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#spells').DataTable().column(8).search(properties, true, false, false).draw();
    if (properties) {
        $('#concentration_clear_btn').removeClass('hide_block');
    } else {
        $('#concentration_clear_btn').addClass('hide_block');
    }
    saveFilter('spells');
});
$('#concentration_clear_btn').on('click', function () {
    $('#concentration_clear_btn').addClass('hide_block');
    $('.concentration_checkbox').prop('checked', false);
    $('#spells').DataTable().column(8).search("", true, false, false).draw();
    saveFilter('spells');
});
$('.damage_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="damage"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#spells').DataTable().column(5).search(properties, true, false, false).draw();
    if (properties) {
        $('#damage_clear_btn').removeClass('hide_block');
    } else {
        $('#damage_clear_btn').addClass('hide_block');
    }
    saveFilter('spells');
});
$('#damage_clear_btn').on('click', function () {
    $('#damage_clear_btn').addClass('hide_block');
    $('.damage_checkbox').prop('checked', false);
    $('#spells').DataTable().column(5).search("", true, false, false).draw();
    saveFilter('spells');
});
$('.book_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="book"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#spells').DataTable().column(10).search(properties, true, false, false).draw();
    if (properties) {
        $('#book_clear_btn').removeClass('hide_block');
    } else {
        $('#book_clear_btn').addClass('hide_block');
    }
    saveFilter('spells');
});
$('#book_clear_btn').on('click', function () {
    $('#book_clear_btn').addClass('hide_block');
    $('.book_checkbox').prop('checked', false);
    $('#spells').DataTable().column(10).search("", true, false, false).draw();
    saveFilter('spells');
});
