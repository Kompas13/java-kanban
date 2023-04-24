package manager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String urlServer;
    private String apiToken;

    public KVTaskClient() {
        this.urlServer = "http://localhost:"+KVServer.PORT;
        apiToken = getApiToken();

    }

    private String getApiToken() {
        URI uriRegister = URI.create(urlServer+"/register");

        // создаЄм объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпл€р билдера
                .GET()    // указываем HTTP-метод запроса-
                .uri(uriRegister) // указываем адрес ресурса
                .header("Content-Type", "application/json") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаЄм ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // отправл€ем запрос и получаем ответ от сервера
        HttpResponse<String> response = null;
        try {
            response = client.send(request, handler);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // ѕрисваиваем содержимое тела ответа переменной apiToken и возвращаем еЄ
        return apiToken = response.body();
    }

    public void put (String key, String json) {
        //POST /save/<ключ>?API_TOKEN=
        URI uriSave = URI.create(urlServer+"/save"+key+"?API_TOKEN="+apiToken);

        // создаЄм объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпл€р билдера
                .POST(HttpRequest.BodyPublishers.ofString(json))    // указываем HTTP-метод запроса-
                .uri(uriSave) // указываем адрес ресурса
                .header("Content-Type", "application/json") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаЄм ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // отправл€ем запрос и получаем ответ от сервера
        HttpResponse<String> response = null;
        try {
            response = client.send(request, handler);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String load (String key) {
        //GET /load/<ключ>?API_TOKEN=
        URI uriLoad = URI.create(urlServer+"/load"+key+"?API_TOKEN="+apiToken);

        // создаЄм объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпл€р билдера
                .GET()    // указываем HTTP-метод запроса-
                .uri(uriLoad) // указываем адрес ресурса
                .header("Content-Type", "application/json") // указываем заголовок Accept
                .build(); // заканчиваем настройку и создаЄм ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        // отправл€ем запрос и получаем ответ от сервера
        HttpResponse<String> response = null;
        try {
            response = client.send(request, handler);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // возвращаем ответ
        return response.body();
    }
}