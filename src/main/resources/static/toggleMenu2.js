// Script para alternar la barra lateral
document.getElementById("toggle-menu").addEventListener("click", function() {
    const sidebar = document.getElementById("sidebar");
    const container = document.querySelector(".container");
  
    // Alterna la visibilidad de la barra lateral y ajusta el contenido
    sidebar.classList.toggle("open");
    container.classList.toggle("shifted"); // Desplaza el contenido hacia la derecha cuando el menú está abierto
  });
  