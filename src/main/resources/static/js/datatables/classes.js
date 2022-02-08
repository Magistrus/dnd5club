$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#classes').DataTable({
		ajax : '/data/classes',
		dom: 'tS',
		serverSide : true,
        deferRender: true,
		iDisplayLength : 80,
        scrollCollapse: true,
		select: true,
		select: {
			style: 'single',
			toggleable: false
		},
		order : [[0, 'asc']],
		columns : [
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="info_block">1</div><div class="content"><h3 class="row_name"><span>' + row.name;
					result+='</span><span>[' + row.englishName + ']</span></h3></div>';
					return result;
				}
				return data;
			}
		}, 
		],
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
		initComplete: function(settings, json) {
			scrollEventHeight = document.getElementById('scroll_load_simplebar').offsetHeight - 500;
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
			if(rowSelectIndex === 0 && selectedClass === null){
				if (!$('#list_page_two_block').hasClass('block_information')){
					return;
				}
				$('#classes tbody tr:eq('+rowSelectIndex+')').click();
			}
			if (selectedClass) {
				selectClass(selectedClass);
				var rowIndexes = [];
				table.rows( function ( idx, data, node ) {
					if(data.id === selectedClass.id){
						rowIndexes.push(idx);
					}
					return false;
				});
				rowSelectIndex = rowIndexes[0];
			}
			table.row(':eq('+rowSelectIndex+')', { page: 'current' }).select();
		},
		createdRow: function (row, data, dataIndex) {
			
		},
	});
	$('#classes tbody').on('mouseup', 'tr', function (e) {
		if (e.which == 2) {
			var tr = $(this).closest('tr');
			var row = table.row( tr );
			rowSelectIndex = row.index();
			var data = row.data();
			window.open('/classes/' + data.englishName.split(' ').join('_'));
		}
	});
	$('#classes tbody').on('click', 'tr', function () {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
		var tr = $(this).closest('tr');
		var table = $('#classes').DataTable();
		var row = table.row( tr );
		var data = row.data();
		if (cntrlIsPressed){
			window.open('/backgrounds/' + data.englishName.split(' ').join('_'));
		}
		rowSelectIndex = row.index();
		selectClass(data);
		selectedClass = data;
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});
function selectClass(data){
	$('#trait_name').text(data.name);
	document.title = data.name + ' (' +data.englishName+ ')' + ' | Классы D&D 5e';
	history.pushState('data to be passed', '', '/classes/' + data.englishName.split(' ').join('_'));
	var url = '/classes/fragment_id/' + data.id;
	$("#content_block").load(url);
}
$('#text_clear').on('click', function () {
	$('#search').val('');
	const table = $('#treasures').DataTable();
	table.tables().search($(this).val()).draw();
	$('#text_clear').hide();
});
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
});
$('#btn_filters').on('click', function() {
	$('#searchPanes').toggleClass('hide_block');
});
$('.ability_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="ability"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#classes').DataTable().column(2).search(properties, true, false, false).draw();
	if(properties) {
		$('#ability_clear_btn').removeClass('hide_block');
	} else {
		$('#ability_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#ability_clear_btn').on('click', function() {
	$('#skill_clear_btn').addClass('hide_block');
	$('.skill_checkbox').prop('checked', false);
	$('#classes').DataTable().column(2).search("", true, false, false).draw();
	setFiltered();
});
$('.skill_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="skill"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#classes').DataTable().column(3).search(properties, true, false, false).draw();
	if(properties) {
		$('#skill_clear_btn').removeClass('hide_block');
	} else {
		$('#skill_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#skill_clear_btn').on('click', function() {
	$('#skill_clear_btn').addClass('hide_block');
	$('.skill_checkbox').prop('checked', false);
	$('#classes').DataTable().column(3).search("", true, false, false).draw();
	setFiltered();
});
$('.prerequisite_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="prerequisite"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#classes').DataTable().column(4).search(properties, true, false, false).draw();
	if(properties) {
		$('#prerequisite_clear_btn').removeClass('hide_block');
	} else {
		$('#prerequisite_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#prerequisite_clear_btn').on('click', function() {
	$('#prerequisite_clear_btn').addClass('hide_block');
	$('.prerequisite_checkbox').prop('checked', false);
	$('#classes').DataTable().column(4).search("", true, false, false).draw();
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