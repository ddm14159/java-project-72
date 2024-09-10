@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <div class="text-center">
        <h1>${page.getUrl().getName()}</h1>
    </div>
`
)
