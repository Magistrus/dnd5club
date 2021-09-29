$(document).ready(function() {
	var scrollEventHeight = 0;
	var table = $('#armors').DataTable({
		ajax : '/data/armors',
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
			data : "type",
		},
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="spell_lvl">' + row.ac + '</div>';
					result +='<div class="spell_name">' + row.name;
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
		],
		columnDefs : [
			{
				"targets": [ 0 ],
				"visible": false
			},
			{
				"targets": [ 2 ],
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
		    $('#armors tbody tr:eq(0)').click();
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
	$('#armors tbody').on('click', 'tr', function () {
		var tr = $(this).closest('tr');
		var table = $('#armors').DataTable();
		var row = table.row( tr );
		var data = row.data();
		document.getElementById('armor_name').innerHTML = data.name;
		document.getElementById('ac').innerHTML = data.ac;
		document.getElementById('type').innerHTML = data.type;
		document.getElementById('cost').innerHTML = data.cost;
		document.getElementById('weight').innerHTML = data.weight;
		document.getElementById('requirements').innerHTML = data.requirements;
		document.getElementById('stealth').innerHTML = data.stealth;
		
		var source = '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +data.id+ '\'">' + data.bookshort + '</span>';
		source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
		document.getElementById('source').innerHTML = source;

		history.pushState('data to be passed', '', '/armors/' + data.englishName.split(' ').join('_'));
		var url = '/armors/fragment/' + data.id;
		$(".content_block").load(url);
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});