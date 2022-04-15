$(document).ready(function () {
    var scrollEventHeight = 0;
    var rowSelectIndex = 0;
    var table = $('#options').DataTable({
        ajax: '/data/options?optionType=' + optionType,
        dom: 't',
        serverSide: true,
        deferRender: true,
        iDisplayLength: 80,
        scrollCollapse: true,
        select: true,
        select: {
            style: 'single',
            toggleable: false
        },
        columns: [
            {
                data: "name",
                render: function (data, type, row) {
                    if (type === 'display') {
                        var result = '<div class="content"><h3 class="row_name"><span>' + row.name;
                        result += '</span> <ename>[' + row.englishName + ']</ename></h3>';
                        if (row.prerequisite !== 'Нет') {
                            result += '<div class="secondary_name">' + row.prerequisite + '</div>';
                        }
                        result += '</div>';
                        return result;
                    }
                    return data;
                }
            },
            {
                data: 'englishName',
            },
            {
                data: 'type',
                searchable: false
            },
            {
                data: 'prerequisite',
                searchable: false
            },
            {
                data: 'level',
                searchable: false
            },
            {
                data: 'bookshort',
                searchable: false,
            },
        ],
        columnDefs: [
            {
                "targets": [ 1, 2, 3, 4, 5 ],
                "visible": false
            },
        ],
        buttons: [
            {} ],
        order: [ [ 0, 'asc' ] ],
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
        ordering: true,
        initComplete: function (settings, json) {
            scrollEventHeight = $('#scroll_load_simplebar')[0].offsetHeight - 800;
            var simpleBar = SimpleBar.instances.get(document.getElementById('info_wrapper'));
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
                if (!isHomebrewShowed('options')) {
                    $(row).addClass('hide_block');
                }
            }
        },
    });
    $('#options tbody').on('click', 'tr', function (e) {
        var tr = $(this).closest('tr');
        var table = $('#options').DataTable();
        var row = table.row(tr);
        $.get('/options/fragment/' + row.data().id)
         .done(function (spellData) {
             $.magnificPopup.open({
                 items: {
                     src: '<div class="dnd5-popup-block"><div class="header"><h4>' + row.data().name + '</h4></div><div class="wrapper" data-simplebar><p>' + spellData + '</p></div></div>',
                     type: 'inline'
                 },
             });
         });
    });
    $('#option_search').on('keyup click', function () {
        if ($(this).val()) {
            $('#option_text_clear').show();
        } else {
            $('#option_text_clear').hide();
        }
        table.tables().search($(this).val()).draw();
    });
});
$('#option_btn_filters').on('click', function () {
    $('#option_search_panes').toggleClass('hide_block');

    $('#option_btn_filters').toggleClass('open');
});
$('#option_text_clear').on('click', function () {
    $('#option_search').val('');
    const table = $('#options').DataTable();
    table.tables().search($(this).val()).draw();
    $('#option_text_clear').hide();
});
$('.prerequsite_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="prerequsite"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#options').DataTable().column(3).search(properties, true, false, false).draw();
    if (properties) {
        $('#prerequsite_clear_btn').removeClass('hide_block');
    } else {
        $('#prerequsite_clear_btn').addClass('hide_block');
    }
    setFiltered();
});
$('#prerequsite_clear_btn').on('click', function () {
    $('#prerequsite_clear_btn').addClass('hide_block');
    $('.prerequsite_checkbox').prop('checked', false);
    $('#options').DataTable().column(3).search("", true, false, false).draw();
    setFiltered();
});
$('.level_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="level"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#options').DataTable().column(4).search(properties, true, false, false).draw();
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
    $('#options').DataTable().column(4).search("", true, false, false).draw();
    setFiltered();
});


