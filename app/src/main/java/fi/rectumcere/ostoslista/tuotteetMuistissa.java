package fi.rectumcere.ostoslista;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class tuotteetMuistissa {
    private JSONObject json;
    private JSONArray arrayJSON;
    public ArrayList<Tuote> getListFromStorage(Context context){
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("tuotteet.json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();

                ArrayList<Tuote> lista = new ArrayList<>();
                arrayJSON = new JSONArray(ret);
                for(int luku = 0; luku < arrayJSON.length(); luku++){
                    String syote = "";
                    json = new JSONObject(arrayJSON.getString(luku));
                    lista.add(new Tuote(json.getString("nimi"), json.getString("luokka"), json.getDouble("koko"), json.getString("tyyppi")));
                }
                return lista;
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (JSONException e) {
            Log.e("login activity", "JSON error: " + e.toString());
        }
        return null;
    }
    public void putListToFile(ArrayList<Tuote> lista, Context context){
        String data = "";
        arrayJSON = new JSONArray();
        try {
            for (int kierros = 0; kierros < lista.size(); kierros++) {
                json = new JSONObject();
                Tuote tuote = lista.get(kierros);
                json.put("nimi", tuote.getNimi());
                json.put("luokka", tuote.getLuokka());
                json.put("koko", tuote.getKoko());
                json.put("tyyppi", tuote.getKoon_tyyppi());
                arrayJSON.put(json.toString());
            }
            data = arrayJSON.toString();
        } catch (Exception e){

        }

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("tuotteet.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}