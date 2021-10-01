$(document).ready(function() {
	var scrollEventHeight = 0;
	var table = $('#options').DataTable({
		ajax : '/data/options',
		dom: 't',
		serverSide : true,
        deferRender: true,
		iDisplayLength : 30,
        scrollCollapse: true,
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
		ordering : true,
		initComplete: function(settings, json) {
		    $('#options tbody tr:eq(0)').click();
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
		data.optionTypes.forEach(element => {
			var a = document.createElement("a");
			a.href = '/classes/' + element.className; 
			a.title = element.name;
			a.classList.add('tip', 'icon', 'icon_' + element.className.toLowerCase());
			classIconsElement.appendChild(a);
		});
		document.title = data.name;
		history.pushState('data to be passed', '', '/options/' + data.englishName.split(' ').join('_'));
		var url = '/options/fragment/' + data.id;
		$(".content_block").load(url);
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});
$('#btn_close').on('click', function() {
	document.getElementById('container_card').classList.toggle('block_information');
});