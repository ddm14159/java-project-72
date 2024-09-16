package hexlet.code.controller;

import static io.javalin.rendering.template.TemplateUtil.model;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.URI;
import java.sql.SQLException;

import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;

public class UrlsController {
    public static void index(Context ctx) throws SQLException {
        var urls = UrlRepository.getEntities();
        var lastChecks = UrlCheckRepository.getLastChecks();
        var page = new UrlsPage(urls, lastChecks);
        String flash = ctx.consumeSessionAttribute("flash");
        String flashType = ctx.consumeSessionAttribute("flashType");
        page.setFlash(flash);
        page.setFlashType(flashType);

        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        var checks = UrlCheckRepository.findByUrl(id);
        var page = new UrlPage(url, checks);

        ctx.render("urls/show.jte", model("page", page));
    }

    public static void build(Context ctx) {
        String flash = ctx.consumeSessionAttribute("flash");
        String flashType = ctx.consumeSessionAttribute("flashType");
        var page = new BasePage();
        page.setFlash(flash);
        page.setFlashType(flashType);

        ctx.render("urls/build.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        URI rawUrl;
        var inputUrl = ctx.formParam("url");

        try {
            rawUrl = new URI(inputUrl);
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(NamedRoutes.buildPath());
            return;
        }

        String parsedUrl = String
                .format(
                        "%s://%s%s",
                        rawUrl.getScheme(),
                        rawUrl.getHost(),
                        rawUrl.getPort() == -1 ? "" : ":" + rawUrl.getPort()
                )
                .toLowerCase();

        Url url = UrlRepository.find(parsedUrl).orElse(null);

        if (url != null) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flashType", "primary");
        } else {
            var newUrl = new Url(parsedUrl);
            UrlRepository.save(newUrl);

            ctx.sessionAttribute("flash", "Страница успешно добавлена");
            ctx.sessionAttribute("flashType", "success");
        }

        ctx.redirect(NamedRoutes.urlsPath());
    }

    public static void check(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        try {
            var response = Unirest.get(url.getName()).asString();
            var status = response.getStatus();
            var body = response.getBody();
            var document = Jsoup.parse(body);
            var title = document.title();
            var h1Element = document.selectFirst("h1");
            var h1 = h1Element != null ? h1Element.text() : "";
            var descElement = document.selectFirst("meta[name=description]");
            var description = descElement != null ? descElement.attr("content") : "";

            var check = new UrlCheck(id, status, title, h1, description);
            UrlCheckRepository.save(check);

            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.sessionAttribute("flash-type", "success");
        } catch (UnirestException e) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("flash-type", "danger");
        } catch (Exception e) {
            ctx.sessionAttribute("flash", e.getMessage());
            ctx.sessionAttribute("flash-type", "danger");
        }

        ctx.redirect(NamedRoutes.urlPath(id));
    }
}
