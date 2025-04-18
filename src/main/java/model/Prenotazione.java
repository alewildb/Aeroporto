package model;
import java.util.ArrayList;
public class Prenotazione {
    private Utente_Generico passeggero; //non final, teoricamente un utente può passare la propria prenotazione a qualcun altro
    private ArrayList<Bagaglio> bagagli = new ArrayList<>(); //Inizialmente vuota, poi al momento del check in verranno aggiunti uno o più bagagli.
    private String nome_Passeggero;
    private String cognome_Passeggero;
    private String numero_Documento_Passeggero;
    private Stato_Prenotazione stato_Prenotazione = Stato_Prenotazione.IN_ATTESA;
    //verranno gestiti in futuro, quando implementeremo il DB.
    private String numero_Biglietto;
    private String posto_Assegnato;

    public Prenotazione(Utente_Generico passeggero) {
        this.nome_Passeggero = passeggero.getNome();
        this.cognome_Passeggero = passeggero.getCognome();
        this.numero_Documento_Passeggero = passeggero.getNumero_Documento();
        this.passeggero = passeggero; //quando viene creata la prenotazione, l'utente passa se stesso, andando a mettere i propri dati all'interno della prenotazione.
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

    //prototipi
    public void check_In(){};
}
