$('#generate').on('click', function() {
	let type = '?level='+$('#level').val()+'&type=' + $('#type').val(); 
	var parent = document.createElement("div");
	parent.classList.add("tools_block_result");
	var element = document.createElement("p");
	parent.appendChild(element);
	fetch('/tools/encounters/random' + type).then(data => data.text()) .then(html => element.innerHTML = html);
    document.getElementById('content_block').prepend(parent);
});
$('#clear').on('click', function() {
	  const myNode = document.getElementById("content_block");
	  while (myNode.firstChild) {
	    myNode.removeChild(myNode.lastChild);
	  }
});
$('#level').change(function() {
	$('#random_encounter_table').attr('href', '/tools/encounters/table?level='+$('#level').val()+'&type=' + $('#type').val())
});
$('#type').change(function() {
	$('#random_encounter_table').attr('href', '/tools/encounters/table?level='+$('#level').val()+'&type=' + $('#type').val())
});