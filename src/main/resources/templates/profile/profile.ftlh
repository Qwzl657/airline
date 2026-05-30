<#import "../layout/base.ftlh" as layout>

<@layout.base title="Профиль">

    <div class="row">
        <div class="col-md-4">
            <div class="card shadow-sm mb-3">
                <div class="card-body text-center">
                    <#if user.role?string == "COMPANY">
                        <img src="/uploads/logos/${user.logo!'default.png'}"
                             alt="Логотип"
                             class="rounded mb-3"
                             style="max-height:100px;"
                             onerror="this.src='/uploads/logos/default.png'">

                        <form action="/profile/logo" method="post"
                              enctype="multipart/form-data">
                            <input type="hidden"
                                   name="${_csrf.parameterName}"
                                   value="${_csrf.token}"/>
                            <input type="file" name="logo"
                                   class="form-control form-control-sm mb-2"
                                   accept=".png,.jpg,.jpeg,.gif">
                            <button type="submit"
                                    class="btn btn-outline-primary btn-sm w-100">
                                Обновить логотип
                            </button>
                        </form>
                    </#if>

                    <h5 class="mt-3">${user.fullName}</h5>
                    <p class="text-muted">${user.email}</p>
                    <span class="badge bg-primary">${user.role}</span>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <h5>История бронирований</h5>

            <#if bookings?has_content>
                <#list bookings as b>
                    <div class="card mb-2 flight-card">
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-2 text-center">
                                    <img src="/uploads/logos/${b.companyLogo!'default.png'}"
                                         alt="${b.companyName}"
                                         style="max-height:40px;"
                                         onerror="this.src='/uploads/logos/default.png'">
                                </div>
                                <div class="col-md-6">
                                    <div class="fw-bold">
                                        ${b.departureCity} → ${b.arrivalCity}
                                    </div>
                                    <div class="small text-muted">
                                        Рейс: ${b.flightNumber} |
                                        Место: ${b.seatNumber} (${b.seatClass})
                                    </div>
                                    <div class="small text-muted">
                                        Вылет: ${b.departureTime?string("dd MMM yyyy HH:mm")}
                                    </div>
                                </div>
                                <div class="col-md-4 text-end">
                                    <div class="fw-bold text-primary">
                                        $${b.price?string("0.00")}
                                    </div>
                                    <div class="small text-muted">
                                        Забронировано:
                                        ${b.bookedAt?string("dd.MM.yyyy")}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </#list>
            <#else>
                <div class="alert alert-info">У вас пока нет бронирований.</div>
            </#if>
        </div>
    </div>

</@layout.base>