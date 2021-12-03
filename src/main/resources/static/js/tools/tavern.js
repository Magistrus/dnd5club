$('#generate').on('click', function() {
	getName();
});
$('#clear').on('click', function() {
	  const myNode = document.getElementById("content_block");
	  while (myNode.firstChild) {
	    myNode.removeChild(myNode.lastChild);
	  }
});
function getName(){
	const url = '/tools/tavern/name/';
	$("#content_block_name").load(url);
}