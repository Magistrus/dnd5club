$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#weapons').DataTable({
		ajax : '/data/weapons',
		dom: 't',
		serverSide : true,
        deferRender: true,
		iDisplayLength : 30,
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
			data : "type",
		},
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="info_block">' + row.damage + '</div>';
					result +='<div class="content"><div class="row_name">' + row.name;
					result+='<span>' + row.englishName + '</span></div>';
					result+='<div class="secondary_name">' + row.type + '</div></div>';
					return result;
				}
				return data;
			}
		}, 
		{
			data : 'englishName',
		},
		{
			data : 'damageType',
			searchable: false
		},
		{
			data : 'cost',
			searchable: false,
			width : "1%",
		},
		],
		columnDefs : [
			{
				"targets": [ 0,  2, 3 ],
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
	        searchPanes: {
                collapseMessage: 'Свернуть все',
                showMessage: 'Развернуть все',
                clearMessage: 'Сбросить фильтры'
	        }
		},
		initComplete: function(settings, json) {
		    $('#weapons tbody tr:eq(0)').click();
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
		    table.searchPanes.container().prependTo($('#searchPanes'));
		    table.searchPanes.container().hide();
		},
		drawCallback: function ( settings ) {
		    $('#weapons tbody tr:eq(0)').click();
		    table.row(':eq(0)', { page: 'current' }).select();
		},
		createdRow: function (row, data, dataIndex) {
			if(data.homebrew){
				$(row).addClass('custom_source');
				if(localStorage.getItem('homebrew_source') != 'true'){
					$(row).addClass('hide_block');
				}
			}
		},
	});

	$('#weapons tbody').on('click', 'tr', function () {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
		var tr = $(this).closest('tr');
		var table = $('#weapons').DataTable();
		var row = table.row( tr );
		var data = row.data();
		if (data === undefined) {
			return;
		}
		$('#weapon_name').html(data.name);
		$('#type').html(data.type);
		$('#damage').html('<span class="dice_text">' + data.damage + '</span>' + ' ' + data.damageType);
		$('#cost').html(data.cost);
		$('#weight').html(data.weight);
		var propertyElement = document.getElementById('properties');
		while (propertyElement.firstChild) {
			propertyElement.removeChild(propertyElement.firstChild);
		}
		for (var i = 0; i < data.properties.length; i++) {
			var element = data.properties[i];
			var a = document.createElement("a");
			a.href = '#' + element.englishName; 
			a.innerHTML = element.name;
			a.title = element.description;
			a.classList.add('tip_scroll');
			propertyElement.appendChild(a);
			switch(element.name){
			case 'Универсальное':
				a.innerHTML += ' ';
				var span = document.createElement("span");
				span.innerHTML= data.versatile;
				span.classList.add('dice_text');
				propertyElement.appendChild(span);
				break;
			case 'Метательное':
			case 'Боеприпас':
				var span = document.createElement("span");
				span.innerHTML= data.distance;
				propertyElement.appendChild(span);
				break;
			}
			if (i < data.properties.length-1){
				var span = document.createElement("span");
				span.innerHTML += ', ';
				propertyElement.appendChild(span);
			}
		}
		var source = (data.homebrew ? '<span class="tip homebrew_text" title="Homebrew - не является официальным.">Homebrew</span> - ' : '') + '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +data.id+ '\'">' + data.bookshort + '</span>';
		source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
		$('#source').html(source);
		document.title = data.name;
		history.pushState('data to be passed', '', '/weapons/' + data.englishName.split(' ').join('_'));
		var url = '/weapons/fragment/' + data.id;
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
	$('#btn_filters').on('click', function() {
		var table = $('#weapons').DataTable();
		table.searchPanes.container().toggle();
	})
});
$('#text_clear').on('click', function () {
	$('#search').val('');
	const table = $('#weapons').DataTable();
	table.tables().search($(this).val()).draw();
	$('#text_clear').hide();
});
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
});