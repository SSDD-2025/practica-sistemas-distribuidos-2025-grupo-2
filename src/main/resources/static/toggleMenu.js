document.getElementById('toggle-menu').addEventListener('click', function() {
    let sidebar = document.querySelector('.sidebar');
    let content = document.querySelector('.content-wrapper');
  
    if (sidebar.classList.contains('active')) {
      sidebar.classList.remove('active');
      content.classList.remove('expanded');
    } else {
      sidebar.classList.add('active');
      content.classList.add('expanded');
    }
  });
  