package fi.rectumcere.ostoslista;
/**
 * shoppinglist app
 * @author Jesse Stenroth
 * @version 1.0
 */
public class Tuote {
    String nimi;
    double koko;
    String koon_tyyppi;
    String luokka;

    /**
     * get category
     * @return category String
     */
    public String getLuokka() {
        return luokka;
    }

    /**
     * set category
     * @param luokka category String
     */
    public void setLuokka(String luokka) {
        this.luokka = luokka;
    }

    /**
     * constructor of Tuote class
     * @param nimea Title
     * @param luokk category
     * @param kokoa size
     * @param tyyppi size type example L or Kg
     */
    public Tuote(String nimea, String luokk, double kokoa, String tyyppi){
        this.nimi = nimea;
        this.koko = kokoa;
        this.koon_tyyppi = tyyppi;
        this.luokka = luokk;
    }

    /**
     * get Title
     * @return title
     */
    public String getNimi() {
        return nimi;
    }

    /**
     * set title
     * @param nimi title
     */
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    /**
     * get size
     * @return size
     */
    public double getKoko() {
        return koko;
    }

    /**
     * set size
     * @param koko size
     */
    public void setKoko(double koko) {
        this.koko = koko;
    }

    /**
     * get size type
     * @return size type
     */
    public String getKoon_tyyppi() {
        return koon_tyyppi;
    }

    /**
     * set size type
     * @param koon_tyyppi size type
     */
    public void setKoon_tyyppi(String koon_tyyppi) {
        this.koon_tyyppi = koon_tyyppi;
    }
    @Override
    public String toString(){
        return this.nimi + " (" + this.koko + " " + this.koon_tyyppi + ")";
    }
}
