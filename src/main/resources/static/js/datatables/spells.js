$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#spells').DataTable({
		ajax : '/data/spells?sourceTypes=' + sourceTypes,
		dom: 't',
		serverSide : true,
        deferRender: true,
        scrollCollapse: true,
		iDisplayLength : 80,
		order : [[0, 'asc'], [2, 'asc']],
		select: true,
		select: {
			style: 'single',
			toggleable: false
		},
		columns : [
		{
			data : 'level',
			searchable: false,
		},
		{
			data : 'school',
			searchable: false,
		},
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='<div class="tip info_block" title="'+(row.level ===  0 ? 'Заговор' : row.level + ' уровень заклинания') +'">' + (row.level ===  0 ? '◐' : row.level) + '</div>';
					result+='<div class="content"><div class="row_name">' + row.name;
					result+='<span>[' + row.englishName + ']</span></div>';
					result+='<div class="content_description"><div class="secondary_name s1 capitalize_text">' + row.school + '</div>';
					result+='<div class="secondary_name s2">';
					if (row.concentration) {
						result+='<span class="concentration" title="Концентрация">К</span>';
					}
					if (row.ritual) {
						result+='<span class="ritual" title="Ритуал">Р</span>';
					}
					if (row.verbal) {
						result+='<span class="tip excretion" title="Вербальный">' + row.verbal + '</span>';
					}
					if (row.somatic) {
						result+='<span class="tip excretion" title="Соматический">' + row.somatic + '</span>';
					}
					if (row.material) {
						result+='<span class="tip excretion" title="Материальный">' + row.material + '</span>';
					}
					result+='</div></div></div>';
					return result;
				}
				return data;
			}
		}, 
		{
			data : 'englishName',
		},
		{
			data : "classes",
			searchable: false,
		},
		{
			data : "damageType",
			searchable: false
		},
		{
			data : "ritual",
			searchable: false
		},
		{
			data : "concentration",
			searchable: false
		},
		{
			data : 'timeCast',
			searchable: false,
		},
		{
			data : 'components',
			searchable: false,
		},
		],
		columnDefs : [
			{
				"targets": [ 0,1,3,4,5,6,7,8,9 ],
				"visible": false
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
			scrollEventHeight = document.getElementById('scroll_load_simplebar').offsetHeight - 300;
		    const simpleBar = new SimpleBar(document.getElementById('scroll_load_simplebar'));
		    simpleBar.getScrollElement().addEventListener('scroll', function(event){
		    	if (simpleBar.getScrollElement().scrollTop > scrollEventHeight){
		    		  table.page.loadMore();
		    	      simpleBar.recalculate();
		    	      scrollEventHeight +=750;
		    	}
		    });
		},
		createdRow: function (row, data, dataIndex) {
			if(data.homebrew){
				$(row).addClass('custom_source');
				if(localStorage.getItem('homebrew_source') != 'true'){
					$(row).addClass('hide_block');
				}
			} else if (data.setting){
				$(row).addClass('setting_source');
				if(localStorage.getItem('setting_source') != 'true'){
					$(row).addClass('hide_block');
				}
			}
		},
		drawCallback: function ( settings ) {
			if(rowSelectIndex === 0 && selectedSpell === null){
				if (!$('#list_page_two_block').hasClass('block_information') && selectedSpell === null){
					return;
				}
				$('#spells tbody tr:eq('+rowSelectIndex+')').click();
			}
			if(localStorage.getItem('homebrew_source') == 'false' && selectedSpell === null){
				for(; rowSelectIndex < table.data().count(); rowSelectIndex++){
					if(!table.rows(rowSelectIndex).data()[0].homebrew){
						$('#spells tbody tr:eq('+rowSelectIndex+')').click();
						break;
					}
				}
			}
			if (selectedSpell) {
				selectSpell(selectedSpell);
				let rowIndexes = [];
				table.rows( function ( idx, data, node ) {
					if(data.id === selectedSpell.id){
						rowIndexes.push(idx);
					}
					return false;
				});
				rowSelectIndex = rowIndexes[0];
			}
			table.row(':eq('+rowSelectIndex+')', { page: 'current' }).select();
		}
	});
	$('#spells tbody').on('mousedown', 'tr', function (e) {
		if (e.which == 2) {
			var tr = $(this).closest('tr');
			var row = table.row( tr );
			rowSelectIndex = row.index();
			var data = row.data();
			window.open('/spells/' + data.englishName.split(' ').join('_'));
		}
	});
	$('#spells tbody').on('click', 'tr', function () {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
		var tr = $(this).closest('tr');
		var table = $('#spells').DataTable();
		var row = table.row( tr );
		var data = row.data();
		if (cntrlIsPressed){
			window.open('/spells/' + data.englishName.split(' ').join('_'));
		}
		rowSelectIndex = row.index();
		selectSpell(data);
		selectedSpell = data;
	});
});
function selectSpell(data){
	$('#row_name').html(data.name);
	document.title = data.name;
	history.pushState('data to be passed', '', '/spells/' + data.englishName.split(' ').join('_'));
	const url = '/spells/fragment/' + data.id;
	$("#content_block").load(url);
}
var timer, delay = 300;
$('#search').bind('keydown blur change', function(e) {
    var _this = $(this);
    clearTimeout(timer);
    timer = setTimeout(function() {
		if($('#search').val()){
			$('#text_clear').show();
		}
		else {
			$('#text_clear').hide();
		}
		const table = $('#spells').DataTable();
		table.tables().search($('#search').val()).draw();
    }, delay );
});
$('#text_clear').on('click', function () {
	$('#search').val('');
	const table = $('#spells').DataTable();
	table.tables().search($(this).val()).draw();
	$('#text_clear').hide();
});
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
	localStorage.removeItem('selected_spell');
	history.pushState('data to be passed', 'Заклинания', '/spells/');
});
$('#btn_filters').on('click', function() {
	$('#searchPanes').toggleClass('hide_block');
});
$('.level_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="level"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#spells').DataTable().column(0).search(properties, true, false, false).draw();
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
	$('#spells').DataTable().column(0).search("", true, false, false).draw();
	setFiltered();
});
$('.class_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="class"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#spells').DataTable().column(4).search(properties, true, false, false).draw();
	if(properties) {
		$('#class_clear_btn').removeClass('hide_block');
	} else {
		$('#class_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#class_clear_btn').on('click', function() {
	$('#class_clear_btn').addClass('hide_block');
	$('.class_checkbox').prop('checked', false);
	$('#spells').DataTable().column(4).search("", true, false, false).draw();
	setFiltered();
});
$('.school_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="school"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#spells').DataTable().column(1).search(properties, true, false, false).draw();
	if(properties) {
		$('#school_clear_btn').removeClass('hide_block');
	} else {
		$('#school_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#school_clear_btn').on('click', function() {
	$('#school_clear_btn').addClass('hide_block');
	$('.school_checkbox').prop('checked', false);
	$('#spells').DataTable().column(1).search("", true, false, false).draw();
	setFiltered();
});
$('.timecast_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="timecast"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#spells').DataTable().column(8).search(properties, true, false, false).draw();
	if(properties) {
		$('#timecast_clear_btn').removeClass('hide_block');
	} else {
		$('#timecast_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#timecast_clear_btn').on('click', function() {
	$('#timecast_clear_btn').addClass('hide_block');
	$('.timecast_checkbox').prop('checked', false);
	$('#spells').DataTable().column(8).search("", true, false, false).draw();
	setFiltered();
});
$('.component_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="component"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#spells').DataTable().column(9).search(properties, true, false, false).draw();
	if(properties) {
		$('#component_clear_btn').removeClass('hide_block');
	} else {
		$('#component_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#component_clear_btn').on('click', function() {
	$('#component_clear_btn').addClass('hide_block');
	$('.component_checkbox').prop('checked', false);
	$('#spells').DataTable().column(9).search("", true, false, false).draw();
	setFiltered();
});
$('.ritual_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="ritual"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#spells').DataTable().column(7).search(properties, true, false, false).draw();
	if(properties) {
		$('#ritual_clear_btn').removeClass('hide_block');
	} else {
		$('#ritual_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#ritual_clear_btn').on('click', function() {
	$('#ritual_clear_btn').addClass('hide_block');
	$('.ritual_checkbox').prop('checked', false);
	$('#spells').DataTable().column(7).search("", true, false, false).draw();
	setFiltered();
});
$('.concentration_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="concentration"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#spells').DataTable().column(8).search(properties, true, false, false).draw();
	if(properties) {
		$('#concentration_clear_btn').removeClass('hide_block');
	} else {
		$('#concentration_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#concentration_clear_btn').on('click', function() {
	$('#concentration_clear_btn').addClass('hide_block');
	$('.concentration_checkbox').prop('checked', false);
	$('#spells').DataTable().column(8).search("", true, false, false).draw();
	setFiltered();
});
$('.damage_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="damage"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#spells').DataTable().column(5).search(properties, true, false, false).draw();
	if(properties) {
		$('#damage_clear_btn').removeClass('hide_block');
	} else {
		$('#damage_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#damage_clear_btn').on('click', function() {
	$('#damage_clear_btn').addClass('hide_block');
	$('.damage_checkbox').prop('checked', false);
	$('#spells').DataTable().column(5).search("", true, false, false).draw();
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