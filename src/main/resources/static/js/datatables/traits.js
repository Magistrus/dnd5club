$(document).ready(function() {
	var table = $('#traits').DataTable({
		ajax : '/data/traits',
		dom: 'tS',
		serverSide : true,
        deferRender: true,
        scrollY: 900,
        scrollCollapse: true,
        scroller: true,
        scroller: {
            displayBuffer: 20,
            rowHeight: 50,
            loadingIndicator: true
        },
		select: true,
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
				loadingRecords: "Загрузка..."
		},
		initComplete: function(settings, json) {
		    $('#traits tbody tr:eq(0)').click();
		    table.row(':eq(0)', { page: 'current' }).select(); 
		}
	});
	$('#traits tbody').on('click', 'tr', function () {
		var tr = $(this).closest('tr');
		var table = $('#traits').DataTable();
		var row = table.row( tr );
		var data = row.data();
		document.getElementById('trait_name').innerHTML = data.name;
		document.getElementById('requirement').innerHTML = data.requirement;
		var source = '<span class="tip" data-tipped-traits="inline: \'tooltip-background-source-' + data.id+'\'">' + data.bookshort + '</span>';
		source+= '<span id="tooltip-background-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
		document.getElementById('source').innerHTML = source;
		history.pushState('data to be passed', '', '/traits/' + data.englishName.split(' ').join('_'));
		var url = '/traits/fragment/' + data.id;
		$(".content_block").load(url);
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});
$('#btn_close').on('click', function() {
	document.getElementById('container_card').classList.toggle('block_information');
});