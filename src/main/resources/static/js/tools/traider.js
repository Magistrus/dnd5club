$('#generate').on('click', function() {
	var url = '/tools/trader/result/?result=25&mageRang=1';
	$("#left_content_block").load(url);
});
$('#clear').on('click', function() {
	  const myNode = document.getElementById("content_block");
	  while (myNode.firstChild) {
	    myNode.removeChild(myNode.lastChild);
	  }
});