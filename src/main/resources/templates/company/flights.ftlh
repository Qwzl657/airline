<#import "../layout/base.ftlh" as layout>

<@layout.base title="Мои рейсы">

    <div class="d-flex justify-content-between align-items-center mb-3">
        <h4>Мои рейсы</h4>
        <a href="/company/flights/new" class="btn btn-success">+ Новый рейс</a>
    </div>

    <#if flightsPage.content?has_content>
        <#list flightsPage.content as flight>
            <div class="card mb-2 flight-card">
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-3">
                            <div class="fw-bold">${flight.flightNumber}</div>
                            <div class="small text-muted">
                                ${flight.departureCity} → ${flight.arrivalCity}
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="small">Вылет: ${flight.departureDatetime}</div>
                            <div class="small">Прилёт: ${flight.arrivalDatetime}</div>
                        </div>
                        <div class="col-md-3">
                            <div class="small">🪑 Эконом: ${flight.freeEconomy} своб.</div>
                            <div class="small">💼 Бизнес: ${flight.freeBusiness} своб.</div>
                        </div>
                        <div class="col-md-2 text-end">
                            <a href="/flights/${flight.id}"
                               class="btn btn-outline-primary btn-sm">
                                Подробнее
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </#list>

        <nav>
            <ul class="pagination">
                <#list 0..(flightsPage.totalPages - 1) as p>
                    <li class="page-item <#if p == flightsPage.number>active</#if>">
                        <a class="page-link"
                           href="/company/flights?page=${p}&size=5">
                            ${p + 1}
                        </a>
                    </li>
                </#list>
            </ul>
        </nav>
    <#else>
        <div class="alert alert-info">У вас пока нет рейсов.</div>
    </#if>

</@layout.base>