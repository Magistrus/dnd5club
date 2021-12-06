$('#generate').on('click', function() {
	getName();
	getHabitate();
	getAtmosphere();
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
function getHabitate(){
	const url = '/tools/tavern/habitates/random';
	$("#content_block_habitate").load(url);
}
function getAtmosphere() {
	const url = '/tools/tavern/atmosphere/random';
	$("#content_block_atmosphere").load(url);
}