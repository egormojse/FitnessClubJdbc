document.addEventListener("DOMContentLoaded", function () {
    // Переключение вкладок
    const tabs = document.querySelectorAll('.tab-button');
    const tabContents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', function () {
            // Удаляем активные классы с вкладок и контента
            tabs.forEach(t => t.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active'));

            // Добавляем активный класс к выбранной вкладке
            tab.classList.add('active');
            const targetId = tab.getAttribute('data-target');
            const targetContent = document.getElementById(targetId);
            if (targetContent) {
                targetContent.classList.add('active');
            }
        });
    });

    // Инициализация первой вкладки
    const initialTab = document.querySelector('.tab-button.active');
    if (initialTab) {
        initialTab.click();
    }
});

