package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.util.Utils;
import hexlet.code.repository.UrlRepository;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.sql.SQLException;

public class AppTest {
    private static Javalin app;
    private static MockWebServer mockWebServer;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
    }

    @BeforeAll
    public static void startMock() throws IOException {
        mockWebServer = new MockWebServer();
        var body = Utils.readResourceFile("fixtures/test_page.html");
        mockWebServer.enqueue(new MockResponse().setBody(body));
        mockWebServer.start();
    }

    @AfterAll
    public static void stopMock() throws IOException {
        mockWebServer.shutdown();
        app.stop();
    }

    @Test
    public void testIndex() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Ссылка");
        });
    }

    @Test
    public void testCreate() {
        JavalinTest.test(app, (server, client) -> {
            var url = "http://google.com";
            var requestBody = "url=" + url;

            var response = client.post(NamedRoutes.urlsPath(), requestBody);

            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains(url);

            var entity = UrlRepository.find(url);
            assertThat(entity).isNotEmpty();
        });
    }

    @Test
    public void testCheck() {
        var mockServerUrl = mockWebServer.url("/").toString();

        JavalinTest.test(app, (server, client) -> {
            Url url = new Url(mockServerUrl);
            UrlRepository.save(url);

            client.post(NamedRoutes.checksPath(url.getId()));

            var checkUrl = UrlCheckRepository.findByUrl(url.getId());
            var title = checkUrl.get(0).getTitle();
            var h1 = checkUrl.get(0).getH1();

            assertThat(title).isEqualTo("Some test title");
            assertThat(h1).isEqualTo("Test header");
        });
    }
}
