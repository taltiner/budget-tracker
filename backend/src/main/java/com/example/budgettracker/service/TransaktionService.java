package com.example.budgettracker.service;

import com.example.budgettracker.dto.SchuldenDTO;
import com.example.budgettracker.dto.TransaktionAusgabeDTO;
import com.example.budgettracker.dto.TransaktionEinnahmeDTO;
import com.example.budgettracker.dto.TransaktionNotizDTO;
import com.example.budgettracker.mapper.TransaktionMapper;
import com.example.budgettracker.model.*;
import com.example.budgettracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransaktionService {

    private final TransaktionEinnahmeRepository transaktionEinnahmeRepository;
    private final TransaktionAusgabeRepository transaktionAusgabeRepository;
    private final TransaktionNotizRepository transaktionNotizRepository;
    private final SchuldenRepository schuldenRepository;
    private final TransaktionMapper transaktionMapper;

    @Autowired
    public TransaktionService(
            TransaktionEinnahmeRepository transaktionEinnahmeRepository,
            TransaktionAusgabeRepository transaktionAusgabeRepository,
            TransaktionNotizRepository transaktionNotizRepository,
            SchuldenRepository schuldenRepository,
            TransaktionMapper transaktionMapper) {
        this.transaktionEinnahmeRepository = transaktionEinnahmeRepository;
        this.transaktionAusgabeRepository = transaktionAusgabeRepository;
        this.transaktionNotizRepository = transaktionNotizRepository;
        this.schuldenRepository = schuldenRepository;
        this.transaktionMapper = transaktionMapper;
    }

    public TransaktionEinnahme createEinnahmeTransaktion(TransaktionEinnahmeDTO einnahmeRequestDTO) {
        return transaktionEinnahmeRepository.save(transaktionMapper.toTransaktionEinnahmeEntity(einnahmeRequestDTO));
    }

    public TransaktionEinnahme updateEinnahmeTransaktion(TransaktionEinnahmeDTO einnahmeRequestDTO) {
        return transaktionEinnahmeRepository.update(transaktionMapper.toTransaktionEinnahmeEntity(einnahmeRequestDTO));
    }

    public List<TransaktionEinnahme> getAllTransaktionEinnahmen() {
        return transaktionEinnahmeRepository.findAll();
    }

    public void deleteAllTransaktionEinnahmen() {
        transaktionEinnahmeRepository.deleteAll();
    }

    public TransaktionAusgabe createAusgabeTransaktion(TransaktionAusgabeDTO ausgabeRequestDTO) {
        return transaktionAusgabeRepository.save(transaktionMapper.toTransaktionAusgabeEntity(ausgabeRequestDTO));
    }

    public List<TransaktionAusgabe> updateAusgabeTransaktion(List<TransaktionAusgabeDTO> ausgabeRequestDTO) {
        List<TransaktionAusgabe> ausgaben = ausgabeRequestDTO.stream()
                .map(ausgabe -> transaktionMapper.toTransaktionAusgabeEntity(ausgabe))
                .collect(Collectors.toList());

        return transaktionAusgabeRepository.update(ausgaben);
    }

    public List<TransaktionAusgabe> getAllTransaktionAusgaben() {
        return transaktionAusgabeRepository.findAll();
    }

    public void deleteAllTransaktionAusgaben() {
        transaktionAusgabeRepository.deleteAll();
    }

    public TransaktionNotiz createNotizTransaktion(TransaktionNotizDTO notizDTO) {
        return transaktionNotizRepository.save(transaktionMapper.toTransaktionNotizEntity(notizDTO));
    }

    public TransaktionNotiz updateNotizTransaktion(TransaktionNotizDTO notizDTO) {
        return transaktionNotizRepository.update(transaktionMapper.toTransaktionNotizEntity(notizDTO));
    }

    public List<TransaktionNotiz> getAllTransaktionNotizen() {
        return transaktionNotizRepository.findAll();
    }

    public void deleteAllTransaktionNotizen() {
        transaktionNotizRepository.deleteAll();
    }

    public TransaktionUebersicht getTransaktion(String monat, String jahr) {
        List<TransaktionEinnahme> filteredEinnahmen = filterTransaktionNachJahrUndMonat(getAllTransaktionEinnahmen(), jahr, monat);
        List<TransaktionAusgabe> filteredAusgaben = filterTransaktionNachJahrUndMonat(getAllTransaktionAusgaben(), jahr, monat) ;
        List<TransaktionNotiz> filteredNotizen = filterTransaktionNachJahrUndMonat(getAllTransaktionNotizen(), jahr, monat);

        return new TransaktionUebersicht(filteredEinnahmen, filteredAusgaben, filteredNotizen);
    }

    public TransaktionUebersicht getAllTransaktionen() {
        List<TransaktionEinnahme> einnahmen = getAllTransaktionEinnahmen();
        List<TransaktionAusgabe> ausgaben = getAllTransaktionAusgaben();
        List<TransaktionNotiz> notizen = getAllTransaktionNotizen();

        return new TransaktionUebersicht(einnahmen, ausgaben, notizen);
    }

    public void deleteTransaktion(String monat, String jahr) {
        transaktionEinnahmeRepository.delete(monat, jahr);
        transaktionAusgabeRepository.delete(monat, jahr);
        transaktionNotizRepository.delete(monat, jahr);
    }

    public List<SchuldenDTO> createSchuldenEintraege(List<SchuldenDTO> schuldenDTOS) {
        List<Schulden> savedSchulden = schuldenDTOS.stream()
                .map(schuld -> schuldenRepository.save(transaktionMapper.toSchuldenEntity(schuld)))
                .collect(Collectors.toList());

        return savedSchulden.stream()
                .map(savedSchuld -> transaktionMapper.toSchuldenDTO(savedSchuld))
                .collect(Collectors.toList());
    }

    public List<SchuldenDTO> updateSchuldenEintraege(List<SchuldenDTO> schuldenDTOS) {
        List<Schulden> updatedSchulden = schuldenRepository.update(schuldenDTOS.stream()
                .map(schuldDTO -> transaktionMapper.toSchuldenEntity(schuldDTO))
                .collect(Collectors.toList())
        );

        return updatedSchulden.stream()
                .map(schuld -> transaktionMapper.toSchuldenDTO(schuld))
                .collect(Collectors.toList());
    }

    public List<SchuldenDTO> getSchuldenEintraege() {
        List<Schulden> schulden = schuldenRepository.findAll();

        return schulden.stream().map(schuld -> transaktionMapper.toSchuldenDTO(schuld)).collect(Collectors.toList());
    }

    public List<TransaktionUebersichtTransformiert> getFilteredTransaktionen(String selectedJahr) {
        List<TransaktionEinnahme> filteredEinnahmen = filterTransaktionNachJahr(getAllTransaktionEinnahmen(), selectedJahr) ;
        List<TransaktionAusgabe> filteredAusgaben = filterTransaktionNachJahr(getAllTransaktionAusgaben(), selectedJahr) ;
        List<TransaktionNotiz> filteredNotizen = filterTransaktionNachJahr(getAllTransaktionNotizen(), selectedJahr);

        List<Transaktion> filteredTransaktionen = new ArrayList<>();
        filteredTransaktionen.addAll(filteredEinnahmen);
        filteredTransaktionen.addAll(filteredAusgaben);
        filteredTransaktionen.addAll(filteredNotizen);

        List<TransaktionUebersichtTransformiert> transaktionGruppiert = gruppiereUndTransformiereNachMonat(filteredTransaktionen);
        List<TransaktionUebersichtTransformiert> transaktionSortiert = sortiereDaten(transaktionGruppiert);

        return transaktionSortiert;
    }

    private <T extends Transaktion> List<T> filterTransaktionNachJahr(List<T> transaktionen, String selectedJahr) {
        return transaktionen.stream()
                .filter(transaktion -> transaktion.getJahrTransaktion().equals(selectedJahr))
                .collect(Collectors.toList());
    }

    private <T extends Transaktion> List<T> filterTransaktionNachJahrUndMonat(List<T> transaktionen, String selectedJahr, String selectedMonat) {
        return transaktionen.stream()
                .filter(transaktion -> transaktion.getJahrTransaktion().equals(selectedJahr))
                .filter(transaktion -> transaktion.getMonatTransaktion().equals(selectedMonat))
                .collect(Collectors.toList());
    }

    private List<TransaktionUebersichtTransformiert> gruppiereUndTransformiereNachMonat(List<Transaktion> transaktionen) {
        Map<String, TransaktionUebersichtTransformiert> gruppiert = new HashMap<>();
        String gesamtKey = "gesamt";
        initTransformierterEintragFuerMonat(gruppiert, gesamtKey);

        transaktionen.stream()
                .forEach(transaktion -> {
                    String monat = transaktion.getMonatTransaktion();
                    String monatLabel = SelectOption.MonatAuswahl.getMonatLabel(transaktion.getMonatTransaktion());
                    transaktion.setMonatTransaktion(monatLabel);
                    Boolean istEinnahme = istEinnahme(transaktion);
                    Boolean istAussgabe = istAussagabe(transaktion);

                    if(gruppiert.get(monat) == null) {
                        initTransformierterEintragFuerMonat(gruppiert, monat);
                    }

                    TransaktionUebersichtTransformiert eintragAktuellerMonat = gruppiert.get(monat);
                    TransaktionUebersichtTransformiert eintragGesamt = gruppiert.get(gesamtKey);

                    if(istEinnahme && transaktion instanceof TransaktionEinnahme) {
                        berechneEinnahmeMonat((TransaktionEinnahme) transaktion, eintragAktuellerMonat, eintragGesamt);
                    } else if(istAussgabe && transaktion instanceof TransaktionAusgabe) {
                        berechneAusgabeMonat((TransaktionAusgabe) transaktion, eintragAktuellerMonat, eintragGesamt);
                    } else if(istNotiz(transaktion) && transaktion instanceof TransaktionNotiz) {
                        eintragAktuellerMonat.setNotiz(((TransaktionNotiz) transaktion).getNotiz());
                    }
                });

        double saldo = gruppiert.get(gesamtKey).getEinnahmen().getHoehe() - gruppiert.get(gesamtKey).getGesamtausgaben().getHoehe();
        saldo = Math.round(saldo * 100) / 100;
        gruppiert.get(gesamtKey).getSaldo().setHoehe(saldo);

        return gruppiert.values().stream().toList();
    }

    private List<TransaktionUebersichtTransformiert> sortiereDaten(List<TransaktionUebersichtTransformiert> transaktionen) {
        return transaktionen.stream().sorted((a, b) ->  {
            int indexA = monate.indexOf(SelectOption.MonatAuswahl.getMonatValue(a.getMonatTransaktion()));
            int indexB = monate.indexOf(SelectOption.MonatAuswahl.getMonatValue(b.getMonatTransaktion()));

            return indexB - indexA;
        }).toList();
    }

    private Boolean istEinnahme(Transaktion transaktion) {
        return transaktion.getTransaktionsArt().equals(EingabeArt.EINNAHME);
    }

    private Boolean istAussagabe(Transaktion transaktion) {
        return transaktion.getTransaktionsArt().equals(EingabeArt.AUSGABE);
    }

    private Boolean istNotiz(Transaktion transaktion) {
        return transaktion.getTransaktionsArt().equals(EingabeArt.NOTIZ);
    }

    private void initTransformierterEintragFuerMonat(Map<String, TransaktionUebersichtTransformiert> gruppiert, String monat) {
        TransaktionUebersichtTransformiert initial = new TransaktionUebersichtTransformiert.Builder()
                .setMonatTransaktion(SelectOption.MonatAuswahl.getMonatLabel(monat))
                .setEinnahmen(new GeldbetragNumerisch(0.00, "€"))
                .setAusgaben(new HashMap<>())
                .setGesamtausgaben(new GeldbetragNumerisch(0.00, "€"))
                .setSaldo(new GeldbetragNumerisch(0.00, "€"))
                .setNotiz("")
                .build();
        gruppiert.put(monat, initial);
    }

    private void initAusgabeKategorieEintrag(TransaktionUebersichtTransformiert eintrag, String kategorie) {
        if(!eintrag.getAusgaben().containsKey(kategorie)) {
            eintrag.getAusgaben().put(kategorie, new GeldbetragNumerisch(0.00, "€"));
            System.out.println(eintrag);
        }
    }

    private void berechneEinnahmeMonat(TransaktionEinnahme transaktionEinnahme,
                                       TransaktionUebersichtTransformiert eintragAktuellerMonat,
                                       TransaktionUebersichtTransformiert eintragGesamt) {

        double betragHoehe = 0.00;
        betragHoehe = Double.parseDouble(((TransaktionEinnahme) transaktionEinnahme).getBetragEinnahme().getHoehe());
        eintragAktuellerMonat.getEinnahmen().plus(betragHoehe);
        eintragAktuellerMonat.getSaldo().plus(betragHoehe);
        eintragGesamt.getEinnahmen().plus(betragHoehe);
        eintragGesamt.getSaldo().plus(betragHoehe);
    }

    private void berechneAusgabeMonat(TransaktionAusgabe transaktionAusgabe,
                                      TransaktionUebersichtTransformiert eintragAktuellerMonat,
                                      TransaktionUebersichtTransformiert eintragGesamt) {

        double betragHoehe = 0.00;
        betragHoehe = Double.parseDouble(transaktionAusgabe.getBetragAusgabe().getHoehe());
        String kategorie = ermittleAusgabeKategorie(transaktionAusgabe);

        initAusgabeKategorieEintrag(eintragAktuellerMonat, kategorie);
        initAusgabeKategorieEintrag(eintragGesamt, kategorie);

        eintragAktuellerMonat.getAusgaben().get(kategorie).plus(betragHoehe);
        eintragAktuellerMonat.getGesamtausgaben().plus(betragHoehe);
        eintragAktuellerMonat.getSaldo().minus(betragHoehe);

        eintragGesamt.getGesamtausgaben().plus(betragHoehe);
        eintragGesamt.getAusgaben().get(kategorie).plus(betragHoehe);
    }

    private String ermittleAusgabeKategorie(TransaktionAusgabe transaktionAusgabe) {
        String benutzerdefinierteKategorie = transaktionAusgabe.getBenutzerdefinierteKategorie();
        String kategorieLabel = SelectOption.KategorieAuswahl.getKategorieLabel(transaktionAusgabe.getKategorie());

        return !benutzerdefinierteKategorie.isBlank() ?
                Character.toUpperCase(benutzerdefinierteKategorie.charAt(0)) + benutzerdefinierteKategorie.substring(1).toLowerCase() :
                kategorieLabel;
    }

    private final List<String> monate = new ArrayList<>(Arrays.asList("gesamt", "januar", "februar", "märz", "april", "mai", "juni",
            "juli", "august", "september", "oktober", "november", "dezember"));

}
