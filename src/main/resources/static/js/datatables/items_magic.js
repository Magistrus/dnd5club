$(document).ready(function () {
    var scrollEventHeight = 0;
    var rowSelectIndex = 0;
    var table = $('#items_magic').DataTable({
        ajax: '/data/items/magic',
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
                data: 'rarity',
                searchable: false,
            },
            {
                data: "name",
                render: function (data, type, row) {
                    if (type === 'display') {
                        var result = '<div class="tip info_block ' + row.rarityEnglish + '" title="' + row.rarity + '">' + row.shortRarity + '</div>';
                        result += '<div class="content"><h3 class="row_name"><span>' + row.name;
                        result += '</span><span>[' + row.englishName + ']</span></h3>';
                        result += '<div class="secondary_name">' + row.type + '</div></div>';
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
                data: 'attunement',
                searchable: false,
            },
            {
                data: 'attunement',
                searchable: false,
            },
            {
                data: 'bookshort',
                searchable: false,
            },
        ],
        columnDefs: [
            {
                targets: [ 0 ],
                searchPanes: {
                    dtOpts: {
                        order: []
                    }
                }
            },
            {
                targets: [ 0, 2, 3, 4, 5, 6 ],
                visible: false
            },
        ],
        order: [ [ 0, 'asc' ], [ 1, 'asc' ] ],
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
        drawCallback: function (settings) {
            addEventListeners();

            if (rowSelectIndex === 0 && selectedItemMagic === null) {
                if (!$('#list_page_two_block').hasClass('block_information')) {
                    return;
                }
                $('#items_magic tbody tr:eq(' + rowSelectIndex + ')').click();
            }
            if (selectedItemMagic) {
                selectMagicItem(selectedItemMagic);
                var rowIndexes = [];
                table.rows(function (idx, data, node) {
                    if (data.id === selectedItemMagic.id) {
                        rowIndexes.push(idx);
                    }
                    return false;
                });
                rowSelectIndex = rowIndexes[0];
            }
            table.row(':eq(' + rowSelectIndex + ')', { page: 'current' }).select();
        }
    });
    $('#items_magic tbody').on('mouseup', 'tr', function (e) {
        if (e.which == 2) {
            var tr = $(this).closest('tr');
            var row = table.row(tr);
            rowSelectIndex = row.index();
            var data = row.data();
            window.open('/items/magic/' + data.englishName.split(' ').join('_'));
        }
    });
    $('#items_magic tbody').on('click', 'tr', function (e) {
        if (!document.getElementById('list_page_two_block').classList.contains('block_information')) {
            document.getElementById('list_page_two_block').classList.add('block_information');
        }
        var tr = $(this).closest('tr');
        var table = $('#items_magic').DataTable();
        var row = table.row(tr);
        var data = row.data();
        if ((window.navigator.userAgent.indexOf("Mac") !== -1 && e.metaKey) || e.ctrlKey) {
            window.open('/items/magic/' + data.englishName.split(' ').join('_'));
        }
        rowSelectIndex = row.index();
        selectMagicItem(data);
        selectedMagicItem = data;
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
    const element = $('#items_magic');
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

function selectMagicItem(data) {
    $('#item_name').text(data.name);

    document.title = data.name + ' (' + data.englishName + ')' + ' | Магические предметы D&D 5e';
    history.pushState('data to be passed', '', '/items/magic/' + data.englishName.split(' ').join('_'));
    var url = '/items/magic/fragment/' + data.id;
    $("#content_block").load(url, function () {
        $('.image-container').magnificPopup({
            delegate: 'a',
            type: 'image',
            gallery: {
                enabled: true
            }
        });
    });
}

$('#text_clear').on('click', function () {
    $('#search').val('');
    const table = $('#items_magic').DataTable();
    table.tables().search($(this).val()).draw();
    $('#text_clear').hide();
});
$('#btn_close').on('click', function () {
    $('#items_magic').dataTable().api().rows().deselect();
});

function closeHandler() {
    document.getElementById('list_page_two_block').classList.remove('block_information');

    $.magnificPopup.close();

    history.pushState('data to be passed', '', '/items/magic/');
}

function httpGetImage(theUrl) {
    fetch(theUrl).then(function (response) {
        return response.text().then(function (text) {
            $('#item_magic_img').attr('src', text);
        });
    });
}

$('#btn_filters').on('click', function () {
    $('#searchPanes').toggleClass('hide_block');
});
$('.rarity_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="rarity"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#items_magic').DataTable().column(0).search(properties, true, false, false).draw();
    if (properties) {
        $('#rarity_clear_btn').removeClass('hide_block');
    } else {
        $('#rarity_clear_btn').addClass('hide_block');
    }
    setFiltered();
});
$('#rarity_clear_btn').on('click', function () {
    $('#rarity_clear_btn').addClass('hide_block');
    $('.rarity_checkbox').prop('checked', false);
    $('#items_magic').DataTable().column(0).search("", true, false, false).draw();
    setFiltered();
});
$('.type_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="type"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#items_magic').DataTable().column(3).search(properties, true, false, false).draw();
    if (properties) {
        $('#type_clear_btn').removeClass('hide_block');
    } else {
        $('#type_clear_btn').addClass('hide_block');
    }
    setFiltered();
});
$('#type_clear_btn').on('click', function () {
    $('#type_clear_btn').addClass('hide_block');
    $('.type_checkbox').prop('checked', false);
    $('#items_magic').DataTable().column(3).search("", true, false, false).draw();
    setFiltered();
});
$('.customization_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="customization"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#items_magic').DataTable().column(4).search(properties, true, false, false).draw();
    if (properties) {
        $('#customization_clear_btn').removeClass('hide_block');
    } else {
        $('#customization_clear_btn').addClass('hide_block');
    }
    setFiltered();
});
$('#customization_clear_btn').on('click', function () {
    $('#customization_clear_btn').addClass('hide_block');
    $('.customization_checkbox').prop('checked', false);
    $('#items_magic').DataTable().column(4).search("", true, false, false).draw();
    setFiltered();
});
$('.consumable_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="consumable"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#items_magic').DataTable().column(5).search(properties, true, false, false).draw();
    if (properties) {
        $('#consumable_clear_btn').removeClass('hide_block');
    } else {
        $('#consumable_clear_btn').addClass('hide_block');
    }
    setFiltered();
});
$('#consumable_clear_btn').on('click', function () {
    $('#consumable_clear_btn').addClass('hide_block');
    $('.consumable_checkbox').prop('checked', false);
    $('#items_magic').DataTable().column(5).search("", true, false, false).draw();
    setFiltered();
});
$('.book_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="book"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#items_magic').DataTable().column(6).search(properties, true, false, false).draw();
    if (properties) {
        $('#book_clear_btn').removeClass('hide_block');
    } else {
        $('#book_clear_btn').addClass('hide_block');
    }
    saveFilter('items_magic');
});
$('#book_clear_btn').on('click', function () {
    $('#book_clear_btn').addClass('hide_block');
    $('.book_checkbox').prop('checked', true);
    $('#items_magic').DataTable().column(6).search("", true, false, false).draw();
    saveFilter('items_magic');
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
