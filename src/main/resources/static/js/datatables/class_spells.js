$(document).ready(function () {
    var spellScrollEventHeight = 0;
    let spellTable = $('#spells').DataTable({
        ajax: '/data/spells?classId=' + classId,
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
                searchable: false
            },
            {
                data: 'school',
                searchable: false
            },
            {
                data: "name",
                render: function (data, type, row) {
                    if (type === 'display') {
                        var result = '<div class="tip info_block" title="' + (row.level === 0 ? 'Заговор' : row.level + ' уровень заклинания') + '">' + (row.level === 0 ? '◐' : row.level) + '</div>';
                        result += '<div class="content"><h3 class="row_name"><span>' + row.name;
                        result += '</span> <ename>[' + row.englishName + ']</ename></h3>';
                        result += '<div class="secondary_name">';
                        if (row.concentration) {
                            result += '<span class="tip concentration" title="Концентрация">К</span>';
                        }
                        if (row.ritual) {
                            result += '<span class="tip ritual" title="Ритуал">Р</span>';
                        }
                        result += '<p class="capitalize_text">' + row.school + '</p></div></div>';
                        return result;
                    }
                    return data;
                }
            },
            {
                data: 'englishName',
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
                data: "charLevel",
                searchable: false
            },
            {
                data: "charLevel",
                searchable: false
            },
            {
                data: "charLevel",
                searchable: false
            },
            {
                data: "charLevel",
                searchable: false
            },
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
            spellScrollEventHeight = document.getElementById('content_block').offsetHeight - 1500;
            var simpleBar = SimpleBar.instances.get(document.getElementById('info_wrapper'));
            simpleBar.getScrollElement().addEventListener('scroll', function (event) {
                //alert(spellScrollEventHeight);
                if (simpleBar.getScrollElement().scrollTop > spellScrollEventHeight) {
                    spellTable.page.loadMore();
                    simpleBar.recalculate();
                    spellScrollEventHeight += 750;

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
            } else if (data.setting) {
                $(row).addClass('setting_source');
                if (!isSettingsShowed('spells')) {
                    $(row).addClass('hide_block');
                }
            }
        },
    });
    $('#spells tbody').on('click', 'tr', function (e) {
        let tr = $(this).closest('tr');
        var row = $('#spells').DataTable().row(tr);
        $.get('/spells/fragment/' + row.data().id)
         .done(function (spellData) {
             $.magnificPopup.open({
                 items: {
                     src: '<div class="dnd5-popup-block"><div class="header"><h4>' + row.data().name + '</h4></div><div class="wrapper" data-simplebar><p>' + spellData + '</p></div></div>',
                     type: 'inline'
                 },
             });
         });
    });
});

function selectSpell(data) {
    const url = '/spells/fragment/' + data.id;
    $("#content_block").load(url);
}

var timer, delay = 300;
$('#search_spell').bind('keydown blur change', function (e) {
    var _this = $(this);
    clearTimeout(timer);
    timer = setTimeout(function () {
        if ($('#search_spell').val()) {
            $('#spell_text_clear').show();
        } else {
            $('#spell_text_clear').hide();
        }
        $('#spells').DataTable().tables().search($('#search_spell').val()).draw();
    }, delay);
});
$('#btn_spell_filters').on('click', function () {
    $('#searchPanesSpell').toggleClass('hide_block');

    $('#btn_spell_filters').toggleClass('open');
});
$('#spell_text_clear').on('click', function () {
    $('#search_spell').val('');
    $('#spells').DataTable().tables().search($(this).val()).draw();
    $('#spell_text_clear').hide();
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
    setFiltered();
});
$('#level_clear_btn').on('click', function () {
    $('#level_clear_btn').addClass('hide_block');
    $('.level_checkbox').prop('checked', false);
    $('#spells').DataTable().column(0).search("", true, false, false).draw();
    setFiltered();
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
    setFiltered();
});
$('#school_clear_btn').on('click', function () {
    $('#school_clear_btn').addClass('hide_block');
    $('.school_checkbox').prop('checked', false);
    $('#spells').DataTable().column(1).search("", true, false, false).draw();
    setFiltered();
});
$('.char_level_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="char_level"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#spells').DataTable().column(10).search(properties, true, false, false).draw();
    if (properties) {
        $('#char_level_clear_btn').removeClass('hide_block');
    } else {
        $('#char_level_clear_btn').addClass('hide_block');
    }
    setFiltered();
});
$('#char_level_clear_btn').on('click', function () {
    $('#char_level_clear_btn').addClass('hide_block');
    $('.char_level_checkbox').prop('checked', false);
    $('#spells').DataTable().column(10).search("", true, false, false).draw();
    setFiltered();
});
