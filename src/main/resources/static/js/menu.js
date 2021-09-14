// document.getElementById('dropdown_character').onclick = function() {
// 	document.getElementById('dropdown_character').classList.toggle('open');
// }
// document.getElementById('dropdown_inventory').onclick = function() {
// 	document.getElementById('dropdown_inventory').classList.toggle('open');
// }
// document.getElementById('close_menu').onclick = function() {
// 	document.getElementById('body').classList.toggle('compact_menu');
// }



let arrow = document.querySelectorAll(".arrow");
	for (var i = 0; i < arrow.length; i++) {
		arrow[i].addEventListener("click", (e)=>{
		let arrowParent = e.target.parentElement.parentElement;//selecting main parent of arrow
		arrowParent.classList.toggle("showMenu");
		});
	}

	let sidebar = document.querySelector("#body");
	let sidebarBtn = document.querySelector(".close_menu");
	console.log(sidebarBtn);
	sidebarBtn.addEventListener("click", ()=>{
	sidebar.classList.toggle("compact_menu");
});