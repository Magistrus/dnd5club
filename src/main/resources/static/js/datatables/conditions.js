$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#conditions').DataTable({
		ajax : '/data/conditions',
		dom: 'tiS',
		serverSide : true,
        deferRender: true,
		iDisplayLength : 80,
        scrollCollapse: true,
		select: true,
		select: {
			style: 'single',
			toggleable: false
		},
		columns : [
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="wrapper"><div class="content"><h3 class="row_name"><span>' + row.name;
					result+='</span><span class="books tip" title="' + row.book + '">' + row.book + '</span></h3>';
					result+='<div class="two_row"><span>' + row.englishName + '</span></dv></dv></dv>';
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
			if (selectedCondition) {
				selectCondition(selectedCondition);
				var rowIndexes = [];
				table.rows( function ( idx, data, node ) {
					if(data.id === selectedCondition.id){
						rowIndexes.push(idx);
					}
					return false;
				});
				rowSelectIndex = rowIndexes[0];
			}
		    $('#screens tbody tr:eq('+rowSelectIndex+')').click();
		    table.row(':eq('+rowSelectIndex+')', { page: 'current' }).select();
		}
	});

	$('#conditions tbody').on('click', 'tr', function () {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
		var tr = $(this).closest('tr');
		var table = $('#conditions').DataTable();
		var row = table.row( tr );
		var data = row.data();
		if (data === undefined) {
			return;
		}
		selectCondition(data);
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});
function selectCondition(data){
	$('#condition_name').html(data.name);
	document.title = data.name + ' (' +data.englishName+ ')' + ' | Киниги D&D 5e';
	history.pushState('data to be passed', '', '/conditions/' + data.englishName.split(' ').join('_'));
	var url = '/conditions/fragment/' + data.id;
	$("#content_block").load(url);
}
$('#search').on( 'keyup click', function () {
	if($(this).val()){
		$('#text_clear').show();
	}
	else {
		$('#text_clear').hide();
	}
	const table = $('#conditions').DataTable();
	table.tables().search($(this).val()).draw();
});
$('#text_clear').on('click', function () {
	$('#search').val('');
	const table = $('#conditions').DataTable();
	table.tables().search($(this).val()).draw();
	$('#text_clear').hide();
});
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
	const table = $('#conditions').DataTable();
	table.rows().deselect();
});