document.addEventListener('DOMContentLoaded', () => {
    const sidebar = document.querySelector('.sidebar');
    const toggleButton = document.querySelector('.toggle-sidebar');

    toggleButton.addEventListener('click', () => {
        sidebar.classList.toggle('hidden');
    });
});

$(document).ready(function() {
    $('.tab-content').hide();
    $('.tab-content.active').show();

    $('.tab-button').click(function() {
        var target = $(this).data('target');

        $('.tab-button').removeClass('active');
        $(this).addClass('active');

        $('.tab-content').removeClass('active').hide();
        $('#' + target).addClass('active').show();
    });
});

