package br.usjt.desmob.geodata;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class GeoDataNetwork {

    public static Pais[] buscarPaises(String url, String regiao) throws IOException {
        OkHttpClient client = new OkHttpClient();
        ArrayList<Pais> paises = new ArrayList<>();

        Request request = new Request.Builder()
                .url(url+regiao)
                .build();

        Response response = client.newCall(request).execute();

        String resultado = response.body().string();

//Todos os campos de Pais carregados
        try {
            JSONArray vetor = new JSONArray(resultado);
            for(int i = 0; i < vetor.length(); i++){
                JSONObject item = (JSONObject) vetor.get(i);
                Pais pais = new Pais();
                try {
                    pais.setArea(item.getInt("area"));
                } catch (Exception e){
                    pais.setArea(0);
                }
                pais.setBandeira(item.getString("flag"));
                pais.setCapital(item.getString("capital"));
                pais.setNome(item.getString("name"));
                pais.setRegiao(item.getString("region"));
                pais.setCodigo3(item.getString("alpha3Code"));
                pais.setSubRegiao(item.getString("subregion"));
                pais.setDemonimo(item.getString("demonym"));
                try {
                    pais.setPopulacao(item.getInt("population"));
                } catch (Exception e) {
                    pais.setPopulacao(0);
                }
                try {
                    pais.setGini(item.getDouble("gini"));

                } catch (Exception e) {
                    pais.setGini(0.0);
                }

                JSONArray latlng = item.getJSONArray("latlng");
                try {
                    pais.setLatitude(latlng.getDouble(0));
                } catch (Exception e) {
                    pais.setLatitude(0);
                }
                try {
                    pais.setLongitude(latlng.getDouble(1));
                } catch (Exception e) {
                    pais.setLongitude(0);
                }

                ArrayList<String> arrayList = new ArrayList<String>();
                JSONObject jsonObject;

                JSONArray jArray = item.getJSONArray("languages");

                for (int x = 0; x < jArray.length(); x++) {
                    jsonObject = jArray.getJSONObject(x);
                    arrayList.add((String) jsonObject.opt("name"));
                    Log.d("idioma", "Idioma " + jsonObject.opt("name").toString());
                }
                pais.setIdiomas(arrayList);

                jArray = item.getJSONArray("timezones");
                arrayList = new ArrayList<>();

                for (int x = 0; x < jArray.length(); x++) {
                    arrayList.add((String) jArray.get(x));
                }
                pais.setFusos(arrayList);

                jArray = item.getJSONArray("topLevelDomain");
                arrayList = new ArrayList<>();

                for (int x = 0; x < jArray.length(); x++) {
                    arrayList.add((String) jArray.get(x));
                }
                pais.setDominios(arrayList);

                jArray = item.getJSONArray("currencies");
                arrayList = new ArrayList<>();

                for (int x = 0; x < jArray.length(); x++) {
                    jsonObject = jArray.getJSONObject(x);

                    for (int y = 0; y < jsonObject.length(); y++) {
                        arrayList.add(jsonObject.getString("name"));
                        Log.d("moeda", jsonObject.getString("name").toString());
                    }
                }
                pais.setMoedas(arrayList);

                jArray = item.getJSONArray("borders");
                arrayList = new ArrayList<>();

                for (int x = 0; x < jArray.length(); x++) {
                    arrayList.add((String) jArray.get(x));
                }
                pais.setFronteiras(arrayList);

                paises.add(pais);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new IOException(e);
        }

        return paises.toArray(new Pais[0]);
    }


    public static boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}