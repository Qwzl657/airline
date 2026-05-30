<#import "../layout/base.ftlh" as layout>

<@layout.base title="Регистрация">

    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h4 class="card-title text-center mb-4">Регистрация</h4>

                    <#if emailError??>
                        <div class="alert alert-danger">${emailError}</div>
                    </#if>
                    <form id="register-form" action="/auth/register" method="post">
                        <input type="hidden"
                               name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>

                        <div class="mb-3">
                            <label class="form-label">Полное имя</label>
                            <input type="text" name="fullName"
                                   class="form-control"
                                   value="${userCreateDto.fullName!''}"
                                   placeholder="Иван Иванов">
                            <div class="invalid-feedback"></div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Email</label>
                            <input type="email" name="email"
                                   class="form-control"
                                   value="${userCreateDto.email!''}"
                                   placeholder="example@mail.com">
                            <div class="invalid-feedback"></div>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Пароль</label>
                            <input type="password" name="password"
                                   class="form-control"
                                   placeholder="Минимум 6 символов">
                            <div class="form-text">Минимум 6 символов</div>
                            <div class="invalid-feedback"></div>
                        </div>

                        <button type="submit"
                                class="btn btn-success w-100">
                            Зарегистрироваться
                        </button>
                    </form>

                    <hr>
                    <p class="text-center mb-0">
                        Уже есть аккаунт?
                        <a href="/auth/login">Войти</a>
                    </p>
                </div>
            </div>
        </div>
    </div>

    <script src="/static/js/validation.js"></script>

</@layout.base>