$.sidebarMenu = function(menu) {
  var animationSpeed = 260,
    subMenuSelector = '.sub_menu';

  $(menu).on('click', '.dropdown', function(e) {
    var $this = $(this);
    var checkElement = $this.next();

    if (checkElement.is(subMenuSelector) && checkElement.is(':visible')) {
      checkElement.slideUp(animationSpeed, function() {
        checkElement.removeClass('menu-open');
      });
      checkElement.parent("li").removeClass("showMenu");
    }

    //If the menu is not visible
    else if ((checkElement.is(subMenuSelector)) && (!checkElement.is(':visible'))) {
      //Get the parent menu
      var parent = $this.parents('ul').first();
      //Close all open menus within the parent
      var ul = parent.find('ul:visible').slideUp(animationSpeed);
      //Remove the menu-open class from the parent
      ul.removeClass('menu-open');
      //Get the parent li
      var parent_li = $this.parent("li");

      //Open the target menu and add the menu-open class
      checkElement.slideDown(animationSpeed, function() {
        //Add the class active to the parent li
        checkElement.addClass('menu-open');
        parent.find('li.showMenu').removeClass('showMenu');
        parent_li.addClass('showMenu');
      });
    }
    //if this isn't a link, prevent the page from being redirected
    if (checkElement.is(subMenuSelector)) {
      e.preventDefault();
    }
  });
}

let sidebar = document.querySelector("#body");
if (localStorage.getItem('compact_menu') ==='true'){
	sidebar.classList.add("compact_menu");
}
else {
	sidebar.classList.remove("compact_menu");
}

let sidebarBtn = document.querySelector(".close_menu");
sidebarBtn.addEventListener("click", ()=>{
	sidebar.classList.toggle("compact_menu");
	var check = sidebar.classList.contains("compact_menu");
	localStorage.setItem('compact_menu', check);
});

$(".roll_menu").click(function(){
	$(".navbar").toggleClass("open");
});