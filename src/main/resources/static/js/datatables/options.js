$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#options').DataTable({
		ajax : '/data/options',
		dom: 't',
		serverSide : true,
        deferRender: true,
		iDisplayLength : 80,
        scrollCollapse: true,
		select: true,
		select: {
			style: 'single',
			toggleable: false
		},
		columns : [
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<h3 class="row_name"><span>' + row.name;
					result+='</span><span>[' + row.englishName + ']</span></h3>';
					return result;
				}
				return data;
			}
		}, 
		{
			data : 'englishName',
		},
		{
			data : 'type',
			searchable: false
		},
		{
			data : 'prerequisite',
			searchable: false
		},
		{
			data : 'level',
			searchable: false
		},
		],
		columnDefs : [
		{
			"targets": [ 1, 2, 3, 4],
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
				loadingRecords: "Загрузка...",
		},
		ordering : true,
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
			if(rowSelectIndex === 0 && selectedOption === null){
				if (!$('#list_page_two_block').hasClass('block_information')){
					return;
				}
				$('#options tbody tr:eq('+rowSelectIndex+')').click();
			}
			if (selectedOption) {
				selectOption(selectedOption);
				var rowIndexes = [];
				table.rows( function ( idx, data, node ) {
					if(data.id === selectedOption.id){
						rowIndexes.push(idx);
					}
					return false;
				});
				rowSelectIndex = rowIndexes[0];
			}
			table.row(':eq('+rowSelectIndex+')', { page: 'current' }).select();
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
	$('#options tbody').on('mouseup', 'tr', function (e) {
		if (e.which == 2) {
			var tr = $(this).closest('tr');
			var row = table.row( tr );
			rowSelectIndex = row.index();
			var data = row.data();
			window.open('/options/' + data.englishName.split(' ').join('_'));
		}
	});
	$('#options tbody').on('click', 'tr', function () {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
		var tr = $(this).closest('tr');
		var table = $('#options').DataTable();
		var row = table.row(tr);
		rowSelectIndex = row.index();
		var data = row.data();
		if (cntrlIsPressed){
			window.open('/options/' + data.englishName.split(' ').join('_'));
		}
		selectOption(data);
		selectedOption = data;
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
});
$('#btn_filters').on('click', function() {
	$('#searchPanes').toggleClass('hide_block');
});
function selectOption(data){
	$('#option_name').text(data.name);
	$('#requirement').text(data.prerequisite);
	var source = '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +data.id+'\'">' + data.bookshort + '</span>';
	source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
	$('#source').html(source);

	const classIconsElement = document.getElementById('class_icons');
	while (classIconsElement.firstChild) {
		classIconsElement.removeChild(classIconsElement.firstChild);
	}
	data.optionTypes.forEach(element => {
		var a = document.createElement("a");
		a.href = '/classes/' + element.className; 
		if (element.archetypeName){
			a.href += '/' + element.archetypeName; 
		}
		a.title = element.name;
		a.classList.add('tip', 'icon', 'icon_' + element.className.toLowerCase().replace(' ', '_'));
		classIconsElement.appendChild(a);
	});
	document.title = data.name;
	history.pushState('data to be passed', '', '/options/' + data.englishName.split(' ').join('_'));
	var url = '/options/fragment/' + data.id;
	$("#content_block").load(url);
}
$('#text_clear').on('click', function () {
	$('#search').val('');
	const table = $('#options').DataTable();
	table.tables().search($(this).val()).draw();
	$('#text_clear').hide();
});
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
});
$('.category_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="category"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#options').DataTable().column(2).search(properties, true, false, false).draw();
	if(properties) {
		$('#category_clear_btn').removeClass('hide_block');
	} else {
		$('#category_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#category_clear_btn').on('click', function() {
	$('#category_clear_btn').addClass('hide_block');
	$('.category_checkbox').prop('checked', false);
	$('#options').DataTable().column(2).search("", true, false, false).draw();
	setFiltered();
});
$('.prerequsite_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="prerequsite"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#options').DataTable().column(3).search(properties, true, false, false).draw();
	if(properties) {
		$('#prerequsite_clear_btn').removeClass('hide_block');
	} else {
		$('#prerequsite_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#prerequsite_clear_btn').on('click', function() {
	$('#prerequsite_clear_btn').addClass('hide_block');
	$('.prerequsite_checkbox').prop('checked', false);
	$('#options').DataTable().column(3).search("", true, false, false).draw();
	setFiltered();
});
$('.level_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="level"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#options').DataTable().column(4).search(properties, true, false, false).draw();
	if(properties) {
		$('#level_clear_btn').removeClass('hide_block');
	} else {
		$('#level_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#level_clear_btn').on('click', function() {
	$('#level_clear_btn').addClass('hide_block');
	$('.level_checkbox').prop('checked', false);
	$('#options').DataTable().column(4).search("", true, false, false).draw();
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