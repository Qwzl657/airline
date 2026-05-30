<#import "../layout/base.ftlh" as layout>

<@layout.base title="Рейсы">

    <div class="card mb-4 flight-card">
        <div class="card-body">
            <h5 class="card-title">Поиск рейсов</h5>

            <form id="search-form" action="/flights" method="get">
                <div class="row g-2">
                    <div class="col-md-3">
                        <input type="text" name="departureCity"
                               class="form-control"
                               placeholder="Город вылета"
                               value="${departureCity!''}">
                    </div>
                    <div class="col-md-3">
                        <input type="text" name="arrivalCity"
                               class="form-control"
                               placeholder="Город прилёта"
                               value="${arrivalCity!''}">
                    </div>
                    <div class="col-md-2">
                        <input type="date" name="dateFrom"
                               class="form-control"
                               value="${dateFrom!''}">
                    </div>
                    <div class="col-md-2">
                        <input type="date" name="dateTo"
                               class="form-control"
                               value="${dateTo!''}">
                    </div>
                    <div class="col-md-2">
                        <button type="submit" class="btn btn-primary w-100">
                            Найти ✈
                        </button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div id="flights-results" data-user-role="${userRole!'GUEST'}">

        <#if flightsPage.content?has_content>
        <#list flightsPage.content as flight>
            <div class="card mb-3 flight-card">
                <div class="card-body">
                    <div class="row align-items-center">

                        <div class="col-md-2 text-center">
                            <img src="/uploads/logos/${flight.companyLogo!'default.png'}"
                                 alt="${flight.companyName}"
                                 style="max-height:50px; max-width:100px;"
                                 onerror="this.src='/uploads/logos/default.png'">
                            <div class="small text-muted">${flight.companyName}</div>
                        </div>

                        <div class="col-md-4">
                            <div class="d-flex align-items-center gap-3">
                                <div class="text-center">
                                    <div class="fw-bold fs-5">
                                        ${flight.departureTimeFormatted}
                                    </div>
                                    <div class="text-muted">
                                        ${flight.departureCity}
                                    </div>
                                </div>
                                <div class="text-center text-muted">✈ ──────</div>
                                <div class="text-center">
                                    <div class="fw-bold fs-5">
                                        ${flight.arrivalTimeFormatted}
                                    </div>
                                    <div class="text-muted">
                                        ${flight.arrivalCity}
                                    </div>
                                </div>
                            </div>
                            <div class="small text-muted mt-1">
                                ${flight.departureDateShort}
                            </div>
                            <div class="small text-muted">
                                Рейс: ${flight.flightNumber}
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div class="small">🪑 Эконом: ${flight.freeEconomy} мест</div>
                            <div class="small">💼 Бизнес: ${flight.freeBusiness} мест</div>
                            <div class="fw-bold text-primary mt-1">
                                от $${flight.minPrice?string.number}
                            </div>
                        </div>

                        <div class="col-md-3 text-end d-flex flex-column gap-1">
                            <a href="/flights/${flight.id}"
                               class="btn btn-outline-primary btn-sm">
                                Подробнее
                            </a>
                            <#assign isUser = false>
                            <#assign isGuest = true>
                            <#if SPRING_SECURITY_CONTEXT??>
                                <#assign isGuest = false>
                                <#list SPRING_SECURITY_CONTEXT.authentication.principal.authorities as auth>
                                    <#if auth.authority == "ROLE_USER">
                                        <#assign isUser = true>
                                    </#if>
                                </#list>
                            </#if>

                            <#if isGuest>
                                <a href="/auth/login" class="btn btn-success btn-sm">
                                    Забронировать
                                </a>
                            <#elseif isUser>
                                <a href="/flights/${flight.id}"
                                   class="btn btn-success btn-sm">
                                    Забронировать
                                </a>
                            <#else>
                                <button class="btn btn-secondary btn-sm" disabled>
                                    Забронировать
                                </button>
                            </#if>
                        </div>

                    </div>
                </div>
            </div>
        </#list>

            <nav>
                <ul class="pagination justify-content-center">
                    <#list 0..(flightsPage.totalPages - 1) as p>
                        <li class="page-item <#if p == flightsPage.number>active</#if>">
                            <a class="page-link"
                               href="/flights?page=${p}&size=5<#if departureCity??>&departureCity=${departureCity}</#if><#if arrivalCity??>&arrivalCity=${arrivalCity}</#if><#if dateFrom??>&dateFrom=${dateFrom}</#if><#if dateTo??>&dateTo=${dateTo}</#if>">
                                ${p + 1}
                            </a>
                        </li>
                    </#list>
                </ul>
            </nav>

            <#else>
                <div class="alert alert-info">
                    Рейсы не найдены. Попробуйте изменить параметры поиска.
                </div>
            </#if>

    </div>

    <script src="/static/js/search.js"></script>
    <script src="/static/js/live-search.js"></script>

</@layout.base>