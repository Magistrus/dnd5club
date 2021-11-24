$('#generate').on('click', function() {
	let type = '?type=base'; 
	if (document.getElementById('additional').checked){
		type = '?type=add';
	}
	var parent = document.createElement("div");
	parent.classList.add("tools_block_result");
	var element = document.createElement("p");
	parent.appendChild(element);
	fetch('/tools/wildmagic/random' + type).then(data => data.text()) .then(html => element.innerHTML = html);
    document.getElementById('content_block').prepend(parent);
});
$('#clear').on('click', function() {
	  const myNode = document.getElementById("content_block");
	  while (myNode.firstChild) {
	    myNode.removeChild(myNode.lastChild);
	  }
});