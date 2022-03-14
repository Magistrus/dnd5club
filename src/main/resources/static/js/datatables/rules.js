$(document).ready(function () {
    var scrollEventHeight = 0;
    var rowSelectIndex = 0;
    var table = $('#rules').DataTable({
        ajax: '/data/rules',
        dom: 'tiS',
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
                        var result = '<h3 class="row_name"><span>' + row.name;
                        result += '</span><span>[' + row.englishName + ']</span></h3>';
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
                searchable: false,
            },
        ],
        searchCols: [
            null,
            null,
            getSearchColumn('category', 'rules'),
        ],
        columnDefs: [
            {
                "targets": [ 1, 2 ],
                "visible": false
            },
        ],
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
            searchPanes: {
                collapseMessage: 'Свернуть все',
                showMessage: 'Развернуть все',
                clearMessage: 'Сбросить фильтры'
            }
        },
        initComplete: function (settings, json) {
            restoreFilter('rules');

            scrollEventHeight = document.getElementById('scroll_load_simplebar').offsetHeight - 400;
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
        drawCallback: function (settings) {
            addEventListeners();

            if (rowSelectIndex === 0 && selectedRule === null) {
                if (!$('#list_page_two_block').hasClass('block_information') && selectedRule === null) {
                    return;
                }
                $('#rules tbody tr:eq(' + rowSelectIndex + ')').click()
            }
            if (selectedRule) {
                selectRule(selectedRule);
                var rowIndexes = [];
                table.rows(function (idx, data, node) {
                    if (data.id === selectedRule.id) {
                        rowIndexes.push(idx);
                    }
                    return false;
                });
                rowSelectIndex = rowIndexes[0];
            }
            table.row(':eq(' + rowSelectIndex + ')', { page: 'current' }).select();
        }
    });
    $('#rules tbody').on('mouseup', 'tr', function (e) {
        if (e.which == 2) {
            var tr = $(this).closest('tr');
            var row = table.row(tr);
            rowSelectIndex = row.index();
            var data = row.data();
            window.open('/rules/' + data.englishName.split(' ').join('_'));
        }
    });
    $('#rules tbody').on('click', 'tr', function (e) {
        if (!document.getElementById('list_page_two_block').classList.contains('block_information')) {
            document.getElementById('list_page_two_block').classList.add('block_information');
        }
        var tr = $(this).closest('tr');
        var table = $('#rules').DataTable();
        var row = table.row(tr);
        rowSelectIndex = row.index();
        var data = row.data();
        if ((window.navigator.userAgent.indexOf("Mac") !== -1 && e.metaKey) || e.ctrlKey) {
            window.open('/rules/' + data.englishName.split(' ').join('_'));
        }
        selectRule(data);
    });
    $('#search').on('keyup click', function () {
        if ($(this).val()) {
            $('#text_clear').show();
        } else {
            $('#text_clear').hide();
        }
        table.tables().search($(this).val()).draw();
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
    const element = $('#rules');
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

function selectRule(data) {
    $('#rule_name').text(data.name);
    document.title = data.name + ' (' + data.englishName + ')' + ' | Правила и термины D&D 5e';
    history.pushState('data to be passed', '', '/rules/' + data.englishName.split(' ').join('_'));
    var url = '/rules/fragment/' + data.id;
    $("#content_block").load(url);
}

$('#text_clear').on('click', function () {
    $('#search').val('');
    const table = $('#rules').DataTable();
    table.tables().search($(this).val()).draw();
    $('#text_clear').hide();
});
$('#btn_close').on('click', function () {
    $('#rules').dataTable().api().rows().deselect();
});

function closeHandler() {
    document.getElementById('list_page_two_block').classList.remove('block_information');

    $.magnificPopup.close();

    history.pushState('data to be passed', '', '/rules/');
}

$('#btn_filters').on('click', function () {
    $('#searchPanes').toggleClass('hide_block');
});
$('.category_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="category"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#rules').DataTable().column(2).search(properties, true, false, false).draw();
    if (properties) {
        $('#category_clear_btn').removeClass('hide_block');
    } else {
        $('#category_clear_btn').addClass('hide_block');
    }

    saveFilter('rules');
});
$('#category_clear_btn').on('click', function () {
    $('#category_clear_btn').addClass('hide_block');
    $('.category_checkbox').prop('checked', false);
    $('#rules').DataTable().column(2).search("", true, false, false).draw();

    saveFilter('rules');
});


