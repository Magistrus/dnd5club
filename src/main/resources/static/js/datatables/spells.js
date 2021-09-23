$(document).ready(function() {
	var table = $('#spells').DataTable({
		ajax : '/data/spells',
		dom: 'tiS',
		stateSave: true,
		serverSide : true,
        deferRender: true,
        scrollY: $(window).height - 500,
        scrollCollapse: true,
        scroller: true,
        scroller: {
            displayBuffer: 20,
            rowHeight: 40,
            loadingIndicator: true
        },
        paging: false,
		select: true,
		select: {
			style: 'single'
		},
		columns : [
		{
			data : 'level',
		},
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="spell_lvl">' + row.level + '</div>';
					result+='<div class="spell_name">' + row.name;
					result+='<span>' + row.englishName + '</span></div>';
					result+='<div class="spell_school">' + row.school + '</div>';
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
				searchPanes: {
				clearMessage : "Сбросить",
				title : {
						_: 'Отфильтровано - %d',
					0: 'Фильтры не активны (Ctrl или Shift для множественного выбора)',
					1: 'Один фильтр выбран',
				},
				collapse: 'Фильтры (%d)'
			}
		},
		initComplete: function(settings, json) {
		    $('#spells tbody tr:eq(0)').click();
		    table.row(':eq(0)', { page: 'current' }).select(); 
		}
	});
	$('#spells tbody').on('click', 'tr', function () {
		var tr = $(this).closest('tr');
		var table = $('#spells').DataTable();
		var row = table.row( tr );
		var data = row.data();
		document.getElementById('spell_name').innerHTML = data.name;
		document.getElementById('level').innerHTML =  (data.level === 'ЗГ' ? 'Заговор, ' : data.level +' уровень, ') + data.school;
		document.getElementById('timecast').innerHTML = data.timeCast;
		document.getElementById('distance').innerHTML = data.distance;
		document.getElementById('components').innerHTML = data.components;
		document.getElementById('duration').innerHTML = data.duration;
		var source = '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +data.id+'\'">' + data.bookshort + '</span>';
		source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
		document.getElementById('source_spell').innerHTML = source;

		const classIconsElement = document.getElementById('class_icons');
		while (classIconsElement.firstChild) {
			classIconsElement.removeChild(classIconsElement.firstChild);
		}
		data.classes.forEach(element => {
			var a = document.createElement("a");
			a.href = '/classes/' + element.englishName; 
			a.title = element.name;
			a.classList.add('tip', 'icon', 'icon_' + element.englishName.toLowerCase());
			classIconsElement.appendChild(a);
		});
		history.pushState('data to be passed', '', '/spells/' + data.englishName.split(' ').join('_'));
		var url = '/spells/fragment/' + data.id;
		$(".content_block").load(url);
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});

});