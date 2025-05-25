package aeroporto.aeroportoPackage.model;

import aeroporto.aeroportoPackage.controller.AppController; // Import AppController
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Utente_Generico extends Utente {
    private String nome;
    private String cognome;
    private String numero_Documento;
    private ArrayList<Prenotazione> prenotazioni_locali = new ArrayList<>();


    public Utente_Generico(String nome, String cognome, String numero_Documento, String login, String password) {
        super(login, password);
        this.nome = nome;
        this.cognome = cognome;
        this.numero_Documento = numero_Documento;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getNumero_Documento() { return numero_Documento; }
    public void setNumero_Documento(String numero_Documento) { this.numero_Documento = numero_Documento; }

    public ArrayList<Prenotazione> getPrenotazioni() {
        return this.prenotazioni_locali;
    }


    public boolean prenota_Volo(AppController appController, Utente_Generico utentePrenotanteEffettivo, Volo voloSelezionato, String nomePasseggeroSpecifico, String cognomePasseggeroSpecifico, String numeroDocumentoPasseggeroSpecifico, String postoAssegnato){
        if (voloSelezionato == null) {
            System.err.println("Utente_Generico_Model: Tentativo di prenotare un volo nullo.");
            return false;
        }
        if (nomePasseggeroSpecifico == null || nomePasseggeroSpecifico.trim().isEmpty() ||
                cognomePasseggeroSpecifico == null || cognomePasseggeroSpecifico.trim().isEmpty() ||
                numeroDocumentoPasseggeroSpecifico == null || numeroDocumentoPasseggeroSpecifico.trim().isEmpty()) {
            System.err.println("Utente_Generico_Model: Dati passeggero incompleti per la prenotazione.");
            return false;
        }

        // Controlla se lo stesso passeggero ha già una prenotazione attiva per quel volo
        List<Prenotazione> tutteLePrenotazioni = appController.internalGetAllPrenotazioni();
        boolean giaPrenotato = tutteLePrenotazioni.stream()
                .anyMatch(p -> {
                    String codiceVoloPrenotato = AppController.extractCodiceVoloFromNumeroBiglietto(p.getNumero_Biglietto());
                    return voloSelezionato.getCodice_Univoco().equals(codiceVoloPrenotato) &&
                            p.getNumero_Documento_Passeggero().equalsIgnoreCase(numeroDocumentoPasseggeroSpecifico) &&
                            p.getStato_Prenotazione() != Stato_Prenotazione.CANCELLATA;
                });

        if (giaPrenotato) {
            System.err.println("Utente_Generico_Model: Il passeggero " + nomePasseggeroSpecifico + " " + cognomePasseggeroSpecifico +
                    " (Doc: " + numeroDocumentoPasseggeroSpecifico + ") ha già una prenotazione attiva per il volo " +
                    voloSelezionato.getCodice_Univoco());
            return false;
        }

        Prenotazione nuovaPrenotazione = new Prenotazione(utentePrenotanteEffettivo); // Chi sta facendo la prenotazione
        nuovaPrenotazione.setNome_Passeggero(nomePasseggeroSpecifico.trim());
        nuovaPrenotazione.setCognome_Passeggero(cognomePasseggeroSpecifico.trim());
        nuovaPrenotazione.setNumero_Documento_Passeggero(numeroDocumentoPasseggeroSpecifico.trim());
        nuovaPrenotazione.setStato_Prenotazione(Stato_Prenotazione.IN_ATTESA);
        if (postoAssegnato != null && !postoAssegnato.trim().isEmpty()) {
            nuovaPrenotazione.setPosto_Assegnato(postoAssegnato.trim().toUpperCase());
        }
        appController.internalAddPrenotazione(nuovaPrenotazione, voloSelezionato.getCodice_Univoco());
        // this.prenotazioni_locali.add(nuovaPrenotazione); // Se volessimo tenere la lista locale
        System.out.println("Utente_Generico_Model: Prenotazione creata da " + utentePrenotanteEffettivo.getLogin() +
                " per passeggero " + nomePasseggeroSpecifico + " sul volo " + voloSelezionato.getCodice_Univoco());
        return true;
    }

    public List<Prenotazione> cerca_PrenotazionePerNome(AppController appController, Utente_Generico utenteLoggato, String nome_Passeggero_Cercato){
        if (nome_Passeggero_Cercato == null || nome_Passeggero_Cercato.trim().isEmpty()) {
            return getMiePrenotazioni(appController, utenteLoggato);
        }
        String searchTerm = nome_Passeggero_Cercato.trim().toLowerCase();
        return getMiePrenotazioni(appController, utenteLoggato).stream()
                .filter(p -> (p.getNome_Passeggero().toLowerCase().contains(searchTerm) ||
                        p.getCognome_Passeggero().toLowerCase().contains(searchTerm)))
                .collect(Collectors.toList());
    }

    public List<Prenotazione> cerca_PrenotazionePerVolo(AppController appController, Utente_Generico utenteLoggato, String codice_Volo_Cercato){
        if (codice_Volo_Cercato == null || codice_Volo_Cercato.trim().isEmpty()) {
            return getMiePrenotazioni(appController, utenteLoggato);
        }
        String searchTerm = codice_Volo_Cercato.trim().toLowerCase();
        return getMiePrenotazioni(appController, utenteLoggato).stream()
                .filter(p -> {
                    String codiceVoloEstetto = AppController.extractCodiceVoloFromNumeroBiglietto(p.getNumero_Biglietto());
                    return codiceVoloEstetto != null && codiceVoloEstetto.toLowerCase().contains(searchTerm);
                })
                .collect(Collectors.toList());
    }

    public boolean segnala_Smarrimento(AppController appController, Utente_Generico utenteLoggato, int codice_Bagaglio_Smarrito){
        Optional<Bagaglio> bagaglioOpt = appController.internalFindBagaglioByCodice(codice_Bagaglio_Smarrito);
        if (bagaglioOpt.isEmpty()) {
            System.err.println("Utente_Generico_Model: Bagaglio " + codice_Bagaglio_Smarrito + " non trovato.");
            return false;
        }
        Bagaglio bagaglio = bagaglioOpt.get();

        // Verifica che il bagaglio appartenga a una prenotazione dell'utente loggato (o per cui l'utente è passeggero)
        Prenotazione p = bagaglio.getPrenotazione();
        if (p == null ||
                !(p.get_Passeggero().equals(utenteLoggato) ||
                        (p.getNome_Passeggero().equalsIgnoreCase(utenteLoggato.getNome()) &&
                                p.getCognome_Passeggero().equalsIgnoreCase(utenteLoggato.getCognome()) &&
                                p.getNumero_Documento_Passeggero().equalsIgnoreCase(utenteLoggato.getNumero_Documento())))
        ) {
            System.err.println("Utente_Generico_Model: Impossibile segnalare smarrimento per bagaglio non appartenente all'utente/passeggero.");
            return false;
        }

        bagaglio.setStato_Bagaglio(Stato_Bagaglio.SMARRITO);
        boolean updated = appController.internalUpdateBagaglio(bagaglio);
        if (updated) {
            appController.internalSegnalaBagaglioSmarrito(bagaglio);
            System.out.println("Utente_Generico_Model: Segnalazione smarrimento per bagaglio " + bagaglio.getCodice_Bagaglio());
        } else {
            System.err.println("Utente_Generico_Model: Errore durante l'aggiornamento dello stato del bagaglio a SMARRITO.");
        }
        return updated;
    }

    public boolean modifica_Prenotazione(AppController appController, Utente_Generico utenteLoggato, Prenotazione prenotazioneAggiornata){
        Optional<Prenotazione> prenotazioneOriginaleOpt = appController.internalFindPrenotazioneByNumeroBiglietto(prenotazioneAggiornata.getNumero_Biglietto());
        if (prenotazioneOriginaleOpt.isEmpty()) {
            System.err.println("Utente_Generico_Model: Prenotazione originale non trovata per la modifica.");
            return false;
        }
        Prenotazione prenotazioneOriginale = prenotazioneOriginaleOpt.get();

        // Verifica proprietà
        if (!(prenotazioneOriginale.get_Passeggero().equals(utenteLoggato) ||
                (prenotazioneOriginale.getNome_Passeggero().equalsIgnoreCase(utenteLoggato.getNome()) &&
                        prenotazioneOriginale.getCognome_Passeggero().equalsIgnoreCase(utenteLoggato.getCognome()) &&
                        prenotazioneOriginale.getNumero_Documento_Passeggero().equalsIgnoreCase(utenteLoggato.getNumero_Documento())))) {
            System.err.println("Utente_Generico_Model: Modifica prenotazione fallita - prenotazione non appartenente all'utente.");
            return false;
        }
        if (prenotazioneOriginale.getStato_Prenotazione() == Stato_Prenotazione.CANCELLATA) {
            System.err.println("Utente_Generico_Model: Impossibile modificare una prenotazione cancellata.");
            return false;
        }

        prenotazioneAggiornata.set_Passeggero(prenotazioneOriginale.get_Passeggero());


        boolean success = appController.internalUpdatePrenotazione(prenotazioneAggiornata);
        if(success) System.out.println("Utente_Generico_Model: Prenotazione " + prenotazioneAggiornata.getNumero_Biglietto() + " modificata.");
        return success;
    }

    public List<Bagaglio> visualizza_BagagliVolo(AppController appController, Utente_Generico utenteLoggato, String codice_Volo){
        List<Bagaglio> result = new ArrayList<>();
        List<Prenotazione> miePrenotazioniPerVolo = cerca_PrenotazionePerVolo(appController, utenteLoggato, codice_Volo);
        for (Prenotazione p : miePrenotazioniPerVolo) {
            result.addAll(p.getBagagli());
        }
        return result;
    }

    public List<Bagaglio> visualizza_BagagliPrenotazione(AppController appController, Utente_Generico utenteLoggato, Prenotazione prenotazione) {
        if (prenotazione == null) return new ArrayList<>();

        if (!(prenotazione.get_Passeggero().equals(utenteLoggato) ||
                (prenotazione.getNome_Passeggero().equalsIgnoreCase(utenteLoggato.getNome()) &&
                        prenotazione.getCognome_Passeggero().equalsIgnoreCase(utenteLoggato.getCognome()) &&
                        prenotazione.getNumero_Documento_Passeggero().equalsIgnoreCase(utenteLoggato.getNumero_Documento())))) {
            return new ArrayList<>();
        }

        return new ArrayList<>(prenotazione.getBagagli());
    }

    public boolean check_In(AppController appController, Prenotazione prenotazione, int numeroBagagliDaRegistrare) {
        if (prenotazione == null ||
                !(prenotazione.get_Passeggero().equals(this) ||
                        (prenotazione.getNome_Passeggero().equalsIgnoreCase(this.getNome()) &&
                                prenotazione.getCognome_Passeggero().equalsIgnoreCase(this.getCognome()) &&
                                prenotazione.getNumero_Documento_Passeggero().equalsIgnoreCase(this.getNumero_Documento())))
        ) {
            System.err.println("Utente_Generico_Model (check_In): Check-in fallito - prenotazione non valida o non appartenente all'utente " + this.getLogin());
            return false;
        }
        if (prenotazione.getStato_Prenotazione() == Stato_Prenotazione.CANCELLATA) {
            System.err.println("Utente_Generico_Model (check_In): Impossibile effettuare il check-in per una prenotazione cancellata.");
            return false;
        }
        if (prenotazione.getStato_Prenotazione() == Stato_Prenotazione.CONFERMATA) {
            System.out.println("Utente_Generico_Model (check_In): Prenotazione " + prenotazione.getNumero_Biglietto() + " già confermata.");
            return true;
        }
        if (numeroBagagliDaRegistrare < 0) {
            System.err.println("Utente_Generico_Model (check_In): Numero di bagagli non valido.");
            return false;
        }

        for (int i = 0; i < numeroBagagliDaRegistrare; i++) {
            Bagaglio nuovoBagaglio = new Bagaglio(appController.getNextBagaglioId(), prenotazione);
            nuovoBagaglio.setStato_Bagaglio(Stato_Bagaglio.CARICATO_AEREO);
            appController.internalAddBagaglio(nuovoBagaglio);
        }
        prenotazione.setStato_Prenotazione(Stato_Prenotazione.CONFERMATA);
        boolean success = appController.internalUpdatePrenotazione(prenotazione);
        if(success) {
            System.out.println("Utente_Generico_Model (check_In): Check-in completato per prenotazione " + prenotazione.getNumero_Biglietto() + " con " + numeroBagagliDaRegistrare + " bagagli.");
        }
        return success;
    }

    public Bagaglio aggiungi_Bagaglio(AppController appController, Utente_Generico utenteLoggato, Prenotazione prenotazione) {
        if (prenotazione == null ||
                !(prenotazione.get_Passeggero().equals(utenteLoggato) ||
                        (prenotazione.getNome_Passeggero().equalsIgnoreCase(utenteLoggato.getNome()) &&
                                prenotazione.getCognome_Passeggero().equalsIgnoreCase(utenteLoggato.getCognome()) &&
                                prenotazione.getNumero_Documento_Passeggero().equalsIgnoreCase(utenteLoggato.getNumero_Documento())))
        ) {
            System.err.println("Utente_Generico_Model (aggiungi_Bagaglio): Impossibile aggiungere bagaglio, prenotazione non dell'utente.");
            return null;
        }
        if (prenotazione.getStato_Prenotazione() == Stato_Prenotazione.CANCELLATA) {
            System.err.println("Utente_Generico_Model (aggiungi_Bagaglio): Impossibile aggiungere bagagli a una prenotazione cancellata.");
            return null;
        }

        if (prenotazione.getStato_Prenotazione() != Stato_Prenotazione.CONFERMATA) {
            System.err.println("Utente_Generico_Model (aggiungi_Bagaglio): È necessario che la prenotazione sia CONFERMATA per aggiungere bagagli extra qui.");
            return null;
        }

        Bagaglio nuovoBagaglio = new Bagaglio(appController.getNextBagaglioId(), prenotazione);
        nuovoBagaglio.setStato_Bagaglio(Stato_Bagaglio.CARICATO_AEREO); // Stato di default per bagaglio aggiunto
        appController.internalAddBagaglio(nuovoBagaglio); // Aggiunge alla lista globale e alla prenotazione
        System.out.println("Utente_Generico_Model (aggiungi_Bagaglio): Bagaglio " + nuovoBagaglio.getCodice_Bagaglio() + " aggiunto alla prenotazione " + prenotazione.getNumero_Biglietto());
        return nuovoBagaglio;
    }

    public boolean cancella_Prenotazione(AppController appController, Utente_Generico utenteLoggato, Prenotazione prenotazioneDaCancellare) {
        if (prenotazioneDaCancellare == null ||
                !(prenotazioneDaCancellare.get_Passeggero().equals(utenteLoggato) ||
                        (prenotazioneDaCancellare.getNome_Passeggero().equalsIgnoreCase(utenteLoggato.getNome()) &&
                                prenotazioneDaCancellare.getCognome_Passeggero().equalsIgnoreCase(utenteLoggato.getCognome()) &&
                                prenotazioneDaCancellare.getNumero_Documento_Passeggero().equalsIgnoreCase(utenteLoggato.getNumero_Documento())))
        ) {
            System.err.println("Utente_Generico_Model (cancella_Prenotazione): Cancellazione fallita - prenotazione non valida o non appartenente all'utente.");
            return false;
        }
        if (prenotazioneDaCancellare.getStato_Prenotazione() == Stato_Prenotazione.CANCELLATA) {
            System.out.println("Utente_Generico_Model (cancella_Prenotazione): Prenotazione " + prenotazioneDaCancellare.getNumero_Biglietto() + " già cancellata.");
            return true;
        }

        prenotazioneDaCancellare.setStato_Prenotazione(Stato_Prenotazione.CANCELLATA);

        boolean success = appController.internalUpdatePrenotazione(prenotazioneDaCancellare);
        if(success) System.out.println("Utente_Generico_Model (cancella_Prenotazione): Prenotazione " + prenotazioneDaCancellare.getNumero_Biglietto() + " cancellata.");
        return success;
    }

    public List<Prenotazione> getMiePrenotazioni(AppController appController, Utente_Generico utenteLoggato) {
        return appController.internalGetAllPrenotazioni().stream()
                .filter(p -> p.get_Passeggero().equals(utenteLoggato) ||
                        (p.getNome_Passeggero().equalsIgnoreCase(utenteLoggato.getNome()) &&
                                p.getCognome_Passeggero().equalsIgnoreCase(utenteLoggato.getCognome()) &&
                                p.getNumero_Documento_Passeggero().equalsIgnoreCase(utenteLoggato.getNumero_Documento())))
                .collect(Collectors.toList());
    }
}