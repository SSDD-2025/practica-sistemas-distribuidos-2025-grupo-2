document.addEventListener("DOMContentLoaded", function () {
    const rainContainer = document.querySelector('.rain');
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=<>?/|';
    const numLines = 150; // Aumenté la cantidad de líneas
    const speed = 5; // Puedes ajustar la velocidad para que sea más rápida o más lenta

    function generateRain() {
        for (let i = 0; i < numLines; i++) {
            let line = document.createElement('div');
            let randomText = '';
            const length = Math.floor(Math.random() * 20) + 10;

            // Generar texto aleatorio
            for (let j = 0; j < length; j++) {
                randomText += chars[Math.floor(Math.random() * chars.length)];
            }

            line.textContent = randomText;
            line.style.position = 'absolute';  // Necesario para posicionar las líneas correctamente
            line.style.top = `${-Math.random() * 100}%`;  // Empieza fuera de la pantalla
            line.style.left = `${Math.random() * 100}%`; // Distribuir aleatoriamente en el ancho
            line.style.fontFamily = 'monospace'; // Usamos una fuente monoespaciada para un mejor efecto
            line.style.fontSize = '15px'; // Tamaño de las letras
            line.style.color = 'rgba(31, 231, 138, 0.92)'; // Color verde de la lluvia
            line.style.whiteSpace = 'nowrap'; // Evitar que las líneas se rompan

            // Añadir animación para simular la caída
            line.style.animation = `fall ${Math.random() * 3 + speed}s linear infinite`;
            rainContainer.appendChild(line);
        }
    }

    generateRain();
});
