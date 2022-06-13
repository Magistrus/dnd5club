$(document).ready(function () {
    var scrollEventHeight = 0;
    var rowSelectIndex = 0;
    var table = $('#screens').DataTable({
        ajax: '/data/screens',
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
                        var result = '<div class="wrapper"><div class="content"><h3 class="row_name"><span class="name">' + row.name;
                        result += '</span></h3><div class="two_row"><ename>' + row.englishName + '</ename></dv></dv></dv>';
                        return result;
                    }
                    return data;
                }
            },
            {
                data: 'englishName',
            },
            {
                data: 'ordering',
                searchable: false,
            },
            {
                data: 'altName',
            },
            {
                data: 'category',
            },
        ],
        columnDefs: [
            {
                "targets": [ 1, 2 ,3, 4 ],
                "visible": false
            },
        ],
        order: [ [ 2, 'asc' ] ],
        language: {
            processing: "Загрузка...",
            searchPlaceholder: "Поиск ",
            search: "_INPUT_",
            lengthMenu: "Показывать _MENU_ записей на странице",
            zeroRecords: "Ничего не найдено",
            info: "Показано _TOTAL_",
            infoEmpty: "Нет доступных записей",
            infoFiltered: "из _MAX_",
            loadingRecords: "Загрузка..."
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
        drawCallback: function (settings) {
            addEventListeners();

            if (selectedScreen) {
                selectScreen(selectedScreen);
                document.getElementById('list_page_two_block').classList.add('block_information');
                var rowIndexes = [];
                table.rows(function (idx, data, node) {
                    if (data.id === selectedScreen.id) {
                        rowIndexes.push(idx);
                    }
                    return false;
                });
                rowSelectIndex = rowIndexes[0];
                $('#screens tbody tr:eq(' + rowSelectIndex + ')').click();
                table.row(':eq(' + rowSelectIndex + ')', { page: 'current' }).select();
            }
        }
    });

    $('#screens tbody').on('click', 'tr', function (e) {
        if (!document.getElementById('list_page_two_block').classList.contains('block_information')) {
            document.getElementById('list_page_two_block').classList.add('block_information');
        }
        var tr = $(this).closest('tr');
        var table = $('#screens').DataTable();
        var row = table.row(tr);
        var data = row.data();
        if (data === undefined) {
            return;
        }
        selectScreen(data);

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
    const element = $('#screens');
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

function selectScreen(data) {
    selectedScreen = data;
    $('#screen_name').html(data.name);
    $('#english_name').html(data.englishName);

    document.title = data.name + ' (' + data.englishName + ')' + ' | Киниги D&D 5e';
    history.pushState('data to be passed', '', '/screens/' + data.englishName.split(' ').join('_'));
    if (!data.parent) {
        var url = '/screens/fragment/' + data.id;
        $("#content_block").load(url, function () {
            $('.open-popup-link').magnificPopup({
                type: 'inline',
                midClick: true
            });
        });
    } else {
        $("#content_block").load('/screens/fragmentone/' + data.id);
    }
}

$('#search').on('keyup click', function () {
    selectedScreen = null;
    if ($(this).val()) {
        $('#text_clear').show();
    } else {
        $('#text_clear').hide();
    }
    const table = $('#screens').DataTable();
    table.tables().search($(this).val()).draw();
});
$('#text_clear').on('click', function () {
    $('#search').val('');
    selectedScreen = null;
    const table = $('#screens').DataTable();
    table.tables().search($(this).val()).draw();
    $('#text_clear').hide();
});
$('#btn_close').on('click', function () {
    if (window.innerWidth < 1200) {
        $('#screens').dataTable().api().rows().deselect();

        return;
    }

    closeHandler();
});

function closeHandler() {
    document.getElementById('list_page_two_block').classList.remove('block_information');
    selectedScreen = null;

    $.magnificPopup.close();

    history.pushState('data to be passed', '', '/screens/');
}
