package fi.rectumcere.ostoslista;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class haku extends AppCompatActivity {

    private Context tama = this;
    private ArrayList<Tuote> lista;
    private tuotteetMuistissa muisti = new tuotteetMuistissa();
    private ListView nakyma;

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
        this.nakyma = (ListView) findViewById(R.id.listaNakyma);
    }

    private void paivitaLista(){
        ArrayList<String> arrayList = new ArrayList<String>();
        for(int luku = 0; luku < lista.size(); luku++){
            arrayList.add(lista.get(luku).toString());
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        nakyma.setAdapter(arrayAdapter);
    }

}
