$(document).ready(function() {
	var scrollEventHeight = 0;
	var table = $('#users').DataTable({
		ajax : '/admin/data/users',
		dom: 't',
		serverSide : true,
        deferRender: true,
		iDisplayLength : 100,
        scrollCollapse: true,
		select: false,
		columns : [
		{
			data : "name",
			render : function(data, type, row) {
				if (type === 'display') {
					var result ='';
					result +='<div class="content"><h3 class="row_name"><span>' + row.name;
					result+='</span><span>' + row.email + '</span></h3>';
					result+='<div class="secondary_name">' + row.createDate + '</div></div>';
					result+='<div class="secondary_name s2">';
					result+='<label class="check_block"><input class="role_button check" type="checkbox" name="role"'+(row.roles.includes('USER') ? ' checked' : '')+'><span>USER</span></label>';
					result+='<label class="check_block"><input class="role_button check" type="checkbox" name="role"'+(row.roles.includes('SUPERUSER') ? ' checked' : '')+'><span>SUPERUSER</span></label>';
					result+='<label class="check_block"><input class="role_button check" type="checkbox" name="role"'+(row.roles.includes('WRITER') ? ' checked' : '')+'><span>WRITER</span></label>';
					result+='<label class="check_block"><input class="role_button check" type="checkbox" name="role"'+(row.roles.includes('MODERATOR') ? ' checked' : '')+'><span>MODERATOR</span></label>';
					result+='<label class="check_block"><input class="role_button check" type="checkbox" name="role"'+(row.roles.includes('ADMIN') ? ' checked' : '')+'><span>ADMIN</span></label>';
					result+='</div>';
					return result;
				}
				return data;
			}
		},
		{
			data : "email",
		},
		{
			data : "roles",
            searchable: false,
		},
		],
		columnDefs : [
			{
				"targets": [0],
				"visible": true
			},
			{
				"targets": [1, 2],
				"visible": false
			},
		],
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
	});
});
$('#search').on( 'keyup click', function () {
	if($(this).val()){
		$('#text_clear').show();
	}
	else {
		$('#text_clear').hide();
	}
	var table = $('#users').DataTable();
	table.tables().search($(this).val()).draw();
});
$('#text_clear').on('click', function () {
	$('#search').val('');
	var table = $('#users').DataTable();
	table.tables().search($(this).val()).draw();
	$('#text_clear').hide();
});
$('#btn_filters').on('click', function() {
	$('#searchPanes').toggleClass('hide_block');
	$('#btn_filters').toggleClass('open');
});
$('.role_checkbox').on('change', function (e) {
    let properties = $('input:checkbox[name="role"]:checked').map(function () {
        return this.value;
    }).get().join('|');
    $('#users').DataTable().column(2).search(properties, true, false, false).draw();
    if (properties) {
        $('#role_clear_btn').removeClass('hide_block');
    } else {
        $('#role_clear_btn').addClass('hide_block');
    }
    saveFilter('users');
});
$('#role_clear_btn').on('click', function () {
    $('#role_clear_btn').addClass('hide_block');
    $('.role_checkbox').prop('checked', false);
    $('#users').DataTable().column(2).search("", true, false, false).draw();
    saveFilter('users');
});
