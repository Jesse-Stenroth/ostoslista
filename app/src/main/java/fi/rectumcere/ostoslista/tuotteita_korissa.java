package fi.rectumcere.ostoslista;

import java.util.ArrayList;
import java.util.HashMap;

public class tuotteita_korissa {
    HashMap<String, Integer> map;
    public tuotteita_korissa(){
        this.map = new HashMap();
    }
    public void lisaa(String tuote){
        if(this.map.containsKey(tuote)){
            this.map.put(tuote, this.map.get(tuote) + 1);
        } else{
            this.map.put(tuote, 1);
        }
    }
    public ArrayList<String> getLista(){
        ArrayList<String> lista = new ArrayList<>();
        ArrayList<String> apu = new ArrayList<String>(this.map.keySet());
        for(int kierros=0;kierros < apu.size(); kierros++){
            lista.add(this.map.get(apu.get(kierros)) + " x " + apu.get(kierros));
        }
        apu.clear();
        return lista;
    }
    public int getKoko(){
        return this.map.size();
    }

    public HashMap<Tuote,Integer> getMappi(){
        HashMap<Tuote, Integer> apuMappi = new HashMap();
        ArrayList<Tuote> lista = new Tiedot().getTuotteet();
        ArrayList<String> apu = new ArrayList<String>(this.map.keySet());
        for(int kierros=0;kierros<lista.size();kierros++){
            String nimi = lista.get(kierros).toString();
            for(int k=0;k<apu.size();k++){
                if(nimi.equals(apu.get(k))){
                    apuMappi.put(lista.get(kierros), this.map.get(apu.get(k)));
                    break;
                }
            }
        }
        return apuMappi;
    }
    public void poista(String tu){
        String tuote = "";
        String[] osa = tu.split(" ");
        for(int k=2;k<osa.length-1;k++){
            tuote += osa[k] + " ";
        }
        tuote += osa[osa.length-1];
        if(this.map.containsKey(tuote)){
            if(this.map.get(tuote) > 1){
                System.out.println("pois lause 1");
                this.map.put(tuote, this.map.get(tuote) - 1);
            } else{
                System.out.println("pois lause 2");
                this.map.remove(tuote);
            }
        } else{
            System.out.println("pois lause 3");
        }
    }
}
