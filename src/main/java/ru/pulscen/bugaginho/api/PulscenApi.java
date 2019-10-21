package ru.pulscen.bugaginho.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.intStream;
import static java.util.stream.StreamSupport.stream;

public class PulscenApi {

    private String userCredentials;
    private long userId;

    public PulscenApi(long userId, String userCredentials) {
        this.userId = userId;
        this.userCredentials = userCredentials;
    }

    public List<String> getCompaniesURL() {
        this.userId = userId;
        JsonObject firstPageCompanies = getCompanies(1);
        int totalPages = firstPageCompanies.get("pagination").getAsJsonObject().get("total_pages").getAsInt();
        int totalEntries = firstPageCompanies.get("pagination").getAsJsonObject().get("total_entries").getAsInt();

        List<JsonObject> allPagesCompanies = IntStream.rangeClosed(2, totalPages)
                .mapToObj(this::getCompanies)
                .collect(toList());
        allPagesCompanies.add(firstPageCompanies);

        List<String> urls = allPagesCompanies.stream()
                .flatMap(s -> stream(s.get("companies").getAsJsonArray().spliterator(), true))
                .map(o -> o.getAsJsonObject().get("html_url").getAsString())
                .collect(toList());

        assert urls.size() == totalEntries;
        return urls;
    }

    private JsonObject getCompanies(int page) {
        String body = null;
        try {
            body = Objects.requireNonNull(new OkHttpClient().newCall(new Request.Builder()
                    .url("https://www.pulscen.ru/api/v1/users/" + userId + "/companies?page=" + page)
                    .header("cookie", "user_credentials=" + userCredentials)
                    .get().build()).execute().body()).string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new JsonParser().parse(body).getAsJsonObject();
    }
}
