package aeroporto.aeroportoPackage.model;

import java.util.ArrayList;
public class Prenotazione {
    private Utente_Generico passeggero; // L'utente che HA EFFETTUATO la prenotazione
    private ArrayList<Bagaglio> bagagli = new ArrayList<>();
    private String nome_Passeggero; // Nome effettivo del passeggero
    private String cognome_Passeggero; // Cognome effettivo del passeggero
    private String numero_Documento_Passeggero; // Documento effettivo del passeggero
    private Stato_Prenotazione stato_Prenotazione = Stato_Prenotazione.IN_ATTESA;
    private String numero_Biglietto;
    private String posto_Assegnato;

    public Prenotazione(Utente_Generico utentePrenotante) {
        this.passeggero = utentePrenotante;
        this.nome_Passeggero = utentePrenotante.getNome();
        this.cognome_Passeggero = utentePrenotante.getCognome();
        this.numero_Documento_Passeggero = utentePrenotante.getNumero_Documento();
    }

    public String getNome_Passeggero() {
        return nome_Passeggero;
    }

    public String getCognome_Passeggero() {
        return cognome_Passeggero;
    }

    public String getNumero_Documento_Passeggero() {
        return numero_Documento_Passeggero;
    }

    public Stato_Prenotazione getStato_Prenotazione() {
        return stato_Prenotazione;
    }

    public Utente_Generico get_Passeggero() {
        return passeggero;
    }

    public void set_Passeggero(Utente_Generico passeggero) {
        this.passeggero = passeggero;
    }

    public void setNome_Passeggero(String nome_Passeggero) {
        this.nome_Passeggero = nome_Passeggero;
    }

    public void setCognome_Passeggero(String cognome_Passeggero) {
        this.cognome_Passeggero = cognome_Passeggero;
    }

    public void setNumero_Documento_Passeggero(String numero_Documento_Passeggero) {
        this.numero_Documento_Passeggero = numero_Documento_Passeggero;
    }

    public void setStato_Prenotazione(Stato_Prenotazione stato_Prenotazione) {
        this.stato_Prenotazione = stato_Prenotazione;
    }

    public void setNumero_Biglietto(String numero_Biglietto) {
        this.numero_Biglietto = numero_Biglietto;
    }

    public void setPosto_Assegnato(String posto_Assegnato) {
        this.posto_Assegnato = posto_Assegnato;
    }

    public ArrayList<Bagaglio> getBagagli() {
        return bagagli;
    }

    public void setBagagli(ArrayList<Bagaglio> bagagli) {
        this.bagagli = bagagli;
    }

    public String getNumero_Biglietto() {
        return numero_Biglietto;
    }

    public String getPosto_Assegnato() {
        return posto_Assegnato;
    }
}