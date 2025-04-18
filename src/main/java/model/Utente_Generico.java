package model;
import java.util.ArrayList;

public class Utente_Generico extends Utente{
    private String nome;
    private String cognome;
    private String numero_Documento;
    private ArrayList<Prenotazione> prenotazioni = new ArrayList<>(); //verranno create tramite il metodo prenota_Volo()
    public Utente_Generico(String nome, String cognome, String numero_Documento, String login, String password) {
        super(login, password);
        this.nome = nome;
        this.cognome = cognome;
        this.numero_Documento = numero_Documento;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getCognome() {
        return cognome;
    }
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    public String getNumero_Documento() {
        return numero_Documento;
    }
    public void setNumero_Documento(String numero_Documento) {
        this.numero_Documento = numero_Documento;
    }

    //prototipi
    public void prenota_Volo(){};
    public void cerca_PrenotazionePerNome(String nome_Passeggero){};
    public void cerca_PrenotazionePerVolo(String codice_Volo){};
    public void segnala_Smarrimento(String codice_Bagaglio){};
    public void modifica_Prenotazione(Prenotazione prenotazione){};
    public void visualizza_BagagliVolo(String codice_Volo){};
}
