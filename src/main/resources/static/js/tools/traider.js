$('#generate').on('click', function() {
	const url = '/tools/trader/result/?result='+ $('#result').val() +'&mageRang=' + $('#mageRang').val();
	$("#trader_content").load(url);
});
$('#clear').on('click', function() {
	  const myNode = document.getElementById("content_block");
	  while (myNode.firstChild) {
	    myNode.removeChild(myNode.lastChild);
	  }
});