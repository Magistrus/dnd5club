$(document).ready(function () {
    var scrollEventHeight = 0;
    var rowSelectIndex = 0;
    var npc;
    var table = $('#creatures').DataTable({
        ajax: {
            url: '/data/bestiary',
            data: function (d) {
                let npcValue = localStorage.getItem('npc')
                npc = npcValue == null ? 'false' : npcValue;
                d.npc = npc;
            }
        },
        dom: 't',
        serverSide: true,
        iDisplayLength: 80,
        scrollCollapse: true,
        select: true,
        select: {
            style: 'single',
            toggleable: false
        },
        columns: [
            {
                data: 'exp',
            },
            {
                data: "name",
                render: function (data, type, row) {
                    if (type === 'display') {
                        var result = '<div class="info_block">' + row.cr + '</div>';
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
                data: 'cr',
                searchable: false
            },
            {
                data: 'type',
                searchable: false
            },
            {
                data: 'size',
                searchable: false
            },
            {
                data: 'tag',
                searchable: false
            },
            {
                data: 'habitates',
                searchable: false
            },
            {
                data: 'altName',
            },
            {
                data: 'bookshort',
                searchable: false,
            },
        ],
        searchCols: [
            null,
            null,
            null,
            getSearchColumn('cr', 'creatures'),
            getSearchColumn('type', 'creatures'),
            getSearchColumn('size', 'creatures'),
            getSearchColumn('tag', 'creatures'),
            getSearchColumn('habitate', 'creatures'),
            null,
            getSearchColumn('book', 'creatures'),
        ],
        columnDefs: [
            {
                "targets": [ 0, 2, 3, 4, 5, 6, 7, 8, 9 ],
                "visible": false
            },
            {
                "targets": [ 5 ],
                "visible": false,
                searchPanes: {
                    dtOpts: {
                        order: []
                    }
                }
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
            restoreFilter('creatures')

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
        drawCallback: function (settings) {
            addEventListeners();

            if (rowSelectIndex === 0 && selectedCreature === null) {
                if (!$('#list_page_two_block').hasClass('block_information') && selectedCreature === null) {
                    return;
                }
            }
            if (selectedCreature) {
                if (window.innerWidth >= 1200) {
                    selectCreature(selectedCreature);
                }
                var rowIndexes = [];
                table.rows(function (idx, data, node) {
                    if (data.id === selectedCreature.id) {
                        rowIndexes.push(idx);
                    }
                    return false;
                });
                rowSelectIndex = rowIndexes[0];
            }

            if (window.innerWidth >= 1200) {
                $('#creatures tbody tr:eq(' + rowSelectIndex + ')').click();
                table.row(':eq(' + rowSelectIndex + ')', { page: 'current' }).select();
            }
        }
    });
    $('#creatures tbody').on('mouseup', 'tr', function (e) {
        if (e.which == 2) {
            e.preventScrolling();
            var tr = $(this).closest('tr');
            var table = $('#creatures').DataTable();
            var row = table.row(tr);
            rowSelectIndex = row.index();
            var data = row.data();
            window.open('/bestiary/' + data.englishName.split(' ').join('_'));
        }
    });
    $('#creatures tbody').on('click', 'tr', function (e) {
        var tr = $(this).closest('tr');
        var table = $('#creatures').DataTable();
        var row = table.row(tr);
        rowSelectIndex = row.index();
        var data = row.data();
        if ((window.navigator.userAgent.indexOf("Mac") !== -1 && e.metaKey) || e.ctrlKey) {
            window.open('/bestiary/' + data.englishName.split(' ').join('_'));
        }
        selectCreature(data);
        selectedCreature = data;
    });
    $('#search').on('keyup click', function () {
        if ($(this).val()) {
            $('#text_clear').show();
        } else {
            $('#text_clear').hide();
        }
        selectedCreature = null;
        table.tables().search($(this).val()).draw();
        rowSelectIndex = 0;
    });
    $('#btn_filters').on('click', function () {
        $('#searchPanes').toggleClass('hide_block');

        $('#btn_filters').toggleClass('open');
    });
    $('#npc').prop('checked', localStorage.getItem('npc') == 'true' ? true : false);
});
$('#text_clear').on('click', function () {
    $('#search').val('');
    let table = $('#creatures').DataTable();
    selectedCreature = null;
    table.tables().search($(this).val()).draw();
    $('#text_clear').hide();
});

$('.book_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="book"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#creatures').DataTable().column(9).search(properties, true, false, false).draw();
    if (properties) {
        $('#book_clear_btn').removeClass('hide_block');
    } else {
        $('#book_clear_btn').addClass('hide_block');
    }
    saveFilter('creatures');
});
$('#book_clear_btn').on('click', function () {
    $('#book_clear_btn').addClass('hide_block');
    $('.book_checkbox').prop('checked', true);
    $('#creatures').DataTable().column(9).search("", true, false, false).draw();
    saveFilter('creatures');
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
    const element = $('#creatures');
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

function selectCreature(data) {
    if (!data) {
        return;
    }
    $('#statblock').addClass('active');
    $('#description').removeClass('active');
    $('#creature_name').html(data.name);
    $('#right_block').attr('data-creature-type', data.englishType);
    document.title = data.name + ' (' + data.englishName + ')' + ' | Бестиарий D&D 5e';
    history.pushState('data to be passed', '', '/bestiary/' + data.englishName.split(' ').join('_'));
    var url = '/bestiary/fragment/' + data.id;
    $("#content_block").load(url, function () {
        if (!document.getElementById('list_page_two_block').classList.contains('block_information')) {
            document.getElementById('list_page_two_block').classList.add('block_information');
        }
        $('.image-container').magnificPopup({
            delegate: 'a',
            type: 'image',
            gallery: {
                enabled: true
            }
        });
    });
}

function selectDescription(id) {
    var url = '/bestiary/description/' + id;
    $("#content_block").load(url);
}

$('#btn_close').on('click', function () {
    $('#creatures').dataTable().api().rows().deselect();
});

function closeHandler() {
    document.getElementById('list_page_two_block').classList.remove('block_information');
    localStorage.removeItem('selected_creature');

    $.magnificPopup.close();

    history.pushState('data to be passed', 'Бестиарий', '/bestiary/');
}

$('#statblock').on('click', function () {
    let table = $('#creatures').DataTable();
    if (selectedCreature === null) {
        selectCreature(table.row({ selected: true }).data());
    } else {
        selectCreature(selectedCreature);
    }
});
$('#description').on('click', function () {
    $('#description').addClass('active');
    $('#statblock').removeClass('active');
    if (selectedCreature === null) {
        let table = $('#creatures').DataTable();
        selectDescription(table.row({ selected: true }).data().id);
    } else {
        selectDescription(selectedCreature.id);
    }
});
$('.cr_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="cr"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#creatures').DataTable().column(3).search(properties, true, false, false).draw();
    if (properties) {
        $('#cr_clear_btn').removeClass('hide_block');
    } else {
        $('#cr_clear_btn').addClass('hide_block');
    }

    saveFilter('creatures');
});
$('#cr_clear_btn').on('click', function () {
    $('#cr_clear_btn').addClass('hide_block');
    $('.cr_checkbox').prop('checked', false);
    $('#creatures').DataTable().column(3).search("", true, false, false).draw();

    saveFilter('creatures');
})
$('.type_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="type"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#creatures').DataTable().column(4).search(properties, true, false, false).draw();
    if (properties) {
        $('#type_clear_btn').removeClass('hide_block');
    } else {
        $('#type_clear_btn').addClass('hide_block');
    }

    saveFilter('creatures');
});
$('#type_clear_btn').on('click', function () {
    $('#type_clear_btn').addClass('hide_block');
    $('.type_checkbox').prop('checked', false);
    $('#creatures').DataTable().column(4).search("", true, false, false).draw();

    saveFilter('creatures');
})
$('.size_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="size"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#creatures').DataTable().column(5).search(properties, true, false, false).draw();
    if (properties) {
        $('#size_clear_btn').removeClass('hide_block');
    } else {
        $('#size_clear_btn').addClass('hide_block');
    }

    saveFilter('creatures');
});
$('#size_clear_btn').on('click', function () {
    $('#size_clear_btn').addClass('hide_block');
    $('.size_checkbox').prop('checked', false);
    $('#creatures').DataTable().column(5).search("", true, false, false).draw();

    saveFilter('creatures');
})
$('.tag_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="tag"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#creatures').DataTable().column(6).search(properties, true, false, false).draw();
    if (properties) {
        $('#tag_clear_btn').removeClass('hide_block');
    } else {
        $('#tag_clear_btn').addClass('hide_block');
    }

    saveFilter('creatures');
});
$('#tag_clear_btn').on('click', function () {
    $('#tag_clear_btn').addClass('hide_block');
    $('.tag_checkbox').prop('checked', false);
    $('#creatures').DataTable().column(6).search("", true, false, false).draw();

    saveFilter('creatures');
})
$('.habitate_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="habitate"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#creatures').DataTable().column(7).search(properties, true, false, false).draw();
    if (properties) {
        $('#habitate_clear_btn').removeClass('hide_block');
    } else {
        $('#habitate_clear_btn').addClass('hide_block');
    }

    saveFilter('creatures');
});
$('#npc').on('change', function (e) {
    npc = $('#npc').is(':checked')
    localStorage.setItem('npc', npc);
    $('#creatures').DataTable().ajax.reload();
});
$('#habitate_clear_btn').on('click', function () {
    $(this).addClass('hide_block');
    $('.habitate_checkbox').prop('checked', false);
    $('#creatures').DataTable().column(7).search("", true, false, false).draw();

    saveFilter('creatures');
})

$('#btn_export_fvtt').on('click', function () {
    window.open('/creature/json/' + selectedCreature.id, '_self');
});
$('#edit_beast').on('click', function () {
    window.open('/admin/bestiary/' + selectedCreature.id, '_self');
});
