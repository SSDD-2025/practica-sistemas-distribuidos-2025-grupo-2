document.addEventListener("DOMContentLoaded", function () {
    const rainContainer = document.querySelector('.rain');
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=<>?/|';
    const numLines = 150; 
    const speed = 5; 

    function generateRain() {
        for (let i = 0; i < numLines; i++) {
            let line = document.createElement('div');
            let randomText = '';
            const length = Math.floor(Math.random() * 20) + 10;

            
            for (let j = 0; j < length; j++) {
                randomText += chars[Math.floor(Math.random() * chars.length)];
            }

            line.textContent = randomText;
            line.style.position = 'absolute';  
            line.style.top = `${-Math.random() * 100}%`;  
            line.style.left = `${Math.random() * 100}%`; 
            line.style.fontFamily = 'monospace'; 
            line.style.fontSize = '15px'; 
            line.style.color = 'rgba(31, 231, 138, 0.92)'; 
            line.style.whiteSpace = 'nowrap'; 

            
            line.style.animation = `fall ${Math.random() * 3 + speed}s linear infinite`;
            rainContainer.appendChild(line);
        }
    }

    generateRain();
});
