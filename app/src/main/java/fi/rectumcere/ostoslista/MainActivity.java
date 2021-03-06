package fi.rectumcere.ostoslista;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * shoppinglist app
 * @author Jesse Stenroth
 * @version 1.0
 */
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
        //set navigation item for bottom
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //main screen (item adding)
                    Toast.makeText(tama, "Olet täällä",
                            Toast.LENGTH_LONG).show();
                    muisti.putListToFile(lista,tama);
                    return true;
                case R.id.navigation_dashboard:
                    //search of item screen
                    muisti.putListToFile(lista,tama);
                    Intent intent = new Intent(tama, haku.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notifications:
                    //shoppinglist export
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

        //link elements
        this.nimiKentta = (EditText) findViewById(R.id.nimi);
        this.koko = (EditText) findViewById(R.id.koko);
        this.tyyppi = (Spinner) findViewById(R.id.koonTyyppi);
        this.luokka = (Spinner) findViewById(R.id.luokkia);
        //data searching
        lista = muisti.getListFromStorage(this);
    }

    /**
     * Add item to memory
     * @param view this because I use onclick method on activity xml
     */
    public void lisaaTuote(View view) {
        //get values from elements on screen
        String nimi = this.nimiKentta.getText().toString();
        double kokoo = Double.parseDouble(this.koko.getText().toString().trim());
        String type = this.tyyppi.getItemAtPosition(this.tyyppi.getSelectedItemPosition()).toString();
        String luokkia = this.luokka.getItemAtPosition(this.luokka.getSelectedItemPosition()).toString();
        //If lista is not in memory then create new
        if(lista == null){
            lista = new ArrayList<>();
        }
        //Add new item to list
        lista.add(new Tuote(nimi,luokkia,kokoo,type));
        //Tell user item have been added
        Toast.makeText(this, "Tuote lisätty",
                Toast.LENGTH_LONG).show();
    }

    /**
     * This method remove all items from memory
     * @param view this because I use onclick method on activity xml
     */
    public void postaKaikki(View view) {
        //This method remove json file
        this.muisti.poistaTiedosto();
        //tell user items have been removed
        Toast.makeText(this, "Tuotteet poistettu muistista",
                Toast.LENGTH_LONG).show();
        //clear items from RAM
        this.lista.clear();
    }
}
