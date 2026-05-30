<#import "../layout/base.ftlh" as layout>

<@layout.base title="Админ панель">

    <div class="row">
        <div class="col-md-4">
            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <h5>Создать компанию</h5>

                    <#if companyError??>
                        <div class="alert alert-danger">${companyError}</div>
                    </#if>

                    <form action="/admin/companies" method="post">
                        <input type="hidden"
                               name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>

                        <div class="mb-2">
                            <input type="text" name="fullName"
                                   class="form-control"
                                   placeholder="Название компании" required>
                        </div>
                        <div class="mb-2">
                            <input type="email" name="email"
                                   class="form-control"
                                   placeholder="Email" required>
                        </div>
                        <div class="mb-2">
                            <input type="password" name="password"
                                   class="form-control"
                                   placeholder="Пароль (мин. 6)" required>
                        </div>
                        <button type="submit"
                                class="btn btn-success w-100">
                            Создать
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <h5>Компании</h5>

            <#if freezeError??>
                <div class="alert alert-warning">${freezeError}</div>
            </#if>

            <#if companies.content?has_content>
                <#list companies.content as company>
                    <div class="card mb-2">
                        <div class="card-body d-flex
                                justify-content-between align-items-center">
                            <div>
                                <div class="fw-bold">${company.fullName}</div>
                                <div class="text-muted small">${company.email}</div>
                                <div class="small">
                                    Бронирований: ${company.bookingCount!0}
                                </div>
                            </div>
                            <div class="d-flex gap-2 align-items-center">

                                <#if company.enabled>
                                    <span class="badge bg-success">Активна</span>
                                <#else>
                                    <span class="badge bg-danger">Заморожена</span>
                                </#if>

                                <form action="/admin/companies/${company.id}/toggle"
                                      method="post">
                                    <input type="hidden"
                                           name="${_csrf.parameterName}"
                                           value="${_csrf.token}"/>
                                    <button type="submit"
                                            class="btn btn-sm
                                        <#if company.enabled>
                                            btn-outline-danger
                                        <#else>
                                            btn-outline-success
                                        </#if>">
                                        <#if company.enabled>
                                            Заморозить
                                        <#else>
                                            Разморозить
                                        </#if>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </#list>

                <nav>
                    <ul class="pagination">
                        <#list 0..(companies.totalPages - 1) as p>
                            <li class="page-item
                            <#if p == companies.number>active</#if>">
                                <a class="page-link"
                                   href="/admin?page=${p}&size=10">
                                    ${p + 1}
                                </a>
                            </li>
                        </#list>
                    </ul>
                </nav>
            </#if>
        </div>
    </div>

</@layout.base>