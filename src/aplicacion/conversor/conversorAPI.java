package aplicacion.conversor;
import aplicacion.desarrollo.codigosDeCambio;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class conversorAPI {
    static final String apikey = "09c90c96e38c48e12dc47acc";
    static final String URL = "https://v6.exchangerate-api.com/v6/" + apikey;
    static HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new GsonBuilder().
            setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
    static codigosDeCambio codes;
    static HttpRequest currencyDatarequest = HttpRequest.newBuilder().uri(URI.create(URL + "/codes")).build();

    public String lastExchangeRate1 = null;
    String lastExchangeRate2 = null;

    // currency data -  https://v6.exchangerate-api.com/v6/YOUR-API-KEY/codes
    // pairing       - https://v6.exchangerate-api.com/v6/YOUR-API-KEY/pair/EUR/GBP";
    // pairing numb  - https://v6.exchangerate-api.com/v6/YOUR-API-KEY/pair/EUR/GBP/AMOUNT

    static public void showSupportedCurrencies() {
        System.out.println(" ********** Bienvenido a la aplicación ********* ");
        System.out.println(" ********* Elija alguna de las monedas a convertir ********* ");
        int annex = 4;
        for (int i = 0; i < codes.supportedCodes.length; i++) {
            String s = codes.supportedCodes[i].get(0) + " (" + codes.supportedCodes[i].get(1) + ")";
            System.out.printf("%-30s", s);
            if (i % annex == 0)
                System.out.println();
        }
    }

    static public void requestCurrencyData() {
        try {
            HttpResponse<String> response = client.send(currencyDatarequest, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            System.out.println(response.body());
            codes = gson.fromJson(response.body(), codigosDeCambio.class);
            System.out.println(Arrays.toString(codes.supportedCodes));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean requestExchange(double amount, String from, String to) {
        HttpRequest request = null;
        try {
            if (from != "" && to != "") {
                if (amount == 0)
                    request = HttpRequest.newBuilder().uri(URI.create(URL + "/pair/" + from + "/" + to)).build();
                else
                    request = HttpRequest.newBuilder().uri(URI.create(URL + "/pair/" + from + "/" + to + "/" + amount)).build();
            }
            else {
                if (amount != 0)
                    request = HttpRequest.newBuilder().uri(URI.create(URL + "/pair/" + lastExchangeRate1 + "/" + lastExchangeRate2 + "/" + amount)).build();
            }
            if (request == null)
                return false;

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            codigosDeCambio exchangePair = gson.fromJson(response.body(), codigosDeCambio.class);
            if (Objects.equals(exchangePair.result, "success")) {
                lastExchangeRate1 = from;
                lastExchangeRate2 = to;
                System.out.printf("%.3f%s = %.3f%s (x%.5f) - %s %n",
                        amount, exchangePair.baseCode, exchangePair.conversionResult, exchangePair.targetCode, exchangePair.conversionRate, LocalTime.now());
                return true;
            }
            else
                return false;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
