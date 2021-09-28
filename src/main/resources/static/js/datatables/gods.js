$(document).ready(function() {
	var scrollEventHeight = 0;
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
		columns : [
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="spell_lvl">' + row.aligmentShort + '</div>';
					result+='<div class="spell_name">' + row.name;
					result+='<span>' + row.englishName + '</span></div>';
					result+='<div class="spell_school">' + row.commitment + '</div>';
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
		    $('#gods tbody tr:eq(0)').click();
		    table.row(':eq(0)', { page: 'current' }).select(); 
			scrollEventHeight = document.getElementById('scroll_load_simplebar').offsetHeight - 300;
		    const simpleBar = new SimpleBar(document.getElementById('scroll_load_simplebar'));
		    simpleBar.getScrollElement().addEventListener('scroll', function(event){
		    	if (simpleBar.getScrollElement().scrollTop > scrollEventHeight){
		    	      table.page.loadMore();
		    	      simpleBar.recalculate();
		    	      scrollEventHeight +=750;
		    	}
		    });
		}
	});
	$('#gods tbody').on('click', 'tr', function () {
		var tr = $(this).closest('tr');
		var table = $('#gods').DataTable();
		var row = table.row( tr );
		var data = row.data();
		document.getElementById('god_name').innerHTML = data.name;
		document.getElementById('alignment').innerHTML = data.alignment;
		document.getElementById('rank').innerHTML = data.rank;
		document.getElementById('title').innerHTML = data.nicknames;
		document.getElementById('symbol').innerHTML = data.symbol;
		document.getElementById('domains').innerHTML = data.domains;
		document.getElementById('pantheon').innerHTML = data.pantheon;

		var source = '<span class="tip" data-tipped-gods="inline: \'tooltip-race-source-' + data.id +'\'">' + data.bookshort + '</span>';
		source+= '<span id="tooltip-race-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
		document.getElementById('source').innerHTML = source;
		history.pushState('data to be passed', '', '/gods/' + data.englishName.split(' ').join('_'));
		var url = '/gods/fragment/' + data.id;
		$(".content_block").load(url);
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});
$('#btn_close').on('click', function() {
	document.getElementById('container_card').classList.toggle('block_information');
});