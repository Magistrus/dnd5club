$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#books').DataTable({
		ajax : '/data/books',
		dom: 'tiS',
		serverSide : true,
        deferRender: true,
		iDisplayLength : 50,
        scrollCollapse: true,
		select: true,
		select: {
			style: 'single'
		},
		columns : [
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="row_name">' + row.name;
					result+='<span>' + row.englishName + '</span></div>';
					return result;
				}
				return data;
			}
		},
		{
			data : 'englishName',
		},
		{
			data: "type",
			searchable: false
		},
		],
		columnDefs : [
			{
				"targets": [ 1, 2 ],
				"visible": false
			},
		],
        rowGroup: {
            dataSrc: 'type',
        },
		order : [[2, 'desc'],[0, 'asc']],
		language : {
			processing : "Загрузка...",
			searchPlaceholder: "Поиск ",
			search : "_INPUT_",
			lengthMenu : "Показывать _MENU_ записей на странице",
			zeroRecords : "Ничего не найдено",
			info : "Показано _TOTAL_",
			infoEmpty : "Нет доступных записей",
			infoFiltered : "из _MAX_",
			loadingRecords: "Загрузка..."
		},
		initComplete: function(settings, json) {
		    $('#books tbody tr:eq(0)').click();
		    table.row(':eq(0)', { page: 'current' }).select();
			scrollEventHeight = document.getElementById('scroll_load_simplebar').offsetHeight - 400;
		    const simpleBar = new SimpleBar(document.getElementById('scroll_load_simplebar'));
		    simpleBar.getScrollElement().addEventListener('scroll', function(event){
		    	if (simpleBar.getScrollElement().scrollTop > scrollEventHeight){
		    	      table.page.loadMore();
		    	      simpleBar.recalculate();
		    	      scrollEventHeight +=750;
		    	}
		    });
		},
		drawCallback: function ( settings ) {
		    $('#books tbody tr:eq(0)').click();
		    table.row(':eq(0)', { page: 'current' }).select();
		}
	});

	$('#books tbody').on('click', 'tr', function () {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
		var tr = $(this).closest('tr');
		var table = $('#books').DataTable();
		var row = table.row( tr );
		var data = row.data();
		if (data === undefined) {
			return;
		}
		$('#book_name').html(data.name);
		document.getElementById('type').innerHTML = data.type;
		var source = '<span>' + data.id + '</span>';
		document.getElementById('source').innerHTML = source;
		document.title = data.name;
		history.pushState('data to be passed', '', '/books/' + data.englishName.split(' ').join('_'));
		var url = '/books/fragment/' + data.id;
		$("#content_block").load(url);
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});
$('#search').on( 'keyup click', function () {
	if($(this).val()){
		$('#text_clear').show();
	}
	else {
		$('#text_clear').hide();
	}
	const table = $('#books').DataTable();
	table.tables().search($(this).val()).draw();
});
$('#text_clear').on('click', function () {
	$('#search').val('');
	const table = $('#books').DataTable();
	table.tables().search($(this).val()).draw();
	$('#text_clear').hide();
});
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
});