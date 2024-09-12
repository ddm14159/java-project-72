@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.util.Utils;
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <div class="text-center">
        <h1 class="mb-4">Сайты</h1>
        @if(page.getUrls().isEmpty())
            <p>Список URL пуст</p>
        @else
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Имя</th>
                    <th>Последняя проверка</th>
                    <th>Код ответа</th>
                </tr>
                </thead>
                <tbody>
                @for(var url : page.getUrls())
                    <tr>
                        <td>${url.getId()}</td>
                        <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                        <td>
                            @if(page.getLastChecks().get(url.getId()) != null)
                                ${Utils.getParsedDate(page.getLastChecks().get(url.getId()).getCreatedAt())}
                            @else
                                -
                            @endif
                        </td>
                        <td>
                            @if(page.getLastChecks().get(url.getId()) != null)
                                ${page.getLastChecks().get(url.getId()).getStatusCode()}
                            @else
                                -
                            @endif
                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
        @endif
    </div>
`
)
