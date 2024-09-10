@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlsPage
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <div class="text-center">
        <h1>Urls</h1>
        @if(page.getUrls().isEmpty())
            <p>Url list is empty</p>
        @else
            <div class="list-group">
                @for(var url : page.getUrls())
                    <a href="${NamedRoutes.urlPath(url.getId())}" class="list-group-item list-group-item-action">
                        ${url.getName()}
                    </a>
                @endfor
            </div>
        @endif
    </div>
`
)
