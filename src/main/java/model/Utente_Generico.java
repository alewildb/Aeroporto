package model;
import java.util.ArrayList;

public class Utente_Generico {
    private String nome;
    private String cognome;
    private String numero_Documento;
    private ArrayList<Prenotazione> prenotazioni = new ArrayList<>(); //verranno create tramite il metodo prenota_Volo()
    public Utente_Generico(String nome, String cognome, String numero_Documento) {
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
}
