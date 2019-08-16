package fi.rectumcere.ostoslista;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ostoskori {
    private ArrayList<Tuote> listaus = new ArrayList<>();
    private JSONObject json;
    private JSONArray arrayJSON;
    private Context content;

    /**
     * constructor
     * @param contentti activity Context
     */
    public ostoskori(Context contentti){
        this.content = contentti;
    }

    /**
     * add item Tuote to list
     * @param tuote
     */
    public void lisaa(Tuote tuote){
        this.listaus.add(tuote);
    }

    /**
     * remove item on position paikka
     * @param paikka position
     */
    public void poista(int paikka){
        this.listaus.remove(paikka);
    }

    /**
     * Remove shoppinglist file (json)
     */
    public void poistaTiedosto(){
        File dir = content.getFilesDir();
        File file = new File(dir, "ostoskori.json");
        boolean deleted = file.delete();
        if(deleted){
            //Tell file have been deleted
            Toast.makeText(content, "ostoslista poistettu",
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * give list on String
     * @return String what contains shoppinglist
     */
    public String korinLahetys(){
        ArrayList<String> listt = ulos();
        String apu = "";
        for(int i = 0; i < listt.size(); i++){
            apu += listt.get(i) + "\n";
        }
        return apu;
    }

    /**
     * This method remove item of shoppinglist
     * @param teksti title of remove item
     */
    public void poistaListalta(String teksti){
        //First must take off x element of text
        String[] osat = teksti.split("x");
        String loppuosa = "";
        for(int kierros = 1; kierros < osat.length; kierros++){
            loppuosa += "x" + osat[kierros];
        }
        String apu = "";
        for(int k=4; k < loppuosa.length(); k++){
            char merkki = loppuosa.charAt(k);
            if(!(merkki == ']')){
                apu += merkki;
            }
        }
        apu = apu.trim();
        int paikka = -1;
        //get first item what match
        for(int u=0; u < this.listaus.size(); u++){
            if(this.listaus.get(u).toString().contains(apu)){
                paikka = u;
                break;
            }
        }
        if(!(paikka == -1)){
            this.listaus.remove(paikka);
        }
    }

    /**
     * Clear Tuote list
     */
    public void clear(){
        this.listaus.clear();
    }

    /**
     * give String list of item in shoppinglist
     * @return String Arraylist
     */
    public ArrayList<String> ulos(){
        //help list
        ArrayList<String> apu = new ArrayList<>();
        //list item what have been added
        ArrayList<String> joLisatty = new ArrayList<>();
        for(int i = 0; i < this.listaus.size(); i++){
            Tuote tuote = this.listaus.get(i);
            if(!sisaltaakoListaJo(joLisatty,tuote)){
                apu.add(kuinkaMontaSamaa(tuote) + " x [ " + tuote.toString() + " ]");
                joLisatty.add(tuote.toString());
            }
        }

        return apu;
    }

    /**
     * This method tell if String list contains already String of Tuote
     * @param list Arraylist of String
     * @param y Tuote class item
     * @return true if contains false if not
     */
    private boolean sisaltaakoListaJo(ArrayList<String> list, Tuote y){
        for(int k = 0; k < list.size(); k++){
            if(list.get(k).contains(y.toString())){
                return true;
            }
        }
        return false;
    }

    /**
     * This method tell how many same Tuote item is in list
     * @param tuote Tuote item
     * @return value of items
     */
    private int kuinkaMontaSamaa(Tuote tuote){
        int luku = 0;
        for(int kierros = 0; kierros < this.listaus.size(); kierros++){
            if(this.listaus.get(kierros).toString().contains(tuote.toString())){
                luku++;
            }
        }
        return luku;
    }

    /**
     * download shoppinglist of memory
     */
    public void lataaMuistista(){
        String ret = "";

        try {
            InputStream inputStream = content.openFileInput("ostoskori.json");

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
                    this.listaus = lista;
                }
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (JSONException e) {
            Log.e("login activity", "JSON error: " + e.toString());
        }
    }

    /**
     * save shoppinglist to memory
     */
    public void tallennaMuistiin(){
        String data = "";
        arrayJSON = new JSONArray();
        try {
            for (int kierros = 0; kierros < listaus.size(); kierros++) {
                json = new JSONObject();
                Tuote tuote = listaus.get(kierros);
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
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(content.openFileOutput("ostoskori.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
