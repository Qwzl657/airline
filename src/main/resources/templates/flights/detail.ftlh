<#import "../layout/base.ftlh" as layout>

<@layout.base title="Рейс ${flight.flightNumber}">

    <#if bookingError??>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${bookingError}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </#if>

    <#if bookingSuccess??>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${bookingSuccess}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </#if>

    <div class="card flight-card mb-4">
        <div class="card-body">
            <div class="d-flex align-items-center gap-3 mb-3">
                <img src="/uploads/logos/${flight.companyLogo!'default.png'}"
                     alt="${flight.companyName}"
                     style="max-height:60px;"
                     onerror="this.src='/uploads/logos/default.png'">
                <div>
                    <h4 class="mb-0">${flight.companyName}</h4>
                    <span class="text-muted">Рейс: ${flight.flightNumber}</span>
                </div>
            </div>

            <div class="row mb-3">
                <div class="col-md-5 text-center">
                    <div class="fs-3 fw-bold">${flight.departureTimeFormatted}</div>
                    <div class="fs-5">${flight.departureCity}</div>
                    <div class="text-muted">${flight.departureDateLong}</div>
                </div>
                <div class="col-md-2 text-center align-self-center fs-2">✈</div>
                <div class="col-md-5 text-center">
                    <div class="fs-3 fw-bold">${flight.arrivalTimeFormatted}</div>
                    <div class="fs-5">${flight.arrivalCity}</div>
                    <div class="text-muted">${flight.arrivalDateLong}</div>
                </div>
            </div>
        </div>
    </div>

    <h5>Выберите место</h5>

    <#if flight.tickets?has_content>

        <h6 class="text-muted mt-3">🪑 Эконом</h6>
        <div class="d-flex flex-wrap mb-3">
            <#list flight.tickets as ticket>
                <#if ticket.seatClass?string == "ECONOMY">
                    <form action="/bookings/${ticket.id}" method="post">
                        <input type="hidden"
                               name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>
                        <button type="submit"
                                class="btn seat-btn <#if ticket.booked>booked-seat<#else>free-seat</#if>"
                                <#if ticket.booked>disabled</#if>
                                <#if !userRole?? || userRole != "ROLE_USER">disabled</#if>>
                            ${ticket.seatNumber}<br>
                            <small>$${ticket.price?string.number}</small>
                        </button>
                    </form>
                </#if>
            </#list>
        </div>

        <h6 class="text-muted">💼 Бизнес</h6>
        <div class="d-flex flex-wrap mb-3">
            <#list flight.tickets as ticket>
                <#if ticket.seatClass?string == "BUSINESS">
                    <form action="/bookings/${ticket.id}" method="post">
                        <input type="hidden"
                               name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>
                        <button type="submit"
                                class="btn seat-btn <#if ticket.booked>booked-seat<#else>free-seat</#if>"
                                <#if ticket.booked>disabled</#if>
                                <#if !userRole?? || userRole != "ROLE_USER">disabled</#if>>
                            ${ticket.seatNumber}<br>
                            <small>$${ticket.price?string.number}</small>
                        </button>
                    </form>
                </#if>
            </#list>
        </div>

        <div class="d-flex gap-3 mt-2">
            <span class="btn free-seat btn-sm disabled">Свободно</span>
            <span class="btn booked-seat btn-sm disabled">Занято</span>
            <#if !userRole?? || userRole != "ROLE_USER">
                <span class="text-muted small align-self-center">
                    Войдите как пользователь чтобы забронировать
                </span>
            </#if>
        </div>
    </#if>

    <a href="/flights" class="btn btn-outline-secondary mt-3">← Назад к рейсам</a>

</@layout.base>