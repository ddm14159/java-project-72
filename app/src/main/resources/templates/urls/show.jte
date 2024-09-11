@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes
@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <div class="container">
        <div class="text-center">
            <h1 class="mb-4">Сайт: ${page.getUrl().getName()}</h1>
        </div>

        <div class="row justify-content-center">
            <div class="col-md-6">
                <table class="table table-bordered">
                    <tbody>
                    <tr>
                        <th>ID</th>
                        <td>${page.getUrl().getId()}</td>
                    </tr>
                    <tr>
                        <th>Имя</th>
                        <td>${page.getUrl().getName()}</td>
                    </tr>
                    <tr>
                        <th>Дата создания</th>
                        <td>${page.getUrl().getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="text-center mb-4">
            <form action="${NamedRoutes.checksPath(page.getUrl().getId())}" method="post">
                <button type="submit" class="btn btn-primary">Запустить проверку</button>
            </form>
        </div>

        <div class="row justify-content-center">
            <div class="col-md-10">
                <h2 class="text-center">Проверки</h2>
                <table class="table table-bordered table-striped text-center ">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Код ответа</th>
                        <th>Title</th>
                        <th>H1</th>
                        <th>Description</th>
                        <th>Дата проверки</th>
                    </tr>
                    </thead>
                    <tbody>
                    @for(var check : page.getChecks())
                        <tr>
                            <td>${check.getId()}</td>
                            <td>${check.getStatusCode()}</td>
                            <td>${check.getTitle()}</td>
                            <td>${check.getH1()}</td>
                            <td>${check.getDescription()}</td>
                            <td>${check.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}</td>
                        </tr>
                    @endfor
                    </tbody>
                </table>
            </div>
        </div>
    </div>
`
)
