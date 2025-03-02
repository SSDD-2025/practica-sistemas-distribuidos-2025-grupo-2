document.addEventListener("DOMContentLoaded", () => {
  const menuToggle = document.getElementById("toggle-menu");
  const sidebar = document.getElementById("sidebar");
  const contentWrapper = document.querySelector(".content-wrapper");

  menuToggle.addEventListener("click", () => {
    
    sidebar.classList.toggle("active");
    
    contentWrapper.classList.toggle("expanded");
  });
});
