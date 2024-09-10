@import hexlet.code.dto.BasePage
@import hexlet.code.util.NamedRoutes
@param BasePage page

@template.layout.page(
page = page,
content = @`
    <div class="text-center">
        <h3 class="text-body-emphasis">Page analyzer</h3>
        <form method="POST" action="${NamedRoutes.urlsPath()}" class="mt-4">
            <div class="mb-3">
                <label for="urlInput" class="form-label">Enter URL for Analysis</label>
                <input type="text" id="urlInput" name="url" class="form-control" placeholder="Enter URL">
            </div>
            <button type="submit" class="btn btn-primary">Analyze</button>
        </form>
    </div>
`
)
