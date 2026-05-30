<#import "../layout/base.ftlh" as layout>

<@layout.base title="Новый рейс">

    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Создать рейс</h5>

                    <#if flightError??>
                        <div class="alert alert-danger">${flightError}</div>
                    </#if>

                    <form action="/company/flights/new" method="post">
                        <input type="hidden"
                               name="${_csrf.parameterName}"
                               value="${_csrf.token}"/>

                        <div class="mb-3">
                            <label class="form-label">Город вылета</label>
                            <input type="text" name="departureCity"
                                   class="form-control"
                                   value="${flightCreateDto.departureCity!''}"
                                   required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Город прилёта</label>
                            <input type="text" name="arrivalCity"
                                   class="form-control"
                                   value="${flightCreateDto.arrivalCity!''}"
                                   required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Дата и время вылета</label>
                            <input type="datetime-local" name="departureTime"
                                   class="form-control"
                                   value="${flightCreateDto.departureTime!''}"
                                   required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Дата и время прилёта</label>
                            <input type="datetime-local" name="arrivalTime"
                                   class="form-control"
                                   value="${flightCreateDto.arrivalTime!''}"
                                   required>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit"
                                    class="btn btn-success">
                                Создать рейс
                            </button>
                            <a href="/company/flights"
                               class="btn btn-outline-secondary">
                                Отмена
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

</@layout.base>