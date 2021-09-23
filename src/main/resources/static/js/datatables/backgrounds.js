$(document).ready(function() {
	var table = $('#backgrounds').DataTable({
		stateSave: true,
		dom: 'ti',
		serverSide : true,
		ajax : '/data/backgrounds',
		select: true,
        scrollY: 845,
        deferRender: true,
        scroller: true,
		select: {
			style: 'single'
		},
		columns : [
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="spell_name">' + row.name;
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
		    $('#backgrounds tbody tr:eq(0)').click();
		    table.row(':eq(0)', { page: 'current' }).select(); 
		}
	});
	$('#backgrounds tbody').on('click', 'tr', function () {
		var tr = $(this).closest('tr');
		var table = $('#backgrounds').DataTable();
		var row = table.row( tr );
		var data = row.data();
		document.getElementById('option_name').innerHTML = data.name;
		document.getElementById('traits').innerHTML = data.skills;
		document.getElementById('traits_tools').innerHTML = data.toolSkills;
		document.getElementById('languages').innerHTML = data.languages;
		document.getElementById('equipments').innerHTML = data.equipments;
		document.getElementById('start_money').innerHTML = data.startMoney + ' зм.';
		var source = '<span class="tip" data-tipped-backgrounds="inline: \'inline-tooltip-source-' +data.id+'\'">' + data.bookshort + '</span>';
		source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
		document.getElementById('source').innerHTML = source;
		history.pushState('data to be passed', '', '/backgrounds/' + data.englishName.split(' ').join('_'));
		var url = '/backgrounds/fragment/' + data.id;
		$(".content_block").load(url);
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});
$('#btn_close').on('click', function() {
	document.getElementById('container_card').classList.toggle('block_information');
});