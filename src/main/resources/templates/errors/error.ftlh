<#import "../layout/base.ftlh" as layout>

<@layout.base title="Ошибка ${errorCode!500}">

    <div class="row justify-content-center mt-5">
        <div class="col-md-6 text-center">

            <div class="display-1 fw-bold text-danger">${errorCode!500}</div>

            <h4 class="mt-3">
                <#if errorCode?? && errorCode == 404>
                    Страница не найдена
                <#elseif errorCode?? && errorCode == 409>
                    Конфликт данных
                <#elseif errorCode?? && errorCode == 400>
                    Некорректный запрос
                <#else>
                    Что-то пошло не так
                </#if>
            </h4>

            <div class="alert alert-danger mt-3">
                ${errorMessage!'Произошла непредвиденная ошибка.'}
            </div>

            <a href="/" class="btn btn-primary mt-3">
                ← На главную
            </a>
            <a href="javascript:history.back()"
               class="btn btn-outline-secondary mt-3 ms-2">
                Назад
            </a>

        </div>
    </div>

</@layout.base>