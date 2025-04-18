package model;

public class Bagaglio {
    private int codice_Bagaglio;
    private Stato_Bagaglio stato_Bagaglio;
    private final Prenotazione prenotazione;

    public Bagaglio(int codice_Bagaglio, Prenotazione prenotazione) {
        this.codice_Bagaglio = codice_Bagaglio;
        this.prenotazione = prenotazione;
    }

    public int getCodice_Bagaglio() {
        return codice_Bagaglio;
    }

    public void setCodice_Bagaglio(int codice_Bagaglio) {
        this.codice_Bagaglio = codice_Bagaglio;
    }

    public Stato_Bagaglio getStato_Bagaglio() {
        return stato_Bagaglio;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setStato_Bagaglio(Stato_Bagaglio stato_Bagaglio) {
        this.stato_Bagaglio = stato_Bagaglio;
    }

    /* Nessun set per la prenotazione, dato che gli viene assegnata in fase di creazione tramite il costruttore, senza poi poter essere modificato.
    (logicamente non avrebbe senso) */
}
