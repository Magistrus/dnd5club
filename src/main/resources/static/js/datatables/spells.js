$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#spells').DataTable({
		ajax : '/data/spells',
		dom: 't',
		serverSide : true,
        deferRender: true,
        scrollCollapse: true,
		iDisplayLength : 25,
		order : [[0, 'asc'], [2, 'asc']],
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
			data : 'level',
		},
		{
			data : 'school',
		},
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="spell_lvl">' + (row.level ===  0 ? '<span class=\"tip\" title=\"Заговор\">Ф</span>' : row.level) + '</div>';
					result+='<div class="spell_name">' + row.name;
					result+='<span>' + row.englishName + '</span></div>';
					result+='<div class="spell_school">' + row.school + '</div>';
					return result;
				}
				return data;
			}
		}, 
		{
			data : 'englishName',
		},
		],
		columnDefs : [
			{
				"targets": [ 0 ],
				"visible": false
			},
			{
				"targets": [ 1 ],
				"visible": false
			},
			{
				"targets": [ 3 ],
				"visible": false
			},
		],
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
		    table.searchPanes.container().prependTo(table.table().container());
		    table.searchPanes.container().hide();
		},
		drawCallback: function ( settings ) {
			if(rowSelectIndex === 0 && selectedSpell === null){
				$('#spells tbody tr:eq('+rowSelectIndex+')').click();
			}
			if (selectedSpell) {
				selectSpell(selectedSpell);
				var rowIndexes = [];
				table.rows( function ( idx, data, node ) {
					if(data.id === selectedSpell.id){
						rowIndexes.push(idx);
					}
					return false;
				});
				rowSelectIndex = rowIndexes[0];
			}
			table.row(':eq('+rowSelectIndex+')', { page: 'current' }).select();
		}
	});

	$('#spells tbody').on('click', 'tr', function () {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
		var tr = $(this).closest('tr');
		var table = $('#spells').DataTable();
		var row = table.row( tr );
		rowSelectIndex = row.index();
		selectSpell(row.data());
		selectedSpell = null;
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
		rowSelectIndex = 0;
	});
	$('#btn_full_screen').on('click', function() {
		//$('#left_block')[0].style.display = 'none';
	})
	$('#btn_filters').on('click', function() {
		var table = $('#spells').DataTable();
		table.searchPanes.container().toggle();
	})
});
function selectSpell(data){
	document.getElementById('spell_name').innerHTML = data.name;
	document.getElementById('level').innerHTML =  (data.level ===  0 ? '<span class=\"tip\" title=\"Заговор\">Фокус</span>, ' : data.level +' уровень, ') + data.school;
	document.getElementById('timecast').innerHTML = data.timeCast;
	document.getElementById('distance').innerHTML = data.distance;
	document.getElementById('components').innerHTML = data.components;
	document.getElementById('duration').innerHTML = data.duration;
	
	var source = (data.homebrew ? '<span class="tip homebrew_text" title="Homebrew - не является официальным.">Homebrew</span> - ' : '') + '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +data.id+'\'">' + data.bookshort + '</span>';
	source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
	document.getElementById('source_spell').innerHTML = source;

	const classIconsElement = document.getElementById('class_icons');
	while (classIconsElement.firstChild) {
		classIconsElement.removeChild(classIconsElement.firstChild);
	}
	data.classes.forEach(element => {
		var a = document.createElement("a");
		a.href = '/classes/' + element.englishName; 
		a.title = element.name;
		a.classList.add('tip', 'icon', 'icon_' + element.englishName.toLowerCase());
		classIconsElement.appendChild(a);
	});
	document.title = data.name;
	history.pushState('data to be passed', '', '/spells/' + data.englishName.split(' ').join('_'));
	var url = '/spells/fragment/' + data.id;
	$(".content_block").load(url);
}
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
	localStorage.removeItem('selected_spell');
	history.pushState('data to be passed', 'Заклинания', '/spells/');
});