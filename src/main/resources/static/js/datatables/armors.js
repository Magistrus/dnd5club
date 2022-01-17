$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#armors').DataTable({
		ajax : '/data/armors',
		dom: 't',
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
			data : "type",
		},
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="info_block">' + row.ac + '</div>';
					result +='<div class="content"><h3 class="row_name"><span>' + row.name;
					result+='</span><span>[' + row.englishName + ']</span></h3>';
					result+='<div class="secondary_name">' + row.type + '</div></div>';
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
        rowGroup: {
            dataSrc: 'type',
        },
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
			if (!$('#list_page_two_block').hasClass('block_information') && selectedArmor === null){
				return;
			}
			$('#armors tbody tr:eq('+rowSelectIndex+')').click();
		    table.row(':eq(0)', { page: 'current' }).select();
		}
	});
	$('#armors tbody').on('mouseup', 'tr', function (e) {
		if (e.which == 2) {
			var tr = $(this).closest('tr');
			var row = table.row( tr );
			rowSelectIndex = row.index();
			var data = row.data();
			window.open('/armors/' + data.englishName.split(' ').join('_'));
		}
	});
	$('#armors tbody').on('click', 'tr', function () {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
		var tr = $(this).closest('tr');
		var table = $('#armors').DataTable();
		var row = table.row( tr );
		var data = row.data();
		if (data === undefined) {
			return;
		}
		if (cntrlIsPressed){
			window.open('/armors/' + data.englishName.split(' ').join('_'));
		}
		$('#armor_name').text(data.name);
		$('#ac').text(data.acFull);
		$('#type').text(data.type);
		$('#cost').text(data.cost);
		$('#weight').text(data.weight);
		$('#requirements').text(data.requirements);
		$('#stealth').text(data.stealth);
		
		var source = '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +data.id+ '\'">' + data.bookshort + '</span>';
		source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
		document.getElementById('source').innerHTML = source;
		document.title = data.name;
		history.pushState('data to be passed', '', '/armors/' + data.englishName.split(' ').join('_'));
		var url = '/armors/fragment/' + data.id;
		$("#content_block").load(url);
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
});
$('#text_clear').on('click', function () {
	$('#search').val('');
	var table = $('#armors').DataTable();
	table.tables().search($(this).val()).draw();
	$('#text_clear').hide();
});
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
});