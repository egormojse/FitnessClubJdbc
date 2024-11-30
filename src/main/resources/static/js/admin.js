document.addEventListener("DOMContentLoaded", function () {
    // Переключение вкладок
    const tabs = document.querySelectorAll('.sidebar li');
    const tabContents = document.querySelectorAll('.tab-content');

    tabs.forEach(tab => {
        tab.addEventListener('click', function () {
            // Удаляем активные классы
            tabs.forEach(t => t.classList.remove('active'));
            tabContents.forEach(content => content.classList.remove('active'));

            // Устанавливаем активный класс
            tab.classList.add('active');
            const targetTab = document.getElementById(tab.getAttribute('data-tab'));
            targetTab.classList.add('active');
        });
    });

    // Инициализация первой вкладки
    document.querySelector('.sidebar li.active').click();

});

// Функция переключения видимости форм редактирования и удаления
function toggleForm(action, type) {
    const editForm = document.getElementById(`edit${capitalize(type)}Form`);
    const deleteForm = document.getElementById(`delete${capitalize(type)}Form`);
    if (action === 'edit') {
        editForm.style.display = editForm.style.display === 'block' ? 'none' : 'block';
        deleteForm.style.display = 'none';
    } else if (action === 'delete') {
        deleteForm.style.display = deleteForm.style.display === 'block' ? 'none' : 'block';
        editForm.style.display = 'none';
    }
}

// Функция очистки кеша Redis
function clearCache() {
    fetch('/clear-redis-cache', {
        method: 'GET',
    })
        .then(response => response.text())
        .then(data => alert(data))
        .catch(error => alert('Ошибка при очистке кэша: ' + error));
}

// Вспомогательная функция для капитализации строки
function capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

// Search Users Functionality
function searchUsers() {
    const input = document.getElementById('userSearch').value.toLowerCase();
    const userCards = document.querySelectorAll('.user-card');

    userCards.forEach(card => {
        const userName = card.textContent.toLowerCase();
        card.style.display = userName.includes(input) ? '' : 'none';
    });
}

// Search Orders Functionality
function searchOrders() {
    const input = document.getElementById('orderSearch').value.toLowerCase();
    const orderCards = document.querySelectorAll('.order-card');

    orderCards.forEach(card => {
        const orderText = card.textContent.toLowerCase();
        card.style.display = orderText.includes(input) ? '' : 'none';
    });
}

// Search Trainings Functionality
function searchTrainings() {
    const input = document.getElementById('trainingSearch').value.toLowerCase();
    const searchCategory = document.getElementById('searchCategory').value.toLowerCase();
    const trainingCards = document.querySelectorAll('.training-card');

    trainingCards.forEach(card => {
        let textToSearch = '';
        if (searchCategory === 'date') {
            textToSearch = card.querySelector('p:nth-child(1)').textContent.toLowerCase(); // Дата
        } else if (searchCategory === 'time') {
            textToSearch = card.querySelector('p:nth-child(2)').textContent.toLowerCase(); // Время
        } else if (searchCategory === 'status') {
            textToSearch = card.querySelector('p:nth-child(3)').textContent.toLowerCase(); // Статус
        } else if (searchCategory === 'user_employee') {
            textToSearch = card.querySelector('p:nth-child(4)').textContent.toLowerCase(); // Пользователь и Сотрудник
        }

        card.style.display = textToSearch.includes(input) ? '' : 'none';
    });
}
function searchSpa() {
    const input = document.getElementById('spaSearch').value.toLowerCase();
    const searchCategory = document.getElementById('searchSpaCategory').value.toLowerCase();
    const spaCards = document.querySelectorAll('.spa-card');

    spaCards.forEach(card => {
        let textToSearch = '';
        if (searchCategory === 'date') {
            textToSearch = card.querySelector('p:nth-child(1)').textContent.toLowerCase(); // Дата
        } else if (searchCategory === 'time') {
            textToSearch = card.querySelector('p:nth-child(2)').textContent.toLowerCase(); // Время
        } else if (searchCategory === 'status') {
            textToSearch = card.querySelector('p:nth-child(3)').textContent.toLowerCase(); // Статус
        } else if (searchCategory === 'user_employee') {
            textToSearch = card.querySelector('p:nth-child(4)').textContent.toLowerCase(); // Пользователь и Сотрудник
        }

        card.style.display = textToSearch.includes(input) ? '' : 'none';
    });
}
