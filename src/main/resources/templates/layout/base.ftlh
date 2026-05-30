<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><#if title??>${title}<#else>AirBooking</#if></title>

    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        body { background-color: #f8f9fa; }
        .navbar-brand { font-weight: bold; color: #0d6efd !important; }
        .flight-card { border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        .booked-seat { background-color: #dc3545; color: white; }
        .free-seat   { background-color: #198754; color: white; }
        .seat-btn    { width: 48px; height: 48px; margin: 3px;
            border-radius: 8px; font-size: 12px; }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm mb-4">
    <div class="container">
        <a class="navbar-brand" href="/">✈ AirBooking</a>

        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">

                <#if springMacroRequestContext??>

                    <#if !SPRING_SECURITY_CONTEXT??>
                        <li class="nav-item">
                            <a class="nav-link" href="/auth/login">Войти</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="/auth/register">Регистрация</a>
                        </li>
                    <#else>
                        <#assign auth = SPRING_SECURITY_CONTEXT.authentication>

                        <#if auth.authorities?seq_contains("ROLE_ADMIN")>
                            <li class="nav-item">
                                <a class="nav-link" href="/admin">Админ панель</a>
                            </li>
                        </#if>

                        <#if auth.authorities?seq_contains("ROLE_COMPANY")>
                            <li class="nav-item">
                                <a class="nav-link" href="/company/flights">Мои рейсы</a>
                            </li>
                        </#if>

                        <#if auth.authorities?seq_contains("ROLE_USER")>
                            <li class="nav-item">
                                <a class="nav-link" href="/profile">Профиль</a>
                            </li>
                        </#if>

                        <li class="nav-item">
                            <form action="/auth/logout" method="post" class="d-inline">
                                <input type="hidden"
                                       name="${_csrf.parameterName}"
                                       value="${_csrf.token}"/>
                                <button class="btn btn-link nav-link"
                                        type="submit">Выйти</button>
                            </form>
                        </li>
                    </#if>
                </#if>

            </ul>
        </div>
    </div>
</nav>

<div class="container">
    <#nested>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>