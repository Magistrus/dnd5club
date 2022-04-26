$(document).ready(function () {
    $('#charachter_item_menu').addClass('showMenu');
    var scrollEventHeight = 0;
    var rowSelectIndex = 0;
    var table = $('#traits').DataTable({
        ajax: '/data/traits',
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
                        var result = '<div class="content"><h3 class="row_name"><span>' + row.name;
                        result += '</span> <ename>[' + row.englishName + ']</ename></h3>';
                        result += '<div class="content_description"><div class="secondary_name s1">' + row.requirement + '</div></div></div>';
                        return result;
                    }
                    return data;
                }
            },
            {
                data: 'englishName',
            },
            {
                data: 'abilities',
                searchable: false
            },
            {
                data: 'skills',
                searchable: false
            },
            {
                data: 'requirement',
                searchable: false
            },
            {
                data: 'bookshort',
                searchable: false,
            },
        ],
        searchCols: [
            null,
            null,
            getSearchColumn('ability', 'traits'),
            null,
            getSearchColumn('prerequisite', 'traits'),
            getSearchColumn('book', 'traits'),
        ],
        columnDefs: [
            {
                "targets": [ 1, 2, 3, 4, 5 ],
                "visible": false
            },
        ],
        buttons: [ {} ],
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
            restoreFilter('traits');

            scrollEventHeight = document.getElementById('scroll_load_simplebar').offsetHeight - 500;
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

            if (window.innerWidth >= 1200) {
                $('#list_page_two_block').addClass('block_information');
            }
            
            if (rowSelectIndex === 0 && selectedTrait === null) {
                if (!$('#list_page_two_block').hasClass('block_information')) {
                    return;
                }
            }
            if (selectedTrait) {
                selectTrait(selectedTrait);
                var rowIndexes = [];
                table.rows(function (idx, data, node) {
                    if (data.id === selectedTrait.id) {
                        rowIndexes.push(idx);
                    }
                    return false;
                });
                rowSelectIndex = rowIndexes[0];
            }
            $('#traits tbody tr:eq(' + rowSelectIndex + ')').click();
            table.row(':eq(' + rowSelectIndex + ')', { page: 'current' }).select();
        },
        createdRow: function (row, data, dataIndex) {
            if (data.homebrew) {
                $(row).addClass('custom_source');
                if (!isHomebrewShowed('traits')) {
                    $(row).addClass('hide_block');
                }
            }
        },
    });
    $('#traits tbody').on('mouseup', 'tr', function (e) {
        if (e.which == 2) {
            var tr = $(this).closest('tr');
            var row = table.row(tr);
            rowSelectIndex = row.index();
            var data = row.data();
            window.open('/traits/' + data.englishName.split(' ').join('_'));
        }
    });
    $('#traits tbody').on('click', 'tr', function (e) {
        if (!document.getElementById('list_page_two_block').classList.contains('block_information')) {
            document.getElementById('list_page_two_block').classList.add('block_information');
        }
        var tr = $(this).closest('tr');
        var table = $('#traits').DataTable();
        var row = table.row(tr);
        var data = row.data();
        if ((window.navigator.userAgent.indexOf("Mac") !== -1 && e.metaKey) || e.ctrlKey) {
            window.open('/traits/' + data.englishName.split(' ').join('_'));
        }
        rowSelectIndex = row.index();
        selectTrait(data);
    });
    $('#search').on('keyup click', function () {
        if ($('#search').val()) {
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
    const element = $('#traits');
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

function selectTrait(data) {
    $('#trait_name').text(data.name);
    $('#english_name').html(data.englishName);

    document.title = data.name + ' (' + data.englishName + ')' + ' | Черты D&D 5e';
    history.pushState('data to be passed', '', '/traits/' + data.englishName.split(' ').join('_'));
    var url = '/traits/fragment/' + data.id;
    $("#content_block").load(url);
    selectedTrait = data;
}

$('#text_clear').on('click', function () {
    $('#search').val('');
    const table = $('#treasures').DataTable();
    table.tables().search($(this).val()).draw();
    $('#text_clear').hide();
});
$('#btn_close').on('click', function () {
    $('#traits').dataTable().api().rows().deselect();
});

function closeHandler() {
    document.getElementById('list_page_two_block').classList.remove('block_information');
    selectedTrait = null;

    $.magnificPopup.close();

    history.pushState('data to be passed', '', '/traits/');
}

$('#btn_filters').on('click', function () {
    $('#searchPanes').toggleClass('hide_block');

    $('#btn_filters').toggleClass('open');
});
$('.ability_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="ability"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#traits').DataTable().column(2).search(properties, true, false, false).draw();
    if (properties) {
        $('#ability_clear_btn').removeClass('hide_block');
    } else {
        $('#ability_clear_btn').addClass('hide_block');
    }
    saveFilter('traits');
});
$('#ability_clear_btn').on('click', function () {
    $('#skill_clear_btn').addClass('hide_block');
    $('.skill_checkbox').prop('checked', false);
    $('#traits').DataTable().column(2).search("", true, false, false).draw();
    saveFilter('traits');
});
$('.skill_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="skill"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#traits').DataTable().column(3).search(properties, true, false, false).draw();
    if (properties) {
        $('#skill_clear_btn').removeClass('hide_block');
    } else {
        $('#skill_clear_btn').addClass('hide_block');
    }
    saveFilter('traits');
});
$('#skill_clear_btn').on('click', function () {
    $('#skill_clear_btn').addClass('hide_block');
    $('.skill_checkbox').prop('checked', false);
    $('#traits').DataTable().column(3).search("", true, false, false).draw();
    saveFilter('traits');
});
$('.prerequisite_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="prerequisite"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#traits').DataTable().column(4).search(properties, true, false, false).draw();
    if (properties) {
        $('#prerequisite_clear_btn').removeClass('hide_block');
    } else {
        $('#prerequisite_clear_btn').addClass('hide_block');
    }
    saveFilter('traits');
});
$('#prerequisite_clear_btn').on('click', function () {
    $('#prerequisite_clear_btn').addClass('hide_block');
    $('.prerequisite_checkbox').prop('checked', false);
    $('#traits').DataTable().column(5).search("", true, false, false).draw();
    saveFilter('traits');
});
$('.book_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="book"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#traits').DataTable().column(5).search(properties, true, false, false).draw();
    if (properties) {
        $('#book_clear_btn').removeClass('hide_block');
    } else {
        $('#book_clear_btn').addClass('hide_block');
    }
    saveFilter('traits');
});
$('#book_clear_btn').on('click', function () {
    $('#book_clear_btn').addClass('hide_block');
    $('.book_checkbox').prop('checked', true);
    $('#traits').DataTable().column(5).search("", true, false, false).draw();
    saveFilter('traits');
});

