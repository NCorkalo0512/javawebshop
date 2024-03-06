function toggleMenu() {
    const menu = document.querySelector('.side-menu');
    menu.classList.toggle('open');
}

function confirmLogout() {
    if (confirm("Are you sure you want to logout?")) {
        $.ajax({
            url: '/logout',
            type: 'POST',
            success: function() {
                window.location.href = '/login';
            },
            error: function(error) {
                console.error('Error:', error);
            }
        });
    }
}