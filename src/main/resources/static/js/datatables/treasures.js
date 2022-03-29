$(document).ready(function () {
    var scrollEventHeight = 0;
    var rowSelectIndex = 0;
    var table = $('#treasures').DataTable({
        ajax: '/data/treasures',
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
                        var result = '<div class="content"><h3 class="row_name"><span>' + row.name;
                        result += '</span><div class="content_description"><div class="secondary_name"><span class="tip" title="' + row.book + '">' + row.bookshort + '</span></div>';
                        result += '<div class="secondary_name s2"><span class="tip excretion" title="Стоимость">' + row.cost + ' зм.</span></div></div></h3>';
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
            getSearchColumn('category', 'treasures'),
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
        },
        initComplete: function (settings, json) {
            restoreFilter('treasures');

            scrollEventHeight = document.getElementById('scroll_load_simplebar').offsetHeight - 400;
            const simpleBar = new SimpleBar(document.getElementById('scroll_load_simplebar'));
            simpleBar.getScrollElement().addEventListener('scroll', function (event) {
                if (simpleBar.getScrollElement().scrollTop > scrollEventHeight) {
                    table.page.loadMore();
                    simpleBar.recalculate();
                    scrollEventHeight += 750;

                    // addEventListeners(true);
                }
            });
        },
        // drawCallback: function (settings) {
        //     addEventListeners();
        //
        //     if (rowSelectIndex === 0 && selectedTreasure === null) {
        //         if (!$('#list_page_two_block').hasClass('block_information')) {
        //             return;
        //         }
        //         $('#treasures tbody tr:eq(' + rowSelectIndex + ')').click();
        //     }
        //     if (selectedTreasure) {
        //         selectItem(selectedTreasure);
        //         var rowIndexes = [];
        //         table.rows(function (idx, data, node) {
        //             if (data.id === selectedTreasure.id) {
        //                 rowIndexes.push(idx);
        //             }
        //             return false;
        //         });
        //         rowSelectIndex = rowIndexes[0];
        //     }
        //     table.row(':eq(' + rowSelectIndex + ')', { page: 'current' }).select();
        // }
    });
    // $('#treasures tbody').on('click', 'tr', function (e) {
    //     if (!document.getElementById('list_page_two_block').classList.contains('block_information')) {
    //         document.getElementById('list_page_two_block').classList.add('block_information');
    //     }
    //     var tr = $(this).closest('tr');
    //     var table = $('#treasures').DataTable();
    //     var row = table.row(tr);
    //     var data = row.data();
    //     rowSelectIndex = row.index();
    //     selectItem(data);
    //     selectedItem = null;
    // });
    $('#search').on('keyup click', function () {
        if ($(this).val()) {
            $('#text_clear').show();
        } else {
            $('#text_clear').hide();
        }
        table.tables().search($(this).val()).draw();
    });
});

// function addEventListeners(force = false) {
//     $(document).ready(function () {
//         onDeselectListener();
//     });
//
//     $(window).resize(function () {
//         onDeselectListener();
//     });
//
//     if (force) {
//         onDeselectListener();
//     }
// }
//
// function onDeselectListener() {
//     const element = $('#treasures');
//     const table = element.dataTable().api();
//
//     if (window.innerWidth < 1200 && !element.hasClass('has-deselect-handler')) {
//         table.on('deselect.dt', closeHandler);
//         element.addClass('has-deselect-handler');
//
//         return
//     }
//
//     if (window.innerWidth >= 1200) {
//         table.off('deselect.dt', closeHandler);
//         element.removeClass('has-deselect-handler');
//     }
// }

// function selectItem(data) {
//     $('#item_name').text(data.name);
//     $('#english_name').html(data.englishName);

//     $('#type').text(data.type);
//     $('#cost').text(data.cost + ' зм.');
//     var source = '<span class="tip" title="' + data.book + '">' + data.bookshort + '</span>';
//     $('#source').html(source);
//     document.title = data.name + ' (' + data.englishName + ')' + ' | Драгоценности D&D 5e';
//     history.pushState('data to be passed', '', '/treasures/' + data.englishName.split(' ').join('_'));
//     var url = '/treasures/fragment/' + data.id;
//     $("#content_block").load(url);
// }

$('#text_clear').on('click', function () {
    $('#search').val('');
    const table = $('#treasures').DataTable();
    table.tables().search($(this).val()).draw();
    $('#text_clear').hide();
});

// $('#btn_close').on('click', function () {
//     $('#treasures').dataTable().api().rows().deselect();
// });

// function closeHandler() {
//     document.getElementById('list_page_two_block').classList.remove('block_information');
//
//     $.magnificPopup.close();
//
//     history.pushState('data to be passed', '', '/treasures/');
// }

$('#btn_filters').on('click', function () {
    $('#searchPanes').toggleClass('hide_block');

    $('#btn_filters').toggleClass('open');
});
$('.category_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="category"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#treasures').DataTable().column(2).search(properties, true, false, false).draw();
    if (properties) {
        $('#category_clear_btn').removeClass('hide_block');
    } else {
        $('#category_clear_btn').addClass('hide_block');
    }

    saveFilter('treasures');
});
$('#category_clear_btn').on('click', function () {
    $('#category_clear_btn').addClass('hide_block');
    $('.category_checkbox').prop('checked', false);
    $('#treasures').DataTable().column(2).search("", true, false, false).draw();

    saveFilter('treasures');
});


