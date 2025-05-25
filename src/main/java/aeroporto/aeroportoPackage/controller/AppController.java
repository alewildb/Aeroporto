package aeroporto.aeroportoPackage.controller;

import aeroporto.aeroportoPackage.model.*;
import aeroporto.aeroportoPackage.gui.MainFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AppController {
    private MainFrame mainFrame;
    private Utente utenteCorrente;

    // --- Inizio Dati ---
    private final List<Utente> utenti;
    private final List<Volo> voli;
    private final List<Bagaglio> bagagli;
    private final List<Prenotazione> prenotazioni;
    private final List<Gate> gates;
    private final List<Bagaglio> bagagliSmarriti;

    private final AtomicInteger bagaglioIdCounter = new AtomicInteger(1001);
    private final AtomicInteger prenotazioneSequentialId = new AtomicInteger(1);

    private void loadInitialData() {
        Gate g1 = new Gate(1);
        Gate g2 = new Gate(2);
        Gate g3 = new Gate(3);
        this.gates.add(g1);
        this.gates.add(g2);
        this.gates.add(g3);

        Amministratore admin1 = new Amministratore("admin", "admin123");
        Utente_Generico user1 = new Utente_Generico("Mario", "Rossi", "AA12345BB", "mario", "passrossi");
        Utente_Generico user2 = new Utente_Generico("Luisa", "Bianchi", "CC67890DD", "luisa", "passbianchi");
        this.utenti.add(admin1);
        this.utenti.add(user1);
        this.utenti.add(user2);

        Volo_Partenza vp1 = new Volo_Partenza("AZ201", "ITA Airways", "New York JFK", 25, 12, 2025, "14:30", 0, g1);
        Volo_Arrivo va1 = new Volo_Arrivo("LH500", "Lufthansa", "Francoforte FRA", 10, 1, 2026, "09:15", 0);
        va1.setStato_Volo(Stato_Volo.ATTERRATO);
        Volo_Partenza vp2 = new Volo_Partenza("FR123", "Ryanair", "Londra STN", 5, 6, 2025, "10:00", 15, g2);
        vp2.setStato_Volo(Stato_Volo.IN_RITARDO);
        Volo_Partenza vp3 = new Volo_Partenza("EK098", "Emirates", "Dubai DXB", 15, 7, 2025, "22:00", 0, g3);

        this.voli.add(vp1);
        this.voli.add(va1);
        this.voli.add(vp2);
        this.voli.add(vp3);

        Prenotazione pren1 = new Prenotazione(user1);
        pren1.setNumero_Biglietto(generateNumeroBigliettoConCodiceVolo(vp1.getCodice_Univoco()));
        pren1.setStato_Prenotazione(Stato_Prenotazione.CONFERMATA);
        pren1.setPosto_Assegnato("22A");
        addPrenotazioneInternal(pren1);

        Prenotazione pren2 = new Prenotazione(user2);
        pren2.setNumero_Biglietto(generateNumeroBigliettoConCodiceVolo(vp2.getCodice_Univoco()));
        pren2.setStato_Prenotazione(Stato_Prenotazione.IN_ATTESA);
        pren2.setPosto_Assegnato("10C");
        addPrenotazioneInternal(pren2);

        Prenotazione pren3 = new Prenotazione(user1);
        pren3.setNome_Passeggero("Anna");
        pren3.setCognome_Passeggero("Verdi");
        pren3.setNumero_Documento_Passeggero("EE98765FF");
        pren3.setNumero_Biglietto(generateNumeroBigliettoConCodiceVolo(vp3.getCodice_Univoco()));
        pren3.setStato_Prenotazione(Stato_Prenotazione.IN_ATTESA);
        pren3.setPosto_Assegnato("15B");
        addPrenotazioneInternal(pren3);

        Bagaglio bag1 = new Bagaglio(getNextBagaglioId(), pren1);
        bag1.setStato_Bagaglio(Stato_Bagaglio.CARICATO_AEREO);
        addBagaglioInternal(bag1);
        Bagaglio bag2 = new Bagaglio(getNextBagaglioId(), pren1);
        bag2.setStato_Bagaglio(Stato_Bagaglio.RITIRO_DISPONIBILE);
        addBagaglioInternal(bag2);
        Bagaglio bag3 = new Bagaglio(getNextBagaglioId(), pren2);
        bag3.setStato_Bagaglio(Stato_Bagaglio.SMARRITO);
        addBagaglioInternal(bag3);
        this.bagagliSmarriti.add(bag3);
    }

    public String generateNumeroBigliettoConCodiceVolo(String codiceVolo) {
        if (codiceVolo == null || codiceVolo.trim().isEmpty()) {
            codiceVolo = "SCONOSCIUTO";
        }
        return "TKT" + prenotazioneSequentialId.getAndIncrement() + "-" + codiceVolo.toUpperCase();
    }

    public static String extractCodiceVoloFromNumeroBiglietto(String numeroBiglietto) {
        if (numeroBiglietto != null && numeroBiglietto.contains("-")) {
            try {
                return numeroBiglietto.substring(numeroBiglietto.lastIndexOf("-") + 1);
            } catch (IndexOutOfBoundsException e) {
                System.err.println("AppController (extractCodiceVolo): Formato numero biglietto non valido: " + numeroBiglietto);
                return null;
            }
        }
        return null;
    }

    private void addPrenotazioneInternal(Prenotazione prenotazione) {
        this.prenotazioni.add(prenotazione);
        Utente_Generico utentePrenotante = prenotazione.get_Passeggero();
        if (utentePrenotante != null && utentePrenotante.getPrenotazioni() != null) {
            if (!utentePrenotante.getPrenotazioni().contains(prenotazione)) {
                utentePrenotante.getPrenotazioni().add(prenotazione);
            }
        }
    }

    private void addBagaglioInternal(Bagaglio bagaglio) {
        this.bagagli.add(bagaglio);
        Prenotazione prenotazioneAssociata = bagaglio.getPrenotazione();
        if (prenotazioneAssociata != null && prenotazioneAssociata.getBagagli() != null) {
            if (!prenotazioneAssociata.getBagagli().contains(bagaglio)) {
                prenotazioneAssociata.getBagagli().add(bagaglio);
            }
        }
    }

    public List<Utente> internalGetAllUtenti() { return new ArrayList<>(this.utenti); }
    public Optional<Utente> internalFindUtenteByLogin(String login) {
        return this.utenti.stream()
                .filter(u -> u.getLogin().equalsIgnoreCase(login))
                .findFirst();
    }
    public boolean internalAddUtente(Utente utente) {
        if (internalFindUtenteByLogin(utente.getLogin()).isPresent()) { return false; }
        this.utenti.add(utente);
        return true;
    }

    public List<Volo> internalGetAllVoli() { return new ArrayList<>(this.voli); }
    public Optional<Volo> internalFindVoloByCodice(String codiceVolo) {
        return this.voli.stream()
                .filter(v -> v.getCodice_Univoco().equalsIgnoreCase(codiceVolo))
                .findFirst();
    }
    public void internalAddVolo(Volo volo) {
        this.voli.add(volo);
    }
    public boolean internalUpdateVolo(Volo voloAggiornato) {
        Optional<Volo> voloEsistenteOpt = internalFindVoloByCodice(voloAggiornato.getCodice_Univoco());
        if (voloEsistenteOpt.isPresent()) {
            this.voli.remove(voloEsistenteOpt.get());
            this.voli.add(voloAggiornato);
            return true;
        }
        return false;
    }
    public boolean internalRemoveVolo(String codiceVolo) {
        return this.voli.removeIf(v -> v.getCodice_Univoco().equalsIgnoreCase(codiceVolo));
    }

    public List<Prenotazione> internalGetAllPrenotazioni() { return new ArrayList<>(this.prenotazioni); }
    public Optional<Prenotazione> internalFindPrenotazioneByNumeroBiglietto(String numeroBiglietto) {
        return this.prenotazioni.stream()
                .filter(p -> p.getNumero_Biglietto() != null && p.getNumero_Biglietto().equalsIgnoreCase(numeroBiglietto))
                .findFirst();
    }
    public void internalAddPrenotazione(Prenotazione prenotazione, String codiceVoloAssociato) {
        prenotazione.setNumero_Biglietto(generateNumeroBigliettoConCodiceVolo(codiceVoloAssociato));
        addPrenotazioneInternal(prenotazione);
    }
    public boolean internalUpdatePrenotazione(Prenotazione prenotazioneAggiornata) {
        for (int i = 0; i < this.prenotazioni.size(); i++) {
            if (this.prenotazioni.get(i).getNumero_Biglietto().equals(prenotazioneAggiornata.getNumero_Biglietto())) {
                this.prenotazioni.set(i, prenotazioneAggiornata);
                return true;
            }
        }
        System.err.println("AppController (internalUpdatePrenotazione): Nessuna prenotazione trovata con num: " + prenotazioneAggiornata.getNumero_Biglietto());
        return false;
    }

    public List<Bagaglio> internalGetAllBagagli() { return new ArrayList<>(this.bagagli); }
    public Optional<Bagaglio> internalFindBagaglioByCodice(int codiceBagaglio) {
        return this.bagagli.stream()
                .filter(b -> b.getCodice_Bagaglio() == codiceBagaglio)
                .findFirst();
    }
    public void internalAddBagaglio(Bagaglio bagaglio) {
        addBagaglioInternal(bagaglio);
    }
    public boolean internalUpdateBagaglio(Bagaglio bagaglioAggiornato) {
        Optional<Bagaglio> bagaglioEsistenteOpt = internalFindBagaglioByCodice(bagaglioAggiornato.getCodice_Bagaglio());
        if (bagaglioEsistenteOpt.isPresent()) {
            Bagaglio daRimuovere = bagaglioEsistenteOpt.get();
            Prenotazione prenotazioneAssociata = daRimuovere.getPrenotazione();
            if (prenotazioneAssociata != null && prenotazioneAssociata.getBagagli() != null) {
                prenotazioneAssociata.getBagagli().remove(daRimuovere);
            }
            this.bagagli.remove(daRimuovere);
            addBagaglioInternal(bagaglioAggiornato);
            return true;
        }
        return false;
    }

    public List<Bagaglio> internalGetBagagliSmarriti() { return new ArrayList<>(this.bagagliSmarriti); }
    public void internalSegnalaBagaglioSmarrito(Bagaglio bagaglio) {
        if (bagaglio != null && !this.bagagliSmarriti.contains(bagaglio)) {
            this.bagagliSmarriti.add(bagaglio);
        }
    }
    public boolean internalRisolviBagaglioSmarrito(Bagaglio bagaglio) {
        return this.bagagliSmarriti.remove(bagaglio);
    }

    public List<Gate> internalGetAllGates() { return new ArrayList<>(this.gates); }
    public Optional<Gate> internalFindGateByNumero(int numeroGate) {
        return this.gates.stream()
                .filter(g -> g.getNumero_Gate() == numeroGate)
                .findFirst();
    }
    public int getNextBagaglioId() {
        return bagaglioIdCounter.getAndIncrement();
    }
    // --- Fine Dati ---


    public AppController(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.utenteCorrente = null;

        this.utenti = new ArrayList<>();
        this.voli = new ArrayList<>();
        this.bagagli = new ArrayList<>();
        this.prenotazioni = new ArrayList<>();
        this.gates = new ArrayList<>();
        this.bagagliSmarriti = new ArrayList<>();
        loadInitialData();
    }

    // --- Metodi per l'Autenticazione ---
    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        Optional<Utente> utenteOpt = internalFindUtenteByLogin(username.trim());
        if (utenteOpt.isPresent()) {
            Utente utente = utenteOpt.get();
            if (utente.getPassword().equals(password)) {
                this.utenteCorrente = utente;
                if (utente instanceof Amministratore) {
                    mainFrame.showAdminHomePanel((Amministratore) utente, this);
                } else if (utente instanceof Utente_Generico) {
                    mainFrame.showUserHomePanel((Utente_Generico) utente, this);
                }
                return true;
            }
        }
        this.utenteCorrente = null;
        return false;
    }

    public boolean register(String nome, String cognome, String numeroDocumento, String login, String password) {
        if (nome == null || nome.trim().isEmpty() ||
                cognome == null || cognome.trim().isEmpty() ||
                numeroDocumento == null || numeroDocumento.trim().isEmpty() ||
                login == null || login.trim().isEmpty() ||
                password == null || password.isEmpty()) {
            System.err.println("AppController: Tentativo di registrazione con campi vuoti.");
            return false;
        }
        if (internalFindUtenteByLogin(login.trim()).isPresent()) {
            System.err.println("AppController: Username '" + login.trim() + "' già esistente.");
            return false;
        }
        Utente_Generico nuovoUtente = new Utente_Generico(nome.trim(), cognome.trim(), numeroDocumento.trim(), login.trim(), password);
        boolean aggiunto = internalAddUtente(nuovoUtente);
        if (aggiunto) {
            System.out.println("AppController: Utente '" + login.trim() + "' registrato con successo.");
        }
        return aggiunto;
    }

    public void logout() {
        this.utenteCorrente = null;
        mainFrame.showLoginPanel();
    }

    public Utente getUtenteCorrente() {
        return utenteCorrente;
    }

    public boolean isUserAdmin() {
        return this.utenteCorrente instanceof Amministratore;
    }

    // --- Metodi per Utente ---
    public List<Volo> handleVisualizzaVoli() {
        if (utenteCorrente != null) {
            return utenteCorrente.visualizzaVoli(this);
        }
        return List.of();
    }

    // --- Metodi per Admin ---
    public boolean handleAdminInserisciVolo(String codiceUnivoco, String compagniaAerea,
                                            String aeroportoPartenza, String aeroportoArrivo,
                                            int giorno, int mese, int anno,
                                            String orarioPrevisto, int minutiRitardo,
                                            Stato_Volo stato, String tipoVolo, Integer numeroGateSePartenza) {
        if (isUserAdmin()) {
            return ((Amministratore) utenteCorrente).inserisci_Volo(this, codiceUnivoco, compagniaAerea,
                    aeroportoPartenza, aeroportoArrivo, giorno, mese, anno, orarioPrevisto, minutiRitardo,
                    stato, tipoVolo, numeroGateSePartenza);
        }
        return false;
    }

    public boolean handleAdminAggiornaVolo(Volo voloConNuoviDati) {
        if (isUserAdmin()) {
            return ((Amministratore) utenteCorrente).aggiorna_Volo(this, voloConNuoviDati);
        }
        return false;
    }

    public Optional<Volo> handleAdminVisualizzaVoloSingolo(String codiceVolo) {
        return internalFindVoloByCodice(codiceVolo);
    }


    public boolean handleAdminEliminaVolo(String codiceVolo) {
        if (isUserAdmin()) {
            return ((Amministratore) utenteCorrente).elimina_Volo(this, codiceVolo);
        }
        return false;
    }

    public boolean handleAdminAssegnaGateAVolo(String codiceVolo, int numeroGate) {
        if (isUserAdmin()) {
            return ((Amministratore) utenteCorrente).assegna_Gate(this, String.valueOf(numeroGate), codiceVolo);
        }
        return false;
    }

    public List<Bagaglio> handleAdminVisualizzaBagagliTutti() {
        if (isUserAdmin()) {
            return ((Amministratore) utenteCorrente).visualizza_BagagliTutti(this);
        }
        return List.of();
    }

    public boolean handleAdminAggiornaStatoBagaglio(Bagaglio bagaglio) {
        if (isUserAdmin()) {
            return ((Amministratore) utenteCorrente).aggiorna_Bagaglio(this, bagaglio.getCodice_Bagaglio(), bagaglio.getStato_Bagaglio());
        }
        return false;
    }

        public List<Bagaglio> handleAdminVisualizzaSmarrimenti() {
        if (isUserAdmin()) {
            return ((Amministratore) utenteCorrente).visualizza_Smarrimenti(this);
        }
        return List.of();
    }

    public List<Gate> handleAdminGetAllGates() {
        return internalGetAllGates();
    }


    // --- Metodi per Utente Generico ---
    public List<Volo> handleUserGetVoliDisponibili() {
        if (utenteCorrente instanceof Utente_Generico) {
            return handleVisualizzaVoli().stream()
                    .filter(v -> v.getStato_Volo() == Stato_Volo.PROGRAMMATO || v.getStato_Volo() == Stato_Volo.IN_RITARDO)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public boolean handleUserPrenotaVolo(Volo voloSelezionato, String nomePasseggero, String cognomePasseggero, String numeroDocumento, String postoAssegnato) {
        if (utenteCorrente instanceof Utente_Generico) {
            return ((Utente_Generico) utenteCorrente).prenota_Volo(this, (Utente_Generico) utenteCorrente, voloSelezionato, nomePasseggero, cognomePasseggero, numeroDocumento, postoAssegnato);
        }
        return false;
    }

    public boolean handleUserCheckInPrenotazione(Prenotazione prenotazione, int numeroBagagliDaRegistrare) {
        if (utenteCorrente instanceof Utente_Generico) {
            return ((Utente_Generico) utenteCorrente).check_In(this, prenotazione, numeroBagagliDaRegistrare);
        }
        return false;
    }

    public List<Prenotazione> handleUserGetMiePrenotazioni() {
        if (utenteCorrente instanceof Utente_Generico) {
            return ((Utente_Generico) utenteCorrente).getMiePrenotazioni(this, (Utente_Generico)utenteCorrente);
        }
        return List.of();
    }

    public List<Prenotazione> handleUserCercaPrenotazioniPerNomePasseggero(String nomeOCognome) {
        if (utenteCorrente instanceof Utente_Generico) {
            return ((Utente_Generico) utenteCorrente).cerca_PrenotazionePerNome(this, (Utente_Generico)utenteCorrente, nomeOCognome);
        }
        return List.of();
    }

    public List<Prenotazione> handleUserCercaPrenotazioniPerCodiceVolo(String codiceVolo) {
        if (utenteCorrente instanceof Utente_Generico) {
            return ((Utente_Generico) utenteCorrente).cerca_PrenotazionePerVolo(this, (Utente_Generico)utenteCorrente, codiceVolo);
        }
        return List.of();
    }

    public boolean handleUserModificaPrenotazione(Prenotazione prenotazioneAggiornata) {
        if (utenteCorrente instanceof Utente_Generico) {
            return ((Utente_Generico) utenteCorrente).modifica_Prenotazione(this,(Utente_Generico)utenteCorrente, prenotazioneAggiornata);
        }
        return false;
    }

        public boolean handleUserCancellaPrenotazione(Prenotazione prenotazioneDaCancellare) {
        if (utenteCorrente instanceof Utente_Generico) {
            return ((Utente_Generico) utenteCorrente).cancella_Prenotazione(this, (Utente_Generico)utenteCorrente, prenotazioneDaCancellare);
        }
        return false;
    }

    public List<Bagaglio> handleUserGetBagagliPerPrenotazione(Prenotazione prenotazione) {
        if (utenteCorrente instanceof Utente_Generico) {
            return ((Utente_Generico) utenteCorrente).visualizza_BagagliPrenotazione(this, (Utente_Generico)utenteCorrente, prenotazione);
        }
        return List.of();
    }

    public Bagaglio handleUserAggiungiBagaglioAPrenotazione(Prenotazione prenotazione) {
        if (utenteCorrente instanceof Utente_Generico) {
            return ((Utente_Generico) utenteCorrente).aggiungi_Bagaglio(this, (Utente_Generico)utenteCorrente, prenotazione);
        }
        return null;
    }

    public boolean handleUserSegnalaSmarrimentoBagaglio(Bagaglio bagaglio) {
        if (utenteCorrente instanceof Utente_Generico) {
            return ((Utente_Generico) utenteCorrente).segnala_Smarrimento(this, (Utente_Generico)utenteCorrente, bagaglio.getCodice_Bagaglio());
        }
        return false;
    }
}