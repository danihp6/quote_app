package services;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import models.Quotation;

public class QuotationService {

    public QuotationService() { }

    public Quotation getQuotation(Context context){
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context);

        Quotation quotation = null;
        String body = "";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("api.forismatic.com");
        builder.appendPath("api");
        builder.appendPath("1.0");
        builder.appendPath("");

        String method = prefs.getString("http","0");

        String lang = "ru";
        if(prefs.getString("languages","0").equals("0")) lang = "en";

        if(method.equals("0")) {
            builder.appendQueryParameter("method", "getQuote");
            builder.appendQueryParameter("format", "json");
            builder.appendQueryParameter("lang", lang);
        } else
            body = "method=getQuote&format=json&lang=" + lang;

        try {

            URL url = new URL(builder.build().toString());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            Gson gson = new Gson();
            connection.setDoInput(true);

            if(method.equals("0"))
                connection.setRequestMethod("GET");
            else {
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(body);
                writer.flush();

                writer.close();
            }

            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            quotation = gson.fromJson(reader, Quotation.class);

            reader.close();
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return quotation;
    }
}
