document.addEventListener("DOMContentLoaded", function () {
    const tabButtons = document.querySelectorAll(".tab-button");
    const tabContents = document.querySelectorAll(".tab-content");
    const indicators = document.querySelectorAll(".indicator");

    tabButtons.forEach((button) => {
        button.addEventListener("click", function () {
            tabButtons.forEach((btn) => btn.classList.remove("active"));
            tabContents.forEach((content) => content.classList.remove("active"));

            const target = this.getAttribute("data-target");
            this.classList.add("active");
            document.getElementById(target).classList.add("active");
        });
    });

    document.querySelectorAll('.indicator').forEach(indicator => {
        indicator.addEventListener('click', function() {
            const contentId = this.getAttribute('data-toggle');
            const details = document.querySelector(`#${contentId} .details`);

            if (details.classList.contains('hidden')) {
                details.classList.remove('hidden');
                details.classList.add('visible');
                this.classList.add('active');
            } else {
                details.classList.remove('visible');
                details.classList.add('hidden');
                this.classList.remove('active');
            }
        });
    });


    // Валидация форм (универсальная для всех форм)
    const forms = document.querySelectorAll('.form');
    forms.forEach(form => {
        form.addEventListener('submit', function(event) {
            const inputs = form.querySelectorAll('input[type="text"], input[type="email"], input[type="password"]');
            let valid = true;

            inputs.forEach(input => {
                if (input.value.trim() === '') {
                    input.classList.add('error');
                    valid = false;
                } else {
                    input.classList.remove('error');
                }
            });

            if (!valid) {
                event.preventDefault();
                alert('Please fill in all required fields.');
            }
        });
    });
});
document.addEventListener("DOMContentLoaded", () => {
    const toggleBtn = document.querySelector('.toggle-btn');
    const toggleContent = document.querySelector('.toggle-content');
    const toggleBtnIcon = document.querySelector('.toggle-btn-icon');

    let isOpen = false;

    toggleBtn.addEventListener('click', () => {
        if (!isOpen) {
            toggleBtn.style.width = '300px';
            toggleContent.style.display = 'block';
            toggleBtnIcon.innerText = 'Закрыть';
            isOpen = true;
        } else {
            toggleBtn.style.width = '50px';
            toggleContent.style.display = 'none';
            toggleBtnIcon.innerText = 'О нас';
            isOpen = false;
        }
    });
});
