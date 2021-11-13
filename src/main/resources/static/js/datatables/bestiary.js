$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#creatures').DataTable({
		ajax : '/data/bestiary',
		dom: 't',
		serverSide : true,
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
				orderable: false
            },
            layout: 'columns-4',
			orderable: false
        },
		columns : [
		{
			data : 'exp',
		},
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="spell_lvl">' + row.cr + '</div>';
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
			data : 'cr',
			searchable: false
		},
		{
			data : 'type',
			searchable: false
		},
		{
			data : 'size',
			searchable: false
		},
		],
		columnDefs : [
			{
				"targets": [0, 2 ,3, 4 ],
				"visible": false
			},
			{
				"targets": [ 5 ],
				"visible": false,
				searchPanes: {
	                  dtOpts: {
	                    order:[]
	                  }
	            }
			},
		],
		order : [[0, 'asc'], [1, 'asc']],
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
			if(rowSelectIndex === 0 && selectedCreature === null){
				$('#creatures tbody tr:eq('+rowSelectIndex+')').click();
			}
			if (selectedCreature){
				selectCreature(selectedCreature);
				var rowIndexes = [];
				table.rows( function ( idx, data, node ) {
					if(data.id === selectedCreature.id){
						rowIndexes.push(idx);
					}
					return false;
				});
				rowSelectIndex = rowIndexes[0];
			}
			table.row(':eq('+rowSelectIndex+')', { page: 'current' }).select();
		}
	});

	$('#creatures tbody').on('click', 'tr', function () {
		var tr = $(this).closest('tr');
		var table = $('#creatures').DataTable();
		var row = table.row( tr );
		rowSelectIndex = row.index();
		var data = row.data();
		selectCreature(data);
		selectedCreature = null;
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
		rowSelectIndex = 0;
	});
	$('#btn_filters').on('click', function() {
		var table = $('#creatures').DataTable();
		table.searchPanes.container().toggle();
	})
});
function selectCreature(data){
	document.getElementById('creature_name').innerHTML = data.name;
	document.getElementById('cr').innerHTML = data.cr + ' (' + data.exp+' опыта)';
	switch(data.cr){
	case '1/8': data.cr = 1/8; break;
	case '1/4': data.cr = 1/4; break;
	case '1/2': data.cr = 1/2; break;
	}
	document.getElementById('cr_value').value = data.cr;

	document.getElementById('type').innerHTML = data.type +', '+data.alignment;
	document.getElementById('size').innerHTML = data.size;
	document.getElementById('creatute_img').src = httpGet('/image/CREATURE/'+data.id);

	var source = '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +data.id+'\'">' + data.bookshort + '</span>';
	source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
	document.getElementById('source').innerHTML = source;
	document.title = data.name;
	history.pushState('data to be passed', '', '/bestiary/' + data.englishName.split(' ').join('_'));
	var url = '/bestiary/fragment/' + data.id;
	$("#content_block").load(url, function() {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
	});
}
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
	localStorage.removeItem('selected_creature');
	history.pushState('data to be passed', 'Бестиарий', '/bestiary/');
});
function httpGet(theUrl)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.open( "GET", theUrl, false ); 
    xmlHttp.send( null );
    return xmlHttp.responseText;
}