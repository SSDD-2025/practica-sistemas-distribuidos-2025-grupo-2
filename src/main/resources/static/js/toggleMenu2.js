document.addEventListener("DOMContentLoaded", () => {
  const menuToggle = document.getElementById("toggle-menu");
  const sidebar = document.getElementById("sidebar");
  const contentWrapper = document.querySelector(".content-wrapper");

  if (!menuToggle || !sidebar) {
    console.error("No se encontró el botón o el sidebar en el DOM");
    return;
  }

  menuToggle.addEventListener("click", () => {
    sidebar.classList.toggle("active");

    if (sidebar.classList.contains("active")) {
      menuToggle.style.left = "260px";
    } else {
      menuToggle.style.left = "10px";
    }
  });
});
