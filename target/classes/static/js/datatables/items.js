$(document).ready(function() {
	var scrollEventHeight = 0;
	var table = $('#items').DataTable({
		ajax : '/data/items',
		dom: 'tiS',
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
				"targets": [ 0 ],
				"visible": true
			},
			{
				"targets": [ 1 ],
				"visible": false
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
			loadingRecords: "Загрузка..."
		},
		initComplete: function(settings, json) {
		    $('#items tbody tr:eq(0)').click();
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
		}
	});

	$('#items tbody').on('click', 'tr', function () {
		var tr = $(this).closest('tr');
		var table = $('#items').DataTable();
		var row = table.row( tr );
		var data = row.data();
		document.getElementById('item_name').innerHTML = data.name;
		document.getElementById('type').innerHTML = data.type;
		document.getElementById('cost').innerHTML = data.cost;
		document.getElementById('weight').innerHTML = data.weight;
		var source = '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +data.id+ '\'">' + data.bookshort + '</span>';
		source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
		document.getElementById('source').innerHTML = source;
		document.title = data.name;
		history.pushState('data to be passed', '', '/items/' + data.englishName.split(' ').join('_'));
		var url = '/items/fragment/' + data.id;
		$(".content_block").load(url);
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});