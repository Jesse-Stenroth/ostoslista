package fi.rectumcere.ostoslista;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class haku extends AppCompatActivity {

    private Context tama = this;
    private ArrayList<Tuote> lista;
    private tuotteetMuistissa muisti = new tuotteetMuistissa();
    private ListView nakyma;
    private EditText kentta;
    private ostoskori ostokset;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intent = new Intent(tama, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:
                    Toast.makeText(tama, "Olet täällä",
                            Toast.LENGTH_LONG).show();
                    return true;
                case R.id.navigation_notifications:
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        lista = muisti.getListFromStorage(this);
        this.kentta = (EditText) findViewById(R.id.etsi);

        this.nakyma = (ListView) findViewById(R.id.listaNakyma);
        paivitaLista();

        this.ostokset = new ostoskori(tama);
        this.ostokset.lataaMuistista();

        //näppäimistön optimointia
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
        //listan klikkaus
        nakyma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String entry= parent.getAdapter().getItem(position).toString();
                Toast.makeText(tama, "Tuote lisätty",
                        Toast.LENGTH_LONG).show();
                int paikka = -1;
                for(int k=0; k <lista.size();k++){
                    if(lista.get(k).toString().contains(entry.trim())){
                        paikka = k;
                        break;
                    }
                }
                if(paikka != -1){
                    ostokset.lisaa(lista.get(paikka));
                }
            }
        });
    }

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
        System.gc();
    }
}
