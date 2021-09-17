const container = document.querySelector('.simplebar-content');
const sidebar = document.querySelector('.sub_menu_list');

let timerId = null;

function checkSidebar() {
  const containerRect = container.getBoundingClientRect();
  const sidebarRect = sidebar.getBoundingClientRect();
  
  if (sidebarRect.height < window.innerHeight) {
    sidebar.classList.add('fixedTop');
    sidebar.classList.remove('fixed');
    return;
  }
  
  sidebar.classList.remove('fixedTop');
  
  if (window.innerHeight - containerRect.top >= sidebarRect.height) {
    sidebar.classList.add('fixed');
  } else {
    sidebar.classList.remove('fixed');
  }
}

window.addEventListener('scroll', () => checkSidebar());
window.addEventListener('resize', () => checkSidebar());
const resizeObserver = new ResizeObserver(() => checkSidebar());

resizeObserver.observe(sidebar);

checkSidebar();
