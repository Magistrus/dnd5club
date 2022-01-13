$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#traits').DataTable({
		ajax : '/data/traits',
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
					var result ='<div class="content"><h3 class="row_name"><span>' + row.name;
					result+='</span><span>[' + row.englishName + ']</span></h3>';
					result+='<div class="content_description"><div class="secondary_name s1">' + row.requirement + '</div></div></div>';
					return result;
				}
				return data;
			}
		}, 
		{
			data : 'englishName',
		},
		{
			data : 'abilities',
			searchable: false
		},
		{
			data : 'skills',
			searchable: false
		},
		{
			data : 'requirement',
			searchable: false
		},
		],
		columnDefs : [
		{
			"targets": [ 1, 4 ],
			"visible": false
		},
		{
			"targets": [ 2, 3 ],
			"visible": false,
			searchPanes: {
                dtOpts: {
                  order:[]
                }
			}
		},
		],
		buttons: [
		{
		}],
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
			if(rowSelectIndex === 0 && selectedTrait === null){
				if (!$('#list_page_two_block').hasClass('block_information')){
					return;
				}
				$('#traits tbody tr:eq('+rowSelectIndex+')').click();
			}
			if (selectedTrait) {
				selectTrait(selectedTrait);
				var rowIndexes = [];
				table.rows( function ( idx, data, node ) {
					if(data.id === selectedTrait.id){
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
	$('#traits tbody').on('mousedown', 'tr', function (e) {
		if (e.which == 2) {
			var tr = $(this).closest('tr');
			var row = table.row( tr );
			rowSelectIndex = row.index();
			var data = row.data();
			window.open('/traits/' + data.englishName.split(' ').join('_'));
		}
	});
	$('#traits tbody').on('click', 'tr', function () {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
		var tr = $(this).closest('tr');
		var table = $('#traits').DataTable();
		var row = table.row( tr );
		var data = row.data();
		if (cntrlIsPressed){
			window.open('/backgrounds/' + data.englishName.split(' ').join('_'));
		}
		rowSelectIndex = row.index();
		selectTrait(data);
		selectedTrait = data;
	});
	$('#search').on( 'keyup click', function () {
		table.tables().search($(this).val()).draw();
	});
});
function selectTrait(data){
	$('#trait_name').text(data.name);
	$('#requirement').text(data.requirement);
	var source = '<span class="tip" title="'+data.book+'">' + data.bookshort + '</span>';
	document.getElementById('source').innerHTML = source;
	document.title = data.name;
	history.pushState('data to be passed', '', '/traits/' + data.englishName.split(' ').join('_'));
	var url = '/traits/fragment/' + data.id;
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
    $('#traits').DataTable().column(2).search(properties, true, false, false).draw();
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
	$('#traits').DataTable().column(2).search("", true, false, false).draw();
	setFiltered();
});
$('.skill_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="skill"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#traits').DataTable().column(3).search(properties, true, false, false).draw();
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
	$('#traits').DataTable().column(3).search("", true, false, false).draw();
	setFiltered();
});
$('.prerequisite_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="prerequisite"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#traits').DataTable().column(4).search(properties, true, false, false).draw();
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
	$('#traits').DataTable().column(4).search("", true, false, false).draw();
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