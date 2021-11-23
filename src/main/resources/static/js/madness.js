$('#generate').on('click', function() {
	var parent = document.createElement("div");
	var element = document.createElement("p");
	parent.appendChild(element);
	var elementin = document.createElement("div");
	element.appendChild(elementin);
	fetch('/tools/madness/random').then(data => data.text()) .then(html => elementin.innerHTML = html);
    document.getElementById('content_block').prepend(parent);
});
$('#clear').on('click', function() {
	  const myNode = document.getElementById("content_block");
	  while (myNode.firstChild) {
	    myNode.removeChild(myNode.lastChild);
	  }
});
