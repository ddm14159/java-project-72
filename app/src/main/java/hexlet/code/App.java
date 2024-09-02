package hexlet.code;

import io.javalin.Javalin;

public class App {
    public static Javalin getApp() {
        var app = Javalin.create()
                .get("/", ctx -> ctx.result("Hello World"))
                .start(7070);

        return app;
    }

    public static void main(String[] args) {
        var app = getApp();

        app.start();
    }
}
