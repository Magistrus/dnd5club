$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#items_magic').DataTable({
		ajax : '/data/items/magic',
		dom: 'tiS',
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
            layout: 'columns-4',
			orderable: false
        },
		columns : [
		{
			data : 'rarity',
		},
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="spell_lvl">' + row.shortRarity + '</div>';
					result+='<div class="spell_name">' + row.name;
					result+='<span>' + row.englishName + '</span></div>';
					result+='<div class="spell_school">' + row.type + '</div>';
					return result;
				}
				return data;
			}
		}, 
		{
			data : 'englishName',
		},
		{
			data : 'type',
			searchable: false,
		},
		{
			data : 'attunement',
			searchable: false,
		},
		],
		columnDefs : [
			{
				targets: [ 0 ],
				searchPanes: {
	                  dtOpts: {
	                    order:[]
	                  }
	            }
			},
			{
				targets: [ 0, 2, 3, 4 ],
				visible: false
			},
		],
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
			if(rowSelectIndex === 0 && selectedItemMagic === null){
				if (!$('#list_page_two_block').hasClass('block_information')){
					return;
				}
				$('#items_magic tbody tr:eq('+rowSelectIndex+')').click();
			}
			if (selectedItemMagic) {
				selectMagicItem(selectedItemMagic);
				var rowIndexes = [];
				table.rows( function ( idx, data, node ) {
					if(data.id === selectedItemMagic.id){
						rowIndexes.push(idx);
					}
					return false;
				});
				rowSelectIndex = rowIndexes[0];
			}
			table.row(':eq('+rowSelectIndex+')', { page: 'current' }).select();
		}
	});

	$('#items_magic tbody').on('click', 'tr', function () {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
		var tr = $(this).closest('tr');
		var table = $('#items_magic').DataTable();
		var row = table.row( tr );
		var data = row.data();
		rowSelectIndex = row.index();
		selectMagicItem(data);
		selectedMagicItem = null;
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
	$('#btn_filters').on('click', function() {
		var table = $('#items_magic').DataTable();
		table.searchPanes.container().toggle();
	});
});
function selectMagicItem(data){
	document.getElementById('item_name').innerHTML = data.name;
	document.getElementById('type').innerHTML = data.type;
	document.getElementById('rarity').innerHTML = data.rarity;
	document.getElementById('attunement').innerHTML = data.attunement;
	document.getElementById('cost').innerHTML = data.cost;

	var source = '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +data.id+'\'">' + data.bookshort + '</span>';
	source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
	document.getElementById('source').innerHTML = source;
	document.title = data.name;
	history.pushState('data to be passed', '', '/items/magic/' + data.englishName.split(' ').join('_'));
	var url = '/items/magic/fragment/' + data.id;
	$("#content_block").load(url);
}
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
});