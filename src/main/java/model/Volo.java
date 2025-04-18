package model;

public class Volo {
    protected String codice_Univoco;
    protected String compagnia_Aerea;
    protected String aeroporto_Partenza;
    protected String aeroporto_Arrivo;
    protected int giorno_Partenza;
    protected int mese_Partenza;
    protected int anno_Partenza;
    protected Stato_Volo stato_Volo = Stato_Volo.PROGRAMMATO;

    public Volo(String codice_Univoco, String compagnia_Aerea, String aeroporto_Partenza, String aeroporto_Arrivo, int giorno_Partenza, int mese_Partenza, int anno_Partenza) {
        this.codice_Univoco = codice_Univoco;
        this.compagnia_Aerea = compagnia_Aerea;
        this.aeroporto_Partenza = aeroporto_Partenza;
        this.aeroporto_Arrivo = aeroporto_Arrivo;
        this.giorno_Partenza = giorno_Partenza;
        this.mese_Partenza = mese_Partenza;
        this.anno_Partenza = anno_Partenza;
    }
    public String getCodice_Univoco() {
        return codice_Univoco;
    }

    public String getCompagnia_Aerea() {
        return compagnia_Aerea;
    }

    public String getAeroporto_Partenza() {
        return aeroporto_Partenza;
    }

    public String getAeroporto_Arrivo() {
        return aeroporto_Arrivo;
    }

    public int getGiorno_Partenza() {
        return giorno_Partenza;
    }

    public int getMese_Partenza() {
        return mese_Partenza;
    }

    public int getAnno_Partenza() {
        return anno_Partenza;
    }

    public Stato_Volo getStato_Volo() {
        return stato_Volo;
    }

    public void setStato_Volo(Stato_Volo stato_Volo) {
        this.stato_Volo = stato_Volo;
    }

    public void setCodice_Univoco(String codice_Univoco) {
        this.codice_Univoco = codice_Univoco;
    }
    public void setCompagnia_Aerea(String compagnia_Aerea) {
        this.compagnia_Aerea = compagnia_Aerea;
    }

    public void setAeroporto_Partenza(String aeroporto_Partenza) {
        this.aeroporto_Partenza = aeroporto_Partenza;
    }

    public void setAeroporto_Arrivo(String aeroporto_Arrivo) {
        this.aeroporto_Arrivo = aeroporto_Arrivo;
    }

    public void setGiorno_Partenza(int giorno_Partenza) {
        this.giorno_Partenza = giorno_Partenza;
    }

    public void setMese_Partenza(int mese_Partenza) {
        this.mese_Partenza = mese_Partenza;
    }

    public void setAnno_Partenza(int anno_Partenza) {
        this.anno_Partenza = anno_Partenza;
    }
}
