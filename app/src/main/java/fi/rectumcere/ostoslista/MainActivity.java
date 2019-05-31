package fi.rectumcere.ostoslista;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context tama = this;
    private EditText nimiKentta;
    private EditText koko;
    private Spinner tyyppi;
    private Spinner luokka;
    private ArrayList<Tuote> lista;
    private tuotteetMuistissa muisti = new tuotteetMuistissa(this);


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Toast.makeText(tama, "Olet täällä",
                            Toast.LENGTH_LONG).show();
                    muisti.putListToFile(lista,tama);
                    return true;
                case R.id.navigation_dashboard:
                    muisti.putListToFile(lista,tama);
                    Intent intent = new Intent(tama, haku.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    muisti.putListToFile(lista,tama);
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
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //elementtien linkittäminen
        this.nimiKentta = (EditText) findViewById(R.id.nimi);
        this.koko = (EditText) findViewById(R.id.koko);
        this.tyyppi = (Spinner) findViewById(R.id.koonTyyppi);
        this.luokka = (Spinner) findViewById(R.id.luokkia);
        //datan haku
        lista = muisti.getListFromStorage(this);
    }


    public void lisaaTuote(View view) {
        String nimi = this.nimiKentta.getText().toString();
        double kokoo = Double.parseDouble(this.koko.getText().toString().trim());
        String type = this.tyyppi.getItemAtPosition(this.tyyppi.getSelectedItemPosition()).toString();
        String luokkia = this.luokka.getItemAtPosition(this.luokka.getSelectedItemPosition()).toString();
        if(lista == null){
            lista = new ArrayList<>();
        }

        lista.add(new Tuote(nimi,luokkia,kokoo,type));
        Toast.makeText(this, "Tuote lisätty",
                Toast.LENGTH_LONG).show();
    }

    public void postaKaikki(View view) {
        this.muisti.poistaTiedosto();
        Toast.makeText(this, "Tuotteet poistettu muistista",
                Toast.LENGTH_LONG).show();
    }
}
