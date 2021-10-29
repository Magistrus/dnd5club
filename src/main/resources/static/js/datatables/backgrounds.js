$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#backgrounds').DataTable({
		ajax : '/data/backgrounds',
		dom: 't',
		serverSide : true,
        deferRender: true,
		iDisplayLength : 30,
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
					var result ='<div class="spell_name">' + row.name;
					result+='<span>' + row.englishName + '</span></div>';
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
			"targets": [ 1 ],
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
			if(rowSelectIndex === 0 && selectedBackground === null){
				$('#backgrounds tbody tr:eq('+rowSelectIndex+')').click();
			}
			if (selectedBackground) {
				selectBackground(selectedBackground);
				var rowIndexes = [];
				table.rows( function ( idx, data, node ) {
					if(data.id === selectedBackground.id){
						rowIndexes.push(idx);
					}
					return false;
				});
				rowSelectIndex = rowIndexes[0];
			}
			table.row(':eq('+rowSelectIndex+')', { page: 'current' }).select();
		}
	});
	$('#backgrounds tbody').on('click', 'tr', function () {
		var tr = $(this).closest('tr');
		var table = $('#backgrounds').DataTable();
		var row = table.row( tr );
		var data = row.data();
		rowSelectIndex = row.index();
		selectBackground(data);
		selectedBackground = null;
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});
function selectBackground(data){
	document.getElementById('background_name').innerHTML = data.name;
	document.getElementById('traits').innerHTML = data.skills;
	document.getElementById('traits_tools').innerHTML = data.toolSkills;
	document.getElementById('languages').innerHTML = data.languages;
	document.getElementById('equipments').innerHTML = data.equipments;
	document.getElementById('start_money').innerHTML = data.startMoney + ' зм.';
	var source = '<span class="tip" title="' + data.book + '">' + data.bookshort + '</span>';
	document.getElementById('source').innerHTML = source;
	document.title = data.name;
	history.pushState('data to be passed', '', '/backgrounds/' + data.englishName.split(' ').join('_'));
	var url = '/backgrounds/fragment/' + data.id;
	$("#content_block").load(url);
}
$('#btn_close').on('click', function() {
	document.getElementById('container_card').classList.toggle('block_information');
});