package io.github.lucasduete.github_listrep;

import android.app.IntentService;
import android.content.Intent;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyIntentService extends IntentService {

    private final String TAG = "GITHUB-LISTREP";

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        new Thread(() -> {
            Log.d(TAG, "Entrooooou");

            String busca = intent.getStringExtra("busca");

            final String stringUrl = String.format("https://api.github.com/users/%s/repos", busca);

            try {
                URL urlRequest = new URL(stringUrl);

                HttpURLConnection connection = (HttpURLConnection) urlRequest.openConnection();
                connection.setRequestMethod("GET");

                connection.connect();
                String statusCode = String.valueOf(connection.getResponseCode());
                Log.d(TAG, String.valueOf(connection.getResponseCode()));


                InputStream inputStream = connection.getInputStream();
                if (inputStream == null) {
                    Log.d(TAG, "inputStream null");
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String linha;
                StringBuffer buffer = new StringBuffer();
                while ((linha = reader.readLine()) != null) buffer.append(linha);

                Log.d(TAG, buffer.toString());
                try {
                    JSONArray arrayRepos = new JSONArray(buffer.toString());
                    Log.d(TAG, "Objeto:");
                    Log.d(TAG, arrayRepos.toString());

                    Message message = new Message();
                    message.obj = arrayRepos;

                    MainActivity.mainHandle.sendMessage(message);
                } catch (Exception ex) {
                    Log.d(TAG, "Deu pau na conversao de json");
                    ex.printStackTrace();
                }

                if (buffer.length() == 0) {
                    Log.d(TAG, "Buffet length 0");
                }
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Erro fechando o stream", e);
                    }
                }


            } catch (IOException ex) {
                  ex.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Alooooo");
    }
}
