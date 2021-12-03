$('#generate').on('click', function() {
	const url = '/tools/treasury/random/?cr='+ $('#cr').val();
	$("#generate_content_block").load(url, function(){
		$('table tbody tr.item_selected').on('click', function(event) {
			$('.item_selected').removeClass('selected');
			$('.spell_selected').removeClass('selected');
			$(this).addClass('selected');
			selectMagicItem($(this).attr('id'))
		});
		$('table tbody tr.spell_selected').on('click', function(event) {
			$('.spell_selected').removeClass('selected');
			$('.item_selected').removeClass('selected');
			$(this).addClass('selected');
			selectSpell($(this).attr('id'))
		});
	});
});
$('#clear').on('click', function() {
	  const myNode = document.getElementById("content_block");
	  while (myNode.firstChild) {
	    myNode.removeChild(myNode.lastChild);
	  }
});
function selectSpell(id){
	if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
		document.getElementById('list_page_two_block').classList.add('block_information');
	}
	const url = '/spells/fragment/' + id;
	$("#content_block").load(url);
}
function selectMagicItem(id){
	if(!document.getElementById('list_page_two_block').classList.contains('block_information')){
		document.getElementById('list_page_two_block').classList.add('block_information');
	}
	var url = '/items/magic/fragment/' + id;
	$("#content_block").load(url);
}
$('#btn_close').on('click', function() {
	document.getElementById('list_page_two_block').classList.remove('block_information');
	localStorage.removeItem('selected_spell');
});