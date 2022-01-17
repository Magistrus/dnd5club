$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#weapons').DataTable({
		ajax : '/data/weapons',
		dom: 't',
		serverSide : true,
        deferRender: true,
		iDisplayLength : 80,
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
					var result ='<div class="info_block">' + row.damage + '</div>';
					result +='<div class="content"><h3 class="row_name"><span>' + row.name;
					result+='</span><span>[' + row.englishName + ']</span></h3>';
					result+='<div class="content_description"><div class="secondary_name s1">' + row.type + '</div>';
					result+='<div class="secondary_name s2 alg_left"><span class="tip dice_text" title="Урон">' + row.damage + '</span> &nbsp; <span class="tip" title="Тип урона">' + row.damageType +'</span></div>';
					result+='<div class="secondary_name s3"><span class="tip excretion" title="Стоимость">' + row.cost + '</span></div></div></div>';
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
			data : 'properties',
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
				"targets": [ 0, 2, 3, 4, 5 ],
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
			if (!$('#list_page_two_block').hasClass('block_information') && selectedWeapon === null){
				return;
			}
			$('#weapons tbody tr:eq('+rowSelectIndex+')').click();
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
	$('#weapons tbody').on('mouseup', 'tr', function (e) {
		if (e.which == 2) {
			var tr = $(this).closest('tr');
			var row = table.row( tr );
			rowSelectIndex = row.index();
			var data = row.data();
			window.open('/weapons/' + data.englishName.split(' ').join('_'));
		}
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
		if (cntrlIsPressed){
			window.open('/weapons/' + data.englishName.split(' ').join('_'));
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
		$('#searchPanes').toggleClass('hide_block');
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
$('.damge_type_checkbox').on('change', function(e){
	var damageTypes = $('input:checkbox[name="damageType"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#weapons').DataTable().column(3).search(damageTypes, true, false, false).draw();
	if(damageTypes) {
		$('#damage_clear_btn').removeClass('hide_block');
	} else {
		$('#damage_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#damage_clear_btn').on('click', function() {
	$('#damage_clear_btn').addClass('hide_block');
	$('.damge_type_checkbox').prop('checked', false);
	$('#weapons').DataTable().column(3).search("", true, false, false).draw();
	setFiltered();
});
$('.property_checkbox').on('change', function(e){
	var properties = $('input:checkbox[name="property"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#weapons').DataTable().column(4).search(properties, true, false, false).draw();
	if(properties) {
		$('#property_clear_btn').removeClass('hide_block');
	} else {
		$('#property_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#property_clear_btn').on('click', function() {
	$('#property_clear_btn').addClass('hide_block');
	$('.property_checkbox').prop('checked', false);
	$('#weapons').DataTable().column(4).search("", true, false, false).draw();
	setFiltered();
});
function setFiltered(){
	let boxes = $('input:checkbox:checked.filter').map(function() {
		return this.value;
	}).get().join('|');
	if(boxes.length === 0){
		$('#icon_filter').removeClass('active');
	} else {
		$('#icon_filter').addClass('active');
	}
}