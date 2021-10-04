$(document).ready(function() {
	var scrollEventHeight = 0;
	var table = $('#weapons').DataTable({
		ajax : '/data/weapons',
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
					var result ='<div class="spell_lvl">' + row.damage + '</div>';
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
		}
	});

	$('#weapons tbody').on('click', 'tr', function () {
		var tr = $(this).closest('tr');
		var table = $('#weapons').DataTable();
		var row = table.row( tr );
		var data = row.data();
		document.getElementById('weapon_name').innerHTML = data.name;
		document.getElementById('type').innerHTML = data.type;
		document.getElementById('damage').innerHTML = '<span class="dice_text">' + data.damage+ '</span>' + ' ' + data.damageType;
		document.getElementById('cost').innerHTML = data.cost;
		document.getElementById('weight').innerHTML = data.weight;
		var propertyElement = document.getElementById('properties');
		while (propertyElement.firstChild) {
			propertyElement.removeChild(propertyElement.firstChild);
		}
		for (var i = 0; i < data.properties.length; i++) {
			var element = data.properties[i];
			var a = document.createElement("a");
			a.href = '/weapons/property' + element.englishName; 
			a.innerHTML = element.name;
			if (i<data.properties.length-1){
				a.innerHTML += ', ';
			}
			a.title = element.description;
			a.classList.add('tip_scroll');
			propertyElement.appendChild(a);
		}
		var source = '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +data.id+ '\'">' + data.bookshort + '</span>';
		source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
		document.getElementById('source').innerHTML = source;
		document.title = data.name;
		history.pushState('data to be passed', '', '/weapons/' + data.englishName.split(' ').join('_'));
		var url = '/weapons/fragment/' + data.id;
		$(".content_block").load(url);
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});