package fi.rectumcere.ostoslista;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class vienti extends AppCompatActivity {

    private Context tama = this;
    private ostoskori ostokset;
    private ListView nakyma;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //main screen
                    Intent intent = new Intent(tama, MainActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_dashboard:
                    //search screen
                    Intent intent2 = new Intent(tama, haku.class);
                    startActivity(intent2);
                    return true;
                case R.id.navigation_notifications:
                    //this screen
                    Toast.makeText(tama, "Olet täällä",
                            Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vienti);

        //set bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //create shoppinglist
        this.ostokset = new ostoskori(this);
        this.nakyma = (ListView) findViewById(R.id.loppulista);
        //download shoppinglist from memory
        this.ostokset.lataaMuistista();
        asetaTuotteetListalle();

        //set items remove to listview. if more than 1 then n-1
        this.nakyma.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String entry= parent.getAdapter().getItem(position).toString();
                ostokset.poistaListalta(entry);
                asetaTuotteetListalle();
            }
        });

    }

    /**
     * set shoppinglist to listview
     */
    private void asetaTuotteetListalle(){
        try {
            ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ostokset.ulos());
            nakyma.setAdapter(arrayAdapter);
        } catch (Exception e){

        }
    }

    /**
     * remove shopping list from memory
     * @param view this because I use onclick method in activity xml
     */
    public void poistaTiedostoMuistista(View view) {
        this.ostokset.poistaTiedosto();
        this.ostokset.clear();
        asetaTuotteetListalle();
    }

    /**
     * send shoppinglist String to other apps
     * @param view this because I use onclick method in activity xml
     */
    public void laheta(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, ostokset.korinLahetys());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
