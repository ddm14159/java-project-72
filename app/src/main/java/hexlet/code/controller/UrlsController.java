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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import kong.unirest.Unirest;
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
        var checks = url.getChecks();
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
        var name = ctx.formParam("url");

        if (name == null) {
            ctx.redirect(NamedRoutes.urlsPath());
        }

        try {
            var uri = new URI(name);
            var rawUrl = uri.toURL();

            var protocol = rawUrl.getProtocol();
            var host = rawUrl.getHost();
            var port = rawUrl.getPort() == -1 ? "" : ":" + rawUrl.getPort();

            var parsedUrl = protocol + "://" + host + port;

            if (UrlRepository.find(parsedUrl).isPresent()) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.sessionAttribute("flashType", "primary");
                ctx.redirect(NamedRoutes.buildPath());
            } else {
                var url = new Url(parsedUrl);
                UrlRepository.save(url);

                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                ctx.sessionAttribute("flashType", "success");
                ctx.redirect(NamedRoutes.urlsPath());
            }
        } catch (URISyntaxException | MalformedURLException | IllegalArgumentException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flashType", "danger");
            ctx.redirect(NamedRoutes.buildPath());
        }
    }

    public static void check(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));

        var response = Unirest.get(url.getName()).asString();
        var status = response.getStatus();
        var body = response.getBody();
        var document = Jsoup.parse(body);
        var title = document.title();
        var h1 = document.selectFirst("h1") != null
                ? document.selectFirst("h1").text()
                : "";
        var metaTag = document.selectFirst("meta[name=description]");
        var description = metaTag != null && metaTag.hasAttr("content")
                ? metaTag.attr("content")
                : "ааа";

        var check = new UrlCheck(id, status, title, h1, description);
        UrlCheckRepository.save(check);

        ctx.redirect(NamedRoutes.urlPath(id));
    }
}
