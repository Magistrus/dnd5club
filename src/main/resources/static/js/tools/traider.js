$('#generate').on('click', function() {
	if (!$('#result').val()){
		
	}
	const url = '/tools/trader/result/?result='+ $('#result').val() +'&mageRang=' + $('#mageRang').val();
	$("#trader_content").load(url);
});
$('#clear').on('click', function() {
	  const myNode = document.getElementById("content_block");
	  while (myNode.firstChild) {
	    myNode.removeChild(myNode.lastChild);
	  }
});
$('.item_selected').on('click', function(event) {
	event.target.attr('selected', ' ');
});
$('.spell_selected').on('click', function(event) {
	event.target.attr('selected', ' ');
});
function selectMagicItem(data){
	document.getElementById('item_name').innerHTML = data.name;
	document.getElementById('type').innerHTML = data.type;
	document.getElementById('rarity').innerHTML = data.rarity;
	document.getElementById('attunement').innerHTML = data.attunement;
	document.getElementById('cost').innerHTML = data.cost;
	httpGetImage('/image/MAGIC_ITEM/'+data.id);
	
	var source = '<span class="tip" data-tipped-options="inline: \'inline-tooltip-source-' +data.id+'\'">' + data.bookshort + '</span>';
	source+= '<span id="inline-tooltip-source-'+ data.id + '" style="display: none">' + data.book + '</span>';
	document.getElementById('source').innerHTML = source;
	document.title = data.name;
	history.pushState('data to be passed', '', '/items/magic/' + data.englishName.split(' ').join('_'));
	var url = '/items/magic/fragment/' + data.id;
	$("#content_block").load(url);
}