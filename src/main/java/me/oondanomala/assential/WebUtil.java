package me.oondanomala.assential;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class WebUtil {
    private WebUtil() {
    }

    public static String fetchString(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.addRequestProperty("User-Agent", Assential.modName + "/" + Assential.modVersion);
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(10000);

            try (InputStream stream = connection.getInputStream()) {
                return IOUtils.toString(stream, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            Assential.logger.error("Failed to fetch from {}", url, e);
            return null;
        }
    }
}
