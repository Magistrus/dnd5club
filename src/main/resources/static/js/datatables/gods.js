$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#gods').DataTable({
		ajax : '/data/gods',
		dom: 't',
		serverSide : true,
        deferRender: true,
		iDisplayLength : 25,
        scrollCollapse: true,
		select: true,
		select: {
			style: 'single'
		},
        searchPanes: {
            initCollapsed: true,
            viewCount: false,
            dtOpts: {
                select: {
                    //style: 'multi'
                },
				searching: false,
            },
			orderable: false
        },
		columns : [
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="info_block">' + row.aligmentShort + '</div>';
					result+='<div class="content"><div class="row_name">' + row.name;
					result+='<span>' + row.englishName + '</span></div>';
					result+='<div class="secondary_name">' + row.commitment + '</div></div>';
					return result;
				}
				return data;
			}
		}, 
		{
			data : 'englishName',
		},
		{
			data : 'alignment',
			searchable: false,
		},
		{
			data : 'domains',
			searchable: false,
		},
		{
			data : 'rank',
			searchable: false,
		},
		{
			data : 'sex',
			searchable: false,
		},
		],
		columnDefs : [
		{
			"targets": [ 1, 2, 3, 4, 5 ],
			"visible": false
		},
		],
		buttons: [
		{
		}],
		order : [[0, 'asc']],
			language : {
				processing : "Загрузка...",
				searchPlaceholder: "Поиск ",
				search : "_INPUT_",
				lengthMenu : "Показывать _MENU_ записей на странице",
				zeroRecords : "Ничего не найдено",
				info : "Показано _TOTAL_",
				infoEmpty : "Нет доступных записей",
				infoFiltered : "из _MAX_",
				loadingRecords: "Загрузка...",
		        searchPanes: {
		            title: {
		                 _: 'Выбрано фильтров - %d',
		                 0: 'Фильтры не выбраны',
		                 1: 'Один фильтр выбран'
		            },
	                collapseMessage: 'Свернуть все',
	                showMessage: 'Развернуть все',
	                clearMessage: 'Сбросить фильтры'
		        }
		},
		initComplete: function(settings, json) {
			scrollEventHeight = document.getElementById('scroll_load_simplebar').offsetHeight - 300;
		    const simpleBar = new SimpleBar(document.getElementById('scroll_load_simplebar'));
		    simpleBar.getScrollElement().addEventListener('scroll', function(event){
		    	if (simpleBar.getScrollElement().scrollTop > scrollEventHeight){
		    	      table.page.loadMore();
		    	      simpleBar.recalculate();
		    	      scrollEventHeight +=750;
		    	}
		    });
		    table.searchPanes.container().prependTo($('#searchPanes'));
		    table.searchPanes.container().hide();
		},
		drawCallback: function ( settings ) {
			if(rowSelectIndex === 0 && selectedGod === null){
				if (!$('#list_page_two_block').hasClass('block_information')){
					return;
				}
				$('#items_magic tbody tr:eq('+rowSelectIndex+')').click();
			}
			if (selectedGod) {
				var rowIndexes = [];
				table.rows( function ( idx, data, node ) {
					if(data.id === selectedGod.id){
						rowIndexes.push(idx);
					}
					return false;
				});
				rowSelectIndex = rowIndexes[0];
				selectGod(selectedGod);
			}
			table.row(':eq('+rowSelectIndex+')', { page: 'current' }).select();
		}
	});
	$('#gods tbody').on('click', 'tr', function () {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
		let row = $('#gods').DataTable().row( $(this).closest('tr') );
		rowSelectIndex = row.index();
		selectGod(row.data());
	});
	$('#search').on( 'keyup click', function () {
		if($(this).val()){
			$('#text_clear').show();
		}
		else {
			$('#text_clear').hide();
		}
		table.tables().search($(this).val()).draw();
	});
	$('#btn_filters').on('click', function() {
		var table = $('#gods').DataTable();
		table.searchPanes.container().toggle();
	});
});
function selectGod(data){
	$('#god_name').html(data.name);
	$('#alignment').html(data.alignment);
	$('#rank').html(data.rank);
	$('#title').html(data.nicknames);
	$('#symbol').html(data.symbol);
	$('#domains').html(data.domains);
	$('#pantheon').html(data.pantheon);

	var source = '<span class="tip" data-tipped-gods="inline: \'tooltip-race-source-' + data.id +'\'">' + data.bookshort + '</span>';
	source+= '<span id="tooltip-race-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
	document.getElementById('source').innerHTML = source;
	document.title = data.name;
	history.pushState('data to be passed', '', '/gods/' + data.englishName.split(' ').join('_'));
	var url = '/gods/fragment/' + data.id;
	$("#content_block").load(url);
	selectedGod = null;
}
$('#text_clear').on('click', function () {
	$('#search').val('');
	const table = $('#gods').DataTable();
	table.tables().search($(this).val()).draw();
	$('#text_clear').hide();
});
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
});