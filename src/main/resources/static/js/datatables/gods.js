$(document).ready(function() {
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	var table = $('#gods').DataTable({
		ajax : '/data/gods',
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
					var result ='<div class="info_block">' + row.aligmentShort + '</div>';
					result+='<div class="content"><h3 class="row_name"><span>' + row.name;
					result+='</span><span>[' + row.englishName + ']</span></h3>';
					result+='<div class="secondary_name">' + row.commitment + '</div></div>';
					return result;
				}
				return data;
			}
		}, 
		{
			data : 'englishName',
		},
		{
			data : 'alignment',
			searchable: false,
		},
		{
			data : 'domains',
			searchable: false,
		},
		{
			data : 'rank',
			searchable: false,
		},
		{
			data : 'sex',
			searchable: false,
		},
		{
			data : 'pantheon',
			searchable: false,
		},
		],
		columnDefs : [
		{
			"targets": [ 1, 2, 3, 4, 5, 6 ],
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
		drawCallback: function ( settings ) {
			if(rowSelectIndex === 0 && selectedGod === null){
				if (!$('#list_page_two_block').hasClass('block_information') && selectedGod === null){
					return;
				}
				$('#items_magic tbody tr:eq('+rowSelectIndex+')').click();
			}
			if (selectedGod) {
				var rowIndexes = [];
				table.rows( function ( idx, data, node ) {
					if(data.id === selectedGod.id){
						rowIndexes.push(idx);
					}
					return false;
				});
				rowSelectIndex = rowIndexes[0];
				selectGod(selectedGod);
			}
			table.row(':eq('+rowSelectIndex+')', { page: 'current' }).select();
		}
	});
	$('#gods tbody').on('mouseup', 'tr', function (e) {
		if (e.which == 2) {
			var tr = $(this).closest('tr');
			var row = table.row( tr );
			rowSelectIndex = row.index();
			var data = row.data();
			window.open('/gods/' + data.englishName.split(' ').join('_'));
		}
	});
	$('#gods tbody').on('click', 'tr', function () {
		if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
			document.getElementById('list_page_two_block').classList.add('block_information');
		}
		let row = $('#gods').DataTable().row( $(this).closest('tr'));
		let data = row.data()
		if (cntrlIsPressed){
			window.open('/gods/' + data.englishName.split(' ').join('_'));
		}
		rowSelectIndex = row.index();
		selectGod(data);
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
function selectGod(data){
	$('#god_name').html(data.name);
	document.title = data.name + ' (' +data.englishName+ ')' + ' | Боги D&D 5e';
	history.pushState('data to be passed', '', '/gods/' + data.englishName.split(' ').join('_'));
	let url = '/gods/fragment/' + data.id;
	$("#content_block").load(url, function() {
		$('.image-container').magnificPopup({
			delegate: 'a',
			type: 'image',
			gallery:{
				enabled:true
			}
		});
	});
	selectedGod = null;
}
$('#text_clear').on('click', function () {
	$('#search').val('');
	const table = $('#gods').DataTable();
	table.tables().search($(this).val()).draw();
	$('#text_clear').hide();
});
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
});
$('#btn_filters').on('click', function() {
	$('#searchPanes').toggleClass('hide_block');
});
$('.alignment_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="alignment"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#gods').DataTable().column(2).search(properties, true, false, false).draw();
	if(properties) {
		$('#alignment_clear_btn').removeClass('hide_block');
	} else {
		$('#alignment_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#alignment_clear_btn').on('click', function() {
	$('#alignment_clear_btn').addClass('hide_block');
	$('.alignment_checkbox').prop('checked', false);
	$('#gods').DataTable().column(2).search("", true, false, false).draw();
	setFiltered();
});
$('.domen_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="domen"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#gods').DataTable().column(3).search(properties, true, false, false).draw();
	if(properties) {
		$('#domen_clear_btn').removeClass('hide_block');
	} else {
		$('#domen_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#domen_clear_btn').on('click', function() {
	$('#domen_clear_btn').addClass('hide_block');
	$('.domen_checkbox').prop('checked', false);
	$('#gods').DataTable().column(3).search("", true, false, false).draw();
	setFiltered();
});
$('.rank_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="rank"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#gods').DataTable().column(4).search(properties, true, false, false).draw();
	if(properties) {
		$('#rank_clear_btn').removeClass('hide_block');
	} else {
		$('#rank_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#rank_clear_btn').on('click', function() {
	$('#rank_clear_btn').addClass('hide_block');
	$('.rank_checkbox').prop('checked', false);
	$('#gods').DataTable().column(4).search("", true, false, false).draw();
	setFiltered();
});
$('.pantheon_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="pantheon"]:checked').map(function() {
		return this.value;
	}).get().join('|');
    $('#gods').DataTable().column(5).search(properties, true, false, false).draw();
	if(properties) {
		$('#pantheon_clear_btn').removeClass('hide_block');
	} else {
		$('#pantheon_clear_btn').addClass('hide_block');
	}
    setFiltered();
});
$('#pantheon_clear_btn').on('click', function() {
	$('#pantheon_clear_btn').addClass('hide_block');
	$('.pantheon_checkbox').prop('checked', false);
	$('#gods').DataTable().column(5).search("", true, false, false).draw();
	setFiltered();
});
function getImage(id){
	$.ajax({
        type: 'GET',
        url: '/images/GOD/' + id,
        data: 'id=testdata',
        dataType: 'json',
        cache: false,
        success: function(result) {
        	$('.image-container').empty();
        	result.forEach((element, index) => {
        		let alement;
        		if (index==0){
        			aelement = '<a id="god_img" href="'+element+'"><img src="'+ element+'"/></a>';
        		} else {
        			aelement = '<a href="'+element+'"></a>';
        		}
        		$('.image-container').append(aelement);
        	});
        },
    });
}
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