$(document).ready(function () {
    var scrollEventHeight = 0;
    var rowSelectIndex = 0;
    var table = $('#items').DataTable({
        ajax: '/data/items',
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
            {
                data: 'bookshort',
                searchable: false,
            },
        ],
        columnDefs: [
            {
                "targets": [ 1, 2, 3 ],
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
        },
        initComplete: function (settings, json) {
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
        createdRow: function (row, data, dataIndex) {
            if (data.homebrew) {
                $(row).addClass('custom_source');
                if (!isHomebrewShowed('items')) {
                    $(row).addClass('hide_block');
                }
            } else if (data.setting) {
                $(row).addClass('setting_source');
                if (!isSettingsShowed('items')) {
                    $(row).addClass('hide_block');
                }
            }
        },
        drawCallback: function (settings) {
            addEventListeners();

            if (rowSelectIndex === 0 && selectedItem === null) {
                if (!$('#list_page_two_block').hasClass('block_information')) {
                    return;
                }
                $('#items tbody tr:eq(' + rowSelectIndex + ')').click();
            }
            if (selectedItem) {
                selectItem(selectedItem);
                var rowIndexes = [];
                table.rows(function (idx, data, node) {
                    if (data.id === selectedItem.id) {
                        rowIndexes.push(idx);
                    }
                    return false;
                });
                rowSelectIndex = rowIndexes[0];
            }
            table.row(':eq(' + rowSelectIndex + ')', { page: 'current' }).select();
        }
    });
    $('#items tbody').on('mouseup', 'tr', function (e) {
        if (e.which == 2) {
            var tr = $(this).closest('tr');
            var row = table.row(tr);
            rowSelectIndex = row.index();
            var data = row.data();
            window.open('/items/' + data.englishName.split(' ').join('_'));
        }
    });
    $('#items tbody').on('click', 'tr', function (e) {
        if (!document.getElementById('list_page_two_block').classList.contains('block_information')) {
            document.getElementById('list_page_two_block').classList.add('block_information');
        }
        var tr = $(this).closest('tr');
        var table = $('#items').DataTable();
        var row = table.row(tr);
        var data = row.data();
        if ((window.navigator.userAgent.indexOf("Mac") !== -1 && e.metaKey) || e.ctrlKey) {
            window.open('/items/' + data.englishName.split(' ').join('_'));
        }
        rowSelectIndex = row.index();
        selectItem(data);
        selectedItem = data;
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
    const element = $('#items');
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

function selectItem(data) {
    document.getElementById('item_name').innerHTML = data.name;

    document.title = data.name + ' (' + data.englishName + ')' + ' | Снаряжение D&D 5e';
    history.pushState('data to be passed', '', '/items/' + data.englishName.split(' ').join('_'));
    var url = '/items/fragment/' + data.id;
    $("#content_block").load(url);
}

$('#text_clear').on('click', function () {
    $('#search').val('');
    const table = $('#items').DataTable();
    table.tables().search($(this).val()).draw();
    $('#text_clear').hide();
});
$('#btn_close').on('click', function () {
    $('#items').dataTable().api().rows().deselect();
});

function closeHandler() {
    document.getElementById('list_page_two_block').classList.remove('block_information');

    $.magnificPopup.close();

    history.pushState('data to be passed', '', '/items/');
}

$('#btn_filters').on('click', function () {
    $('#searchPanes').toggleClass('hide_block');
});
$('.category_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="category"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#items').DataTable().column(2).search(properties, true, false, false).draw();
    if (properties) {
        $('#category_clear_btn').removeClass('hide_block');
    } else {
        $('#category_clear_btn').addClass('hide_block');
    }

    saveFilter('items');
});
$('#category_clear_btn').on('click', function () {
    $('#category_clear_btn').addClass('hide_block');
    $('.category_checkbox').prop('checked', false);
    $('#items').DataTable().column(2).search("", true, false, false).draw();

    saveFilter('items');
});
$('.book_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="book"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#items').DataTable().column(3).search(properties, true, false, false).draw();
    if (properties) {
        $('#book_clear_btn').removeClass('hide_block');
    } else {
        $('#book_clear_btn').addClass('hide_block');
    }
    saveFilter('items');
});
$('#book_clear_btn').on('click', function () {
    $('#book_clear_btn').addClass('hide_block');
    $('.book_checkbox').prop('checked', true);
    $('#items').DataTable().column(3).search("", true, false, false).draw();
    saveFilter('items');
});
