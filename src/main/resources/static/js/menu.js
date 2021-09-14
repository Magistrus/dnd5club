let arrow = document.querySelectorAll(".arrow");
for (var i = 0; i < arrow.length; i++) {
	arrow[i].parentElement.parentElement.parentElement.addEventListener("click", (e)=>{
		e.target.parentElement.parentElement.parentElement.classList.toggle("showMenu");
	});
	arrow[i].addEventListener("click", (e)=>{
		let arrowParent = e.target.parentElement.parentElement; //selecting main parent of arrow
		arrowParent.classList.toggle("showMenu");
	});
}

let sidebar = document.querySelector("#body");
let sidebarBtn = document.querySelector(".close_menu");

sidebarBtn.addEventListener("click", ()=>{
	sidebar.classList.toggle("compact_menu");
});