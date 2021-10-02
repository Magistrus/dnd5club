$(document).ready(function() {
	var scrollEventHeight = 0;
	var table = $('#creatures').DataTable({
		ajax : '/data/bestiary',
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
			scrollEventHeight = document.getElementById('scroll_load_simplebar').offsetHeight - 300;
		    $('#creatures tbody tr:eq(0)').click();
		    table.row(':eq(0)', { page: 'current' }).select();
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

	$('#creatures tbody').on('click', 'tr', function () {
		var tr = $(this).closest('tr');
		var table = $('#creatures').DataTable();
		var row = table.row( tr );
		var data = row.data();
		document.getElementById('creature_name').innerHTML = data.name;
		document.getElementById('cr').innerHTML = data.cr;
		document.getElementById('type').innerHTML = data.type;
		document.getElementById('size').innerHTML = data.size;

		var source = '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +data.id+'\'">' + data.bookshort + '</span>';
		source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
		document.getElementById('source').innerHTML = source;
		document.title = data.name;
		history.pushState('data to be passed', '', '/bestiary/' + data.englishName.split(' ').join('_'));
		var url = '/bestiary/fragment/' + data.id;
		$(".content_block").load(url);
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});