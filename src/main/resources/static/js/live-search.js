'use strict';

let searchTimer = null;
const DEBOUNCE_MS = 500;

window.addEventListener('load', function () {

    const form = document.getElementById('search-form');
    if (!form) return;

    const resultsContainer = document.getElementById('flights-results');
    if (!resultsContainer) return;

    const inputs = form.querySelectorAll('input');

    inputs.forEach(function (input) {
        input.addEventListener('input', function () {

            clearTimeout(searchTimer);

            searchTimer = setTimeout(function () {
                performSearch(form, resultsContainer);
            }, DEBOUNCE_MS);
        });
    });
});

async function performSearch(form, container) {

    const data   = Object.fromEntries(new FormData(form));

    const params = new URLSearchParams();
    if (data.departureCity) params.append('departureCity', data.departureCity);
    if (data.arrivalCity)   params.append('arrivalCity',   data.arrivalCity);
    if (data.dateFrom)      params.append('dateFrom',      data.dateFrom);
    if (data.dateTo)        params.append('dateTo',        data.dateTo);
    params.append('size', '5');

    showLoading(container);

    try {
        const response = await fetch('/api/flights?' + params.toString());

        if (!response.ok) {
            throw new Error('Ошибка сервера: ' + response.status);
        }

        const page = await response.json();

        renderResults(page, container);

    } catch (error) {
        showError(container, 'Не удалось загрузить рейсы. Попробуйте ещё раз.');
        console.error('Ошибка живого поиска:', error);
    }
}

function renderResults(page, container) {

    if (!page.content || page.content.length === 0) {
        container.innerHTML =
            '<div class="alert alert-info">' +
            'Рейсы не найдены. Попробуйте изменить параметры поиска.' +
            '</div>';
        return;
    }

    let html = '';

    page.content.forEach(function (flight) {

        const depTime = formatTime(flight.departureTime);
        const arrTime = formatTime(flight.arrivalTime);
        const depDate = formatDate(flight.departureTime);
        const logo    = flight.companyLogo || 'default.png';

        html +=
            '<div class="card mb-3 flight-card">' +
            '<div class="card-body">' +
            '<div class="row align-items-center">' +

            '<div class="col-md-2 text-center">' +
            '<img src="/uploads/logos/' + logo + '"' +
            ' alt="' + flight.companyName + '"' +
            ' style="max-height:50px; max-width:100px;"' +
            ' onerror="this.src=\'/uploads/logos/default.png\'">' +
            '<div class="small text-muted">' + flight.companyName + '</div>' +
            '</div>' +

            '<div class="col-md-5">' +
            '<div class="d-flex align-items-center gap-3">' +
            '<div class="text-center">' +
            '<div class="fw-bold fs-5">' + depTime + '</div>' +
            '<div class="text-muted">' + flight.departureCity + '</div>' +
            '</div>' +
            '<div class="text-center text-muted">✈ ──────</div>' +
            '<div class="text-center">' +
            '<div class="fw-bold fs-5">' + arrTime + '</div>' +
            '<div class="text-muted">' + flight.arrivalCity + '</div>' +
            '</div>' +
            '</div>' +
            '<div class="small text-muted mt-1">' + depDate + '</div>' +
            '<div class="small text-muted">Рейс: ' + flight.flightNumber + '</div>' +
            '</div>' +

            '<div class="col-md-3">' +
            '<div class="small">🪑 Эконом: ' + flight.freeEconomy + ' мест</div>' +
            '<div class="small">💼 Бизнес: ' + flight.freeBusiness + ' мест</div>' +
            '<div class="fw-bold text-primary mt-1">' +
            'от $' + flight.minPrice.toFixed(2) +
            '</div>' +
            '</div>' +

            '<div class="col-md-2 text-end">' +
            '<a href="/flights/' + flight.id + '"' +
            ' class="btn btn-outline-primary btn-sm">' +
            'Подробнее' +
            '</a>' +
            '</div>' +

            '</div>' +
            '</div>' +
            '</div>';
    });

    if (page.totalPages > 1) {
        html += buildPagination(page);
    }

    container.innerHTML = html;
}

function buildPagination(page) {
    let html = '<nav><ul class="pagination justify-content-center">';
    for (let p = 0; p < page.totalPages; p++) {
        const active = p === page.number ? 'active' : '';
        html +=
            '<li class="page-item ' + active + '">' +
            '<a class="page-link" href="#" data-page="' + p + '">' +
            (p + 1) +
            '</a>' +
            '</li>';
    }
    html += '</ul></nav>';
    return html;
}

function showLoading(container) {
    container.innerHTML =
        '<div class="text-center py-3">' +
        '<div class="spinner-border text-primary" role="status">' +
        '<span class="visually-hidden">Загрузка...</span>' +
        '</div>' +
        '</div>';
}

function showError(container, message) {
    container.innerHTML =
        '<div class="alert alert-danger">' + message + '</div>';
}

function formatTime(isoString) {
    if (!isoString) return '';
    const date = new Date(isoString);
    return String(date.getHours()).padStart(2, '0') + ':' +
        String(date.getMinutes()).padStart(2, '0');
}

function formatDate(isoString) {
    if (!isoString) return '';
    const date    = new Date(isoString);
    const months  = ['Jan','Feb','Mar','Apr','May','Jun',
        'Jul','Aug','Sep','Oct','Nov','Dec'];
    return date.getDate() + ' ' +
        months[date.getMonth()] + ' ' +
        date.getFullYear();
}