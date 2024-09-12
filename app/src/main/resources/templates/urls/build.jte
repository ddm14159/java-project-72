@import hexlet.code.dto.BasePage
@import hexlet.code.util.NamedRoutes
@param BasePage page

@template.layout.page(
page = page,
content = @`
    <div class="text-center">
        <form method="POST" action="${NamedRoutes.urlsPath()}" class="mt-4">
            <div class="mb-3 d-flex justify-content-center align-items-center">
                <input type="text" id="urlInput" name="url" class="form-control me-2 w-50" placeholder="Ссылка">
                <button type="submit" class="btn btn-primary">Проверить</button>
            </div>
        </form>
    </div>
`
)

