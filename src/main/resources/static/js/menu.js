let arrow = document.querySelectorAll(".arrow");
for (var i = 0; i < arrow.length; i++) {
	arrow[i].addEventListener("click", (e)=>{
		let arrowParent = e.target.parentElement.parentElement; //selecting main parent of arrow
		arrowParent.classList.toggle("showMenu");
	});
}
let sidebar = document.querySelector("#body");
if (localStorage.getItem('compact_menu')){
	sidebar.classList.add("compact_menu");
}
let sidebarBtn = document.querySelector(".close_menu");
sidebarBtn.addEventListener("click", ()=>{
	sidebar.classList.toggle("compact_menu");
	var check = sidebar.classList.contains("compact_menu");
	localStorage.setItem('compact_menu', check);
});