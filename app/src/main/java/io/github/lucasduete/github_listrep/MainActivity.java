package io.github.lucasduete.github_listrep;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Handler mainHandle;
    private ArrayList<Repositorio> repositorios = new ArrayList<>();

    private MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;

        mainHandle = new MyHandle();

        EditText editText = (EditText) findViewById(R.id.editTextSearch);
        Button searchButton = (Button) findViewById(R.id.buttonSearch);

        searchButton.setOnClickListener((view) -> {
            Intent intent = new Intent(context, MyIntentService.class);
            intent.putExtra("busca", editText.getText().toString());
            startService(intent);
        });

        Button cleanButton = (Button) findViewById(R.id.buttonClean);

        cleanButton.setOnClickListener((view) -> {
            repositorios = new ArrayList<>();
            atualizarListView();
        });


        atualizarListView();
    }

    private class MyHandle extends Handler {

        public MyHandle(){
            super();
        }

        @Override
        public void dispatchMessage(Message msg) {
            ArrayList<Repositorio> repositoriosTemp = new ArrayList<>();
            JSONArray jsonArray = (JSONArray) msg.obj;

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    Repositorio repositorio = new Repositorio();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    repositorio.setNome(jsonObject.getString("full_name"));
                    repositorio.setDescricao(jsonObject.getString("description"));
                    repositorio.setNomeAutor(jsonObject.getJSONObject("owner").getString("login"));
                    repositorio.setFoto(jsonObject.getJSONObject("owner").getString("avatar_url"));

                    repositoriosTemp.add(repositorio);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            repositorios.addAll(repositoriosTemp);
            atualizarListView();
        }
    }

    private void atualizarListView() {
        ListView listView = (ListView) findViewById(R.id.listViewRepos);

        adapter = new MyListAdapter(
                repositorios,
                this
        );

        listView.setAdapter(adapter);
    }

}
