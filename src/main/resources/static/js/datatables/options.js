$(document).ready(function() {
	var table = $('#options').DataTable({
		//stateSave: true,
		dom: 'ti',
		serverSide : true,
		ajax : '/data/options',
		select: true,
        deferRender: true,
        scrollY: 845,
        scrollCollapse: true,
        scroller: true,
		select: {
			style: 'single'
		},
		columns : [
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="spell_lvl">'+ row.optionTypes[0]+'</div>';
					result+='<div class="spell_name">' + row.name;
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
				paginate : {
					first : "В начало",
					previous : "Предыдущая",
					next : "Следущая",
					last : "В конец"
				},
		},
		ordering : true,
		initComplete: function(settings, json) {
		    $('#options tbody tr:eq(0)').click();
		    table.row(':eq(0)', { page: 'current' }).select(); 
		}
	});
	$('#options tbody').on('click', 'tr', function () {
		var tr = $(this).closest('tr');
		var table = $('#options').DataTable();
		var row = table.row( tr );
		var data = row.data();
		document.getElementById('option_name').innerHTML = data.name;
		document.getElementById('requirement').innerHTML = data.prerequisite;
		var source = '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +data.id+'\'">' + data.bookshort + '</span>';
		source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
		document.getElementById('source').innerHTML = source;

		const classIconsElement = document.getElementById('class_icons');
		while (classIconsElement.firstChild) {
			classIconsElement.removeChild(classIconsElement.firstChild);
		}
/*		data.classes.forEach(element => {
			var a = document.createElement("a");
			a.href = '/options/' + element.englishName; 
			a.title = element.name;
			a.classList.add('tip', 'icon', 'icon_' + element.englishName.toLowerCase());
			classIconsElement.appendChild(a);
		});*/
		history.pushState('data to be passed', '', '/options/' + data.englishName.split(' ').join('_'));
		var url = '/options/fragment/' + data.id;
		$(".content_block").load(url);
	});
	$('#options').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});
$('#btn_close').on('click', function() {
	document.getElementById('container_card').classList.toggle('block_information');
});