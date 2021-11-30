$('#generate').on('click', function() {
	if (!$('#result').val()){
		
	}
	const url = '/tools/trader/result/?result='+ $('#result').val() +'&mageRang=' + $('#mageRang').val();
	$("#trader_content").load(url, function() {
		$('table tbody tr.item_selected').on('click', function(event) {
			$('.item_selected').removeClass('selected');
			$(this).addClass('selected');
			selectMagicItem($(this).attr('id'))
		});
		$('table tbody tr.spell_selected').on('click', function(event) {
			$('.spell_selected').removeClass('selected');
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
	const url = '/spells/fragment/' + id;
	$("#content_block").load(url);
}
function selectMagicItem(id){
	var url = '/items/magic/fragment/' + id;
	$("#content_block").load(url);
}