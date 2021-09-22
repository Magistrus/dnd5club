$(document).ready(function() {
	var table = $('#spells').DataTable({
		stateSave: true,
		dom: 'ti',
		serverSide : true,
		ajax : '/data/spells',
		select: true,
		iDisplayLength : 50,
		//paging: false,
		
		select: {
			style: 'single'
		},
		columns : [
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result = '<h5>' + data + '</h5>';
					result+='<p class="en_title encaption_text">' ;
					result+= '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +row.id+'\'">' + row.bookshort + '</span>';
					result+= '<span id="inline-tooltip-source-'+ row.id + '" style="display: none">' + row.book + '</span>';
					result+=  '/ '+ row.englishName;
					result+= '</p>'; 
					result+='<small>' + row.school + '</small>';
					return result;
				}
				return data;
			}, 
		},
			],
			columnDefs : [
				{
					"targets": [ 0 ],
					"visible": true
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
		ordering : true,
	});
	$('#spells tbody').on('click', 'tr', function () {
		var tr = $(this).closest('tr');
		var table = $('#spells').DataTable();
		var row = table.row( tr );
		var data = row.data();
		document.getElementById('spell_name').innerHTML = data.name;
		document.getElementById('level').innerHTML = data.level +', ' + data.school;
		document.getElementById('timecast').innerHTML = data.timeCast;
		document.getElementById('distance').innerHTML = data.distance;
		document.getElementById('components').innerHTML = data.components;
		document.getElementById('duration').innerHTML = data.duration;
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
});