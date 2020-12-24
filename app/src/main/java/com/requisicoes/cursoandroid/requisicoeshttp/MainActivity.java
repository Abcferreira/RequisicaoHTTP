package com.requisicoes.cursoandroid.requisicoeshttp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button botaoRecuperar;
    private TextView textoResultado;
    private EditText editCep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editCep = (EditText) findViewById(R.id.editCep);
        botaoRecuperar = findViewById(R.id.buttonRecuperar);
        textoResultado = findViewById(R.id.textResultado);

        botaoRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTask task = new MyTask();
                String cep = editCep.getText().toString();
                String urlCep = "https://viacep.com.br/ws/" + cep + "/json/";
                task.execute(urlCep);
            }
        });

    }

    class MyTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String stringUrl = strings[0];
            InputStream inputStream;
            InputStreamReader inputStreamReader;
            StringBuffer buffer = null;

            try {

                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                // Recupera os dados em Bytes
                inputStream = conexao.getInputStream();

                //inputStreamReader lê os dados em Bytes e decodifica para caracteres
                inputStreamReader = new InputStreamReader(inputStream);

                //Objeto utilizado para leitura dos caracteres do InpuStreamReader
                BufferedReader reader = new BufferedReader(inputStreamReader);
                buffer = new StringBuffer();
                String linha = "";

                while ((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            String cep = null;
            String logradouro = null;
            String bairro = null;
            String localidade = null;
            String estado = null;
            try {
                JSONObject jsonObject = new JSONObject(resultado);
                cep = jsonObject.getString("cep");
                logradouro = jsonObject.getString("logradouro");
                bairro = jsonObject.getString("bairro");
                localidade = jsonObject.getString("localidade");
                estado = jsonObject.getString("uf");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            textoResultado.setText("CEP: "+cep+"\n"+"Logradouro: "+logradouro+"\n"+"Bairro: "+bairro+"\n"+"Cidade: "+localidade+"\n"+"UF: "+estado);
        }
    }

}
