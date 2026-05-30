<#import "../layout/base.ftlh" as layout>

<@layout.base title="Ошибка">

    <div class="row justify-content-center mt-5">
        <div class="col-md-6 text-center">
            <div class="display-1 text-danger">${errorCode!500}</div>
            <h4 class="mt-3">${errorMessage!'Что-то пошло не так'}</h4>
            <a href="/" class="btn btn-primary mt-3">На главную</a>
        </div>
    </div>

</@layout.base>