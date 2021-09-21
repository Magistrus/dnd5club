$(document).ready(function() {
	$('a.toggle-vis').on( 'click', function (e) {
		e.preventDefault();
		var column = table.column( $(this).attr('data-column') );
		column.visible( ! column.visible() );
	});
	var table = $('#spells').DataTable({
		//stateSave: true,
		dom: 'ti',
		serverSide : true,
		ajax : '/data/spells',
		select: true,
		//iDisplayLength : 10,
		//paging: false,
		
		select: {
			style: 'single'
		},
		columns : [
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var school = '';
					var result = '<h5>' + data + ' <p class="en_title encaption_text">' + row.englishName ;
					if (row.ritual === 'true') {
							result+=' <span title="ритуал">Р</span>'; 
					}
					if (row.concentration === 'true') {
						result+=' <span title="концентрация">К</span>';	
					}
					result+='</p></h5><small>';
					result += row.school;
					result += '</small>';
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
		document.getElementById('level').innerHTML = data.level +', ' data.school;
		document.getElementById('timecast').innerHTML = data.timeCast;
		document.getElementById('distance').innerHTML = data.distance;
		document.getElementById('components').innerHTML = data.components;
		document.getElementById('duration').innerHTML = data.duration;
		var url = '/spells/fragment/' + data.id;
		$(".content_block").load(url);
	});
});
