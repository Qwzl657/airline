'use strict';

const SEARCH_KEY = 'flight_search_params';

window.addEventListener('load', function () {

    const form = document.getElementById('search-form');
    if (!form) return;

    restoreSearchParams(form);

    form.addEventListener('submit', function (e) {
        saveSearchParams(form);
    });
});


function saveSearchParams(form) {

    const data = Object.fromEntries(new FormData(form));
    localStorage.setItem(SEARCH_KEY, JSON.stringify(data));
}


function restoreSearchParams(form) {
    const saved = localStorage.getItem(SEARCH_KEY);
    if (!saved) return;

    let params;
    try {

        params = JSON.parse(saved);
    } catch (e) {

        localStorage.removeItem(SEARCH_KEY);
        return;
    }

    Object.keys(params).forEach(function (key) {
        const input = form.querySelector('[name="' + key + '"]');
        if (input && params[key]) {
            input.value = params[key];
        }
    });
}