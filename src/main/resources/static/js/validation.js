'use strict';

window.addEventListener('load', function () {

    const registerForm = document.getElementById('register-form');
    if (registerForm) {
        initRegisterValidation(registerForm);
    }

    const flightForm = document.getElementById('flight-form');
    if (flightForm) {
        initFlightValidation(flightForm);
    }
});

function initRegisterValidation(form) {

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        clearErrors(form);
        let valid = true;

        const fullName = form.querySelector('[name="fullName"]');
        const email    = form.querySelector('[name="email"]');
        const password = form.querySelector('[name="password"]');

        const nameRegex = /^[^\d!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?0-9]+$/;

        if (!fullName.value.trim()) {
            showError(fullName, 'Введите ваше имя');
            valid = false;
        } else if (fullName.value.trim().length < 2) {
            showError(fullName, 'Имя должно содержать минимум 2 символа');
            valid = false;
        } else if (fullName.value.trim().length > 100) {
            showError(fullName, 'Имя не должно превышать 100 символов');
            valid = false;
        } else if (!nameRegex.test(fullName.value.trim())) {
            showError(fullName, 'Имя может содержать только буквы и пробелы');
            valid = false;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!email.value.trim()) {
            showError(email, 'Введите email');
            valid = false;
        } else if (!emailRegex.test(email.value.trim())) {
            showError(email, 'Введите корректный email');
            valid = false;
        }

        if (!password.value) {
            showError(password, 'Введите пароль');
            valid = false;
        } else if (password.value.length < 6) {
            showError(password, 'Пароль должен содержать минимум 6 символов');
            valid = false;
        }

        if (valid) {
            form.submit();
        }
    });
}

function initFlightValidation(form) {

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        clearErrors(form);
        let valid = true;

        const departureCity = form.querySelector('[name="departureCity"]');
        const arrivalCity   = form.querySelector('[name="arrivalCity"]');
        const departureTime = form.querySelector('[name="departureTime"]');
        const arrivalTime   = form.querySelector('[name="arrivalTime"]');

        if (!departureCity.value.trim()) {
            showError(departureCity, 'Укажите город вылета');
            valid = false;
        }

        if (!arrivalCity.value.trim()) {
            showError(arrivalCity, 'Укажите город прилёта');
            valid = false;
        }

        if (
            departureCity.value.trim() &&
            arrivalCity.value.trim() &&
            departureCity.value.trim().toLowerCase() ===
            arrivalCity.value.trim().toLowerCase()
        ) {
            showError(arrivalCity, 'Город прилёта не может совпадать с городом вылета');
            valid = false;
        }

        if (!departureTime.value) {
            showError(departureTime, 'Укажите дату и время вылета');
            valid = false;
        } else {
            const depDate = new Date(departureTime.value);
            const now     = new Date();

            if (depDate <= now) {
                showError(departureTime, 'Время вылета не может быть в прошлом');
                valid = false;
            }
        }

        if (!arrivalTime.value) {
            showError(arrivalTime, 'Укажите дату и время прилёта');
            valid = false;
        } else if (departureTime.value) {
            const depDate   = new Date(departureTime.value);
            const arrDate   = new Date(arrivalTime.value);

            if (arrDate <= depDate) {
                showError(arrivalTime, 'Время прилёта должно быть позже времени вылета');
                valid = false;
            }


            const diffHours = (arrDate - depDate) / (1000 * 60 * 60);
            if (diffHours > 48) {
                showError(arrivalTime, 'Продолжительность рейса не может превышать 48 часов');
                valid = false;
            }
        }

        if (valid) {
            form.submit();
        }
    });
}

function showError(input, message) {
    input.classList.add('is-invalid');
    const div = document.createElement('div');
    div.classList.add('invalid-feedback');
    div.innerText = message;
    input.after(div);
}

function clearErrors(form) {
    form.querySelectorAll('.is-invalid').forEach(function (el) {
        el.classList.remove('is-invalid');
    });
    form.querySelectorAll('.invalid-feedback').forEach(function (el) {
        el.remove();
    });
}