$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#screens').DataTable({
		ajax : '/data/screens',
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
					var result ='<h3 class="row_name"><span>' + row.name;
					result+='</span><span>[' + row.englishName + ']</span></h3>';
					return result;
				}
				return data;
			}
		},
		{
			data : 'englishName',
		},
		{
			data : 'ordering',
			searchable: false,
		},
		],
		columnDefs : [
			{
				"targets": [ 1, 2 ],
				"visible": false
			},
		],
		order : [[2, 'asc']],
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
			if (selectedScreen) {
				selectScreen(selectedScreen);
				var rowIndexes = [];
				table.rows( function ( idx, data, node ) {
					if(data.id === selectedScreen.id){
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

	$('#screens tbody').on('click', 'tr', function () {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
		var tr = $(this).closest('tr');
		var table = $('#screens').DataTable();
		var row = table.row( tr );
		var data = row.data();
		if (data === undefined) {
			return;
		}
		selectScreen(data);
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});
function selectScreen(data) {
	$('#screen_name').html(data.name);
	document.title = data.name + ' (' +data.englishName+ ')' + ' | Киниги D&D 5e';
	history.pushState('data to be passed', '', '/screens/' + data.englishName.split(' ').join('_'));
	if (!data.parent) {
		var url = '/screens/fragment/' + data.id;
		$("#content_block").load(url, function() {
			$('.open-popup-link').magnificPopup({
				  type:'inline',
				  midClick: true
			});
		});
	} else {
		$("#content_block").load('/screens/fragmentone/' + data.id);
	}
}
$('#search').on( 'keyup click', function () {
	if($(this).val()){
		$('#text_clear').show();
	}
	else {
		$('#text_clear').hide();
	}
	const table = $('#screens').DataTable();
	table.tables().search($(this).val()).draw();
});
$('#text_clear').on('click', function () {
	$('#search').val('');
	const table = $('#screens').DataTable();
	table.tables().search($(this).val()).draw();
	$('#text_clear').hide();
});
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
	const table = $('#screens').DataTable();
	table.rows().deselect();
});