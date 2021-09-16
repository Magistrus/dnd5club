$(document).ready(function() {
	$('a.toggle-vis').on( 'click', function (e) {
		e.preventDefault();
		var column = table.column( $(this).attr('data-column') );
		column.visible( ! column.visible() );
	});
	var table = $('#spells').DataTable({
		stateSave: true,
		dom: 't',
		serverSide : true,
		ajax : '/data/spells',
		deferRender: true,
		scrollY: "85vh",
		scrollX: "65vh",
		scrollCollapse: true,
		scroller: true,
		//paging: false,
		select: {
			style: 'single'
		},
		autoWidth: false,
		searchPanes: {
            layout: 'columns-4'
        },
		columns : [
		{
			data : "name",
			width : "20%", 
			render : function(data, type, row) {
				if (type === 'display') {
					var school = '';
					var result = '<h6>' + data + ' <small class="text-secondary">[' + row.englishName + ']';
					if (row.ritual === 'true') {
							result+=' <span data-toggle="tooltip" data-placement="top" title="ритуал" class="badge badge-pill badge badge-success">Р</span>'; 
					}
					if (row.concentration === 'true') {
						result+=' <span data-toggle="tooltip" data-placement="top" title="концентрация" class="badge badge-pill badge-info">К</span>';	
					}
					result+='</small></h6><small>';
					result += '<div class="text-secondary">';
					result += row.school;
					result += '</small></div>';
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
					extend: 'colvis',
					text: 'Столбцы',
	            	className: 'btn-main btn-sm btn-color-main',
					buttons: [{
						text: 'Школа',
						action: function ( e, dt, node, config ) {
							dt.column( 0 ).visible( ! dt.column( 0 ).visible() );
						}
					},
					{
						text: 'Время',
						action: function ( e, dt, node, config ) {
							dt.column( 4 ).visible( ! dt.column( 4 ).visible() );
						}
					},
					{
						text: 'Длительность',
						action: function ( e, dt, node, config ) {
							dt.column( 5 ).visible( ! dt.column( 5 ).visible() );
						}
					},
					{
						text: 'Дистанция',
						action: function ( e, dt, node, config ) {
							dt.column( 6 ).visible( ! dt.column( 6 ).visible() );
						}
					},
					{
						text: 'Ритуал',
						action: function ( e, dt, node, config ) {
							dt.column( 7 ).visible( ! dt.column( 7 ).visible() );
						}
					},
					{
						text: 'Концентрация',
						action: function ( e, dt, node, config ) {
							dt.column( 8 ).visible( ! dt.column( 8 ).visible() );
						}
					},
					{
						text: 'Компоненты',
						action: function ( e, dt, node, config ) {
							dt.column( 9 ).visible( ! dt.column( 9 ).visible() );
							dt.column( 10 ).visible( ! dt.column( 10 ).visible() );
							dt.column( 11 ).visible( ! dt.column( 11 ).visible() );
						}
					},
					{
						text: 'Тип урона',
						action: function ( e, dt, node, config ) {
							dt.column( 12 ).visible( ! dt.column( 12 ).visible() );
							table.columns.adjust().draw();
						}
					},
					{
						text: 'Источник',
						action: function ( e, dt, node, config ) {
							dt.column( 13 ).visible( ! dt.column( 13 ).visible() );
						}
					}]
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
		var url = '/spells/fragment/' + row.data().id;
		$(".content_block").load(url);
	});
});