<#import "../layout/base.ftlh" as layout>

<@layout.base title="Вход">

    <div class="row justify-content-center">
        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h4 class="card-title text-center mb-4">✈ Вход</h4>

                    <#if RequestParameters.registered??>
                        <div class="alert alert-success">
                            Регистрация прошла успешно! Войдите в систему.
                        </div>
                    </#if>

                    <#if RequestParameters.error??>
                        <div class="alert alert-danger">
                            Неверный логин или пароль.
                        </div>
                    </#if>

                    <form action="/auth/login" method="post">
                        <input type="hidden"
                               name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>

                        <div class="mb-3">
                            <label class="form-label">Email / Логин</label>
                            <input type="text" name="username"
                                   class="form-control"
                                   placeholder="email или логин"
                                   required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Пароль</label>
                            <input type="password" name="password"
                                   class="form-control" required>
                        </div>
                        <button type="submit"
                                class="btn btn-primary w-100">Войти</button>
                    </form>

                    <hr>
                    <p class="text-center mb-0">
                        Нет аккаунта?
                        <a href="/auth/register">Зарегистрироваться</a>
                    </p>
                </div>
            </div>
        </div>
    </div>

</@layout.base>