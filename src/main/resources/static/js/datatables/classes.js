$(document).ready(function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
	var scrollEventHeight = 0;
	var rowSelectIndex = 0;
	let table = $('#classes').DataTable({
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
		order : [[1, 'asc'], [0, 'asc']],
		columns : [
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					let result ='<div class="wrapper"><i class="info_block">'+row.icon+'</i>';
					result+='<div class="content"><h3 class="row_name"><span><span class="name">' + row.name;
					result+='</span> <span>[' + row.englishName + ']</span></span><span class="books tip" title="' + row.book + '">' + row.bookshort + '</span></h3>';
					result+='<div class="two_row"><span>'+ row.hitDice+'</span></div></div>';
					if (row.archetypeName !== null){
						result+='<button class="open tip" title="'+row.archetypeName+'" data-tipped-options="position: \'left\'"><svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M6 14L11.2929 19.2929C11.6834 19.6834 12.3166 19.6834 12.7071 19.2929L18 14M12 11V11C13.6569 11 15 9.65685 15 8V8C15 6.34315 13.6569 5 12 5V5C10.3431 5 9 6.34315 9 8V8C9 9.65685 10.3431 11 12 11Z" stroke="#4D4DAA" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg></button></div>';
						result+='<div class="archetypes"><div class="main">';
						if (row.archetypes.length > 0) {
							result+='<div class="archetype_list"><h4>Основное:</h4><ul>';
							row.archetypes.forEach(function(item, i, arr) {
								result+= '<li class="archetype_item" id="'+item.englishName.replace(' ', '_')+'"><i class="add_favorites"></i><p>' + item.name+' <span>'+item.bookshort+' / ' +item.englishName  + '</span></p></li>';
							});
							result+='</ul></div>';
						}
						if (row.settingArchetypes.length > 0) {
							result+='<div class="archetype_list setting_source '+(localStorage.getItem('setting_source') != 'true' ? 'hide_block' : '') +'"><h4>Сеттинги:</h4><ul>';
							row.settingArchetypes.forEach(function(item, i, arr) {
								result+= '<li class="archetype_item" id="'+item.englishName.replace(' ', '_')+'"><i class="add_favorites"></i><p>' + item.name+' <span>'+item.bookshort+' / ' +item.englishName  + '</span></p></li>';
							});
							result+='</ul></div></div>';
						}
						if (row.homebrewArchetypes.length > 0) {
							result+='<div class="homebrew_list archetype_list custom_source '+(localStorage.getItem('homebrew_source') != 'true' ? 'hide_block' : '') +'"><h4>Homebrew:</h4><ul>';
							row.homebrewArchetypes.forEach(function(item, i, arr) {
								result+= '<li class="archetype_item" id="'+item.englishName.replace(' ', '_')+'"><i class="add_favorites"></i><p>' + item.name+' <span>'+item.bookshort+' / ' +item.englishName  + '</span></p></li>';
							});
							result+='</ul></div>';
						}
					}
					return result;
				}
				return data;
			}
		},
		{
			data : "sidekick",
			searchable: false
		},
		],
        rowGroup: {
            dataSrc: 'sidekick',
        },
		columnDefs : [
			{
				"targets": [1],
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

		},
		drawCallback: function ( settings ) {
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
				$('#classes tbody tr:eq('+rowSelectIndex+')').click();
				table.row(':eq('+rowSelectIndex+')', { page: 'current' }).select();
			}
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
	$('#classes tbody').on('mouseup', 'tr', function (e) {
		if (e.which == 2) {
			var tr = $(this).closest('tr');
			var row = table.row( tr );
			rowSelectIndex = row.index();
			var data = row.data();
			window.open('/classes/' + data.englishName.split(' ').join('_'));
		}
	});
	$('#classes tbody').on('click', 'tr', function (event) {
		var tr = $(this).closest('tr');
		var row = table.row( tr );
		var data = row.data();
		if (cntrlIsPressed){
			window.open('/classes/' + data.englishName.split(' ').join('_'));
		}
		if(data.spellcaster){
			$('#class_spells').removeClass('hide_block');
		} else {
			$('#class_spells').addClass('hide_block');
		}
		if(data.option !== null){
			$('#button_option_name').text(data.option);
			$('#class_options').removeClass('hide_block');
		} else{
			$('#class_options').addClass('hide_block');
		}
		rowSelectIndex = row.index();
		if ($(event.target).closest('li').length != 0){
			let liTareget = $(event.target).closest('li')[0]; 
			if (liTareget.classList.contains('select_point')){
				liTareget.classList.remove('select_point');
				selectedArchetype = null;
				selectClass(data);
			} else {
				$('li').removeClass('select_point');
				liTareget.classList.add('select_point');
				selectedArchetype = null;
				setActiveArchetype(data, data.englishName.replace(' ', '_'), liTareget.id);
			}
			if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
				document.getElementById('list_page_two_block').classList.add('block_information');
			}
		}
		else if (event.target.tagName == 'BUTTON' || event.target.parentNode.tagName == 'BUTTON' || event.target.parentNode.parentNode.tagName == 'BUTTON'){
			tr[0].classList.toggle('open');
			SimpleBar.instances.get(document.querySelector('[data-simplebar]')).recalculate();
		}
		else {
			$('li').removeClass('select_point');
			$('tr').removeClass('open');
			tr[0].classList.add('open');
			selectClass(data);
			if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
				document.getElementById('list_page_two_block').classList.add('block_information');
			}
		}
		selectedClass = data;
		if ($(event.target).closest('li').length == 0){
			event.target.scrollIntoView({block: "center", behavior: "smooth"});	
		}
	});
});
$('#search').on( 'keyup click', function () {
	$('#classes').DataTable().tables().search($(this).val()).draw();
});
function selectClass(data){
	$('#class_name').text(data.name);
	document.title = data.name + ' (' +data.englishName+ ')' + ' | Классы D&D 5e';
	history.pushState('data to be passed', '', '/classes/' + data.englishName.split(' ').join('_'));
	if (selectedArchetype){
		setActiveArchetype(data, selectedClass.englishName.replace(' ', '_'), selectedArchetype);
		$('#' + selectedArchetype).addClass('select_point');
		return;
	}
	var url = '/classes/fragment_id/' + data.id;
	$("#content_block").load(url, function() {
		$('#info_wrapper').removeClass('description');
		$('#info_wrapper').removeClass('spells');
		$('#info_wrapper').removeClass('images');
		$('#info_wrapper').removeClass('options');
		$('#info_wrapper').addClass('traits');
		$('.btn_class').removeClass('active');
		$('#class_traits').addClass('active');
		if(localStorage.getItem('homebrew_source') == 'true'){
			$('#homebrew_source').prop('checked', true);
			$('.custom_source').removeClass('hide_block');
			$('#source_id').addClass('active');
		} else {
			localStorage.setItem('homebrew_source', 'false');
		}
		if(localStorage.getItem('setting_source') == 'true'){
			$('#setting_source').prop('checked', true);
			$('.setting_source').removeClass('hide_block');
			$('.module_source').removeClass('hide_block');
			$('#source_id').addClass('active');
		}
		$('#mobile_selector').change(function() {
			setActiveArchetype(data, data.englishName.replace(' ', '_'), $('#mobile_selector').val());
		});
	});
}
function setActiveArchetype(data, className, archetypeName) {
	document.title = data.name + ' ('+data.englishName+') - '+ $('#' + archetypeName).text() +  ' | Подклассы D&D 5e';
	var url = '/classes/' + className + '/architypes/' + archetypeName;
	$("#content_block").load(url, function() {
		$('#mobile_selector').change(function () {
			setActiveArchetype(data, className, $('#mobile_selector').val());
		});
		$('#info_wrapper').removeClass('description');
		$('#info_wrapper').removeClass('spells');
		$('#info_wrapper').removeClass('images');
		$('#info_wrapper').removeClass('options');
		$('#info_wrapper').addClass('traits');
		$('.btn_class').removeClass('active');
		$('#class_traits').addClass('active');
		showOnlyArchetype();
		selectedArchetype = null;
	});
	history.pushState('data to be passed', className, '/classes/' + className + '/' + archetypeName);
}
$('#class_traits').on('click', function() {
	$('.btn_class').removeClass('active');
	this.classList.add('active');
	let data = $('#classes').DataTable().rows({selected:  true}).data();
	if ($('li').hasClass('select_point')){
		setActiveArchetype(data[0], data[0].englishName.replace(' ', '_'), $('li.select_point').attr('id'));
	}
	else {
		selectClass(data[0]);
	}
});
$('#class_description').on('click', function() {
	$('.btn_class').removeClass('active');
	this.classList.add('active');
	var data = $('#classes').DataTable().rows({selected:  true}).data()[0];
	if ($('li').hasClass('select_point')){
		url = '/classes/'+data.englishName+ '/archetype/' + $('li.select_point').attr('id') + '/description';
		loadDescription(url);
	}
	else {
		var url = '/classes/' + data.englishName + '/description';
		loadDescription(url);
	}
	localStorage.setItem('class_info', 'description');
});
$('#class_spells').on('click', function() {
	$('.btn_class').removeClass('active');
	this.classList.add('active');
	loadClassSpells();
});
$('#class_options').on('click', function() {
	$('.btn_class').removeClass('active');
	this.classList.add('active');
	var selectedClass = $('.card.active')[0];
	localStorage.setItem('class_info', 'options');
	loadClassOptions();
});
$('#class_images').on('click', function() {
	$('.btn_class').removeClass('active');
	this.classList.add('active');
	loadImages();
	localStorage.setItem('class_info', 'images');
})
$('#text_clear').on('click', function () {
	$('#search').val('');
	$('#classes').DataTable().tables().search($(this).val()).draw();
	$('#text_clear').hide();
});
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
	$('#classes').DataTable().rows().deselect();
	$('li').removeClass('select_point');
	history.pushState('data to be passed', '', '/classes/');
});
$('#btn_filters').on('click', function() {
	$('#searchPanes').toggleClass('hide_block');
});
$('.dice_hp_checkbox').on('change', function(e){
	let properties = $('input:checkbox[name="dice_hp"]:checked').map(function() {
		return this.value;
	}).get().join('|');
	$('#classes').DataTable().column(2).search(properties, true, false, false).draw();
	if(properties) {
		$('#dice_hp_clear_btn').removeClass('hide_block');
	} else {
		$('#dice_hp_clear_btn').addClass('hide_block');
	}
	setFiltered();
});
$('#dice_hp_clear_btn').on('click', function() {
	$('#dice_hp_clear_btn').addClass('hide_block');
	$('.dice_hp_checkbox').prop('checked', false);
	$('#classes').DataTable().column(2).search("", true, false, false).draw();
	setFiltered();
});
$('#only_archetypes').click(function () {
	showOnlyArchetype();
})
function showOnlyArchetype(){
	if($('#only_archetypes').is(':checked')) {
		$('details').not('.feet_show').addClass('hide_block');
	} else {
		$('details').removeClass('hide_block');
	}
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
function loadDescription(url){
	$('#content_block').load(url, function() {
		$('#info_wrapper').removeClass('traits');
		$('#info_wrapper').removeClass('spells');
		$('#info_wrapper').removeClass('images');
		$('#info_wrapper').removeClass('options');
		$('#info_wrapper').addClass('description');
	});
}
function loadClassSpells() {
	let data = $('#classes').DataTable().rows({selected:  true}).data()[0];
	let url = '/classes/spells/' + data.englishName;
	$('#content_block').load(url, function() {
		$('#info_wrapper').removeClass('traits');
		$('#info_wrapper').removeClass('description');
		$('#info_wrapper').removeClass('images');
		$('#info_wrapper').removeClass('options');
		$('#info_wrapper').addClass('spells');
	});
}
function loadClassOptions() {
	let data = $('#classes').DataTable().rows({selected:  true}).data()[0];
	var url = '/classes/options/' + data.englishName;
	$('#content_block').load(url, function() {
		$('#info_wrapper').removeClass('traits');
		$('#info_wrapper').removeClass('description');
		$('#info_wrapper').removeClass('images');
		$('#info_wrapper').removeClass('spells');
		$('#info_wrapper').removeClass('options');
		$('#info_wrapper').addClass('options');
	});
}
function loadImages(){
	let data = $('#classes').DataTable().rows({selected:  true}).data()[0];
	var englishName =data.englishName.replace(' ', '_');
	var url = '/classes/images/' + englishName;
	$('#content_block').load(url, function() {
		$('#info_wrapper').removeClass('traits');
		$('#info_wrapper').removeClass('description');
		$('#info_wrapper').removeClass('spells');
		$('#info_wrapper').removeClass('options');
		$('#info_wrapper').addClass('images');
	});
}