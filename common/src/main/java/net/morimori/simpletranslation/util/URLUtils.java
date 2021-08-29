package net.morimori.simpletranslation.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class URLUtils {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.159 Safari/537.36";
    private static final Gson gson = new Gson();

    public static InputStream getStream(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.addRequestProperty("user-agent", USER_AGENT);
        return connection.getInputStream();
    }

    public static JsonObject getJsonResponse(URL url) throws IOException {
        return gson.fromJson(new InputStreamReader(getStream(url), StandardCharsets.UTF_8), JsonObject.class);
    }
}
