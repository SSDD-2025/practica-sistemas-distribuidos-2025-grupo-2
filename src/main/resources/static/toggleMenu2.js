document.addEventListener("DOMContentLoaded", () => {
  const menuToggle = document.getElementById("toggle-menu");
  const sidebar = document.getElementById("sidebar");
  const contentWrapper = document.querySelector(".content-wrapper");

  menuToggle.addEventListener("click", () => {
    // Alterna la clase .active en la barra lateral (para ocultar/mostrar)
    sidebar.classList.toggle("active");
    // Alterna la clase .expanded en el contenedor de contenido
    contentWrapper.classList.toggle("expanded");
  });
});
