package fi.rectumcere.ostoslista;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
/**
 * shoppinglist app
 * @author Jesse Stenroth
 * @version 1.0
 */
public class haku extends AppCompatActivity {

    private Context tama = this;
    private ArrayList<Tuote> lista;
    private tuotteetMuistissa muisti = new tuotteetMuistissa(this);
    private ListView nakyma;
    private EditText kentta;
    private ostoskori ostokset;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //Main screen activity
                    Intent intent = new Intent(tama, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:
                    //this activity
                    Toast.makeText(tama, "Olet täällä",
                            Toast.LENGTH_LONG).show();
                    return true;
                case R.id.navigation_notifications:
                    //export activity
                    Intent intent2 = new Intent(tama, vienti.class);
                    startActivity(intent2);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haku);
        //Set bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //Get items from memory
        lista = muisti.getListFromStorage(this);
        //Get elements from activity xml
        this.kentta = (EditText) findViewById(R.id.etsi);

        this.nakyma = (ListView) findViewById(R.id.listaNakyma);
        paivitaLista();
        //create new class of shoppinglist
        this.ostokset = new ostoskori(tama);
        //get shoppinglist from memory
        this.ostokset.lataaMuistista();

        //keyboard optimazing
        kentta.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    String syote = kentta.getText().toString();
                    lista = muisti.getListFromStorage(tama);
                    if(!(syote.trim().equals("") || syote == null || syote.isEmpty() || syote.trim().toLowerCase().equals("etsi"))){
                        ArrayList<Tuote> uusi = new ArrayList<>();
                        for(int kierros=0; kierros < lista.size(); kierros++){
                            if(lista.get(kierros).toString().contains(syote) || lista.get(kierros).getLuokka().contains(syote)){
                                uusi.add(lista.get(kierros));
                            }
                        }
                        lista = uusi;
                    }
                    paivitaLista();
                    System.gc();
                    return true;
                }
                return false;
            }
        });
        //Click of list
        nakyma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                //get String what have been clicked
                String entry= parent.getAdapter().getItem(position).toString();
                //Tell item have been added to shoppinglist
                Toast.makeText(tama, "Tuote lisätty",
                        Toast.LENGTH_LONG).show();
                //Get position in Tuote list
                int paikka = -1;
                for(int k=0; k <lista.size();k++){
                    if(lista.get(k).toString().contains(entry.trim())){
                        paikka = k;
                        break;
                    }
                }
                //add item to shopping list
                if(paikka != -1){
                    ostokset.lisaa(lista.get(paikka));
                }
                //save shopping list to memory
                ostokset.tallennaMuistiin();
            }
        });
    }

    /**
     * This method update listview
     */
    private void paivitaLista(){
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            for (int luku = 0; luku < lista.size(); luku++) {
                arrayList.add(lista.get(luku).toString());
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
            nakyma.setAdapter(arrayAdapter);
        } catch (Exception e){

        }
    }

    /**
     *This method search item what contains String on title or category
     * @param view this because I use onclick method on activity xml
     */
    public void etsiTuotteet(View view) {
        String syote = kentta.getText().toString();
        this.lista = muisti.getListFromStorage(this);
        if(!(syote.trim().equals("") || syote == null || syote.isEmpty() || syote.trim().toLowerCase().equals("etsi"))){
            ArrayList<Tuote> uusi = new ArrayList<>();
            for(int kierros=0; kierros < this.lista.size(); kierros++){
                if(this.lista.get(kierros).toString().contains(syote) || this.lista.get(kierros).getLuokka().contains(syote)){
                    uusi.add(this.lista.get(kierros));
                }
            }
            this.lista = uusi;
        }
        paivitaLista();
        //clear RAM
        System.gc();
    }
}
