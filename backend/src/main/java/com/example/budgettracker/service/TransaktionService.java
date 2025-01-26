package com.example.budgettracker.service;

import com.example.budgettracker.model.*;
import com.example.budgettracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransaktionService {

    private final TransaktionEinnahmeRepository transaktionEinnahmeRepository;
    private final TransaktionAusgabeRepository transaktionAusgabeRepository;
    private final TransaktionNotizRepository transaktionNotizRepository;

    @Autowired
    public TransaktionService(
            TransaktionEinnahmeRepository transaktionEinnahmeRepository,
            TransaktionAusgabeRepository transaktionAusgabeRepository,
            TransaktionNotizRepository transaktionNotizRepository) {
        this.transaktionEinnahmeRepository = transaktionEinnahmeRepository;
        this.transaktionAusgabeRepository = transaktionAusgabeRepository;
        this.transaktionNotizRepository = transaktionNotizRepository;
    }

    public TransaktionEinnahme createEinnahmeTransaktion(TransaktionEinnahme einnahme) {
         return transaktionEinnahmeRepository.save(einnahme);
    }

    public List<TransaktionEinnahme> getAllTransaktionEinnahmen() {
        return transaktionEinnahmeRepository.findAll();
    }

    public void deleteAllTransaktionEinnahmen() {
        transaktionEinnahmeRepository.deleteAll();
    }

    public TransaktionAusgabe createAusgabeTransaktion(TransaktionAusgabe ausgabe) {
        return transaktionAusgabeRepository.save(ausgabe);
    }

    public List<TransaktionAusgabe> getAllTransaktionAusgaben() {
        return transaktionAusgabeRepository.findAll();
    }

    public void deleteAllTransaktionAusgaben() {
        transaktionAusgabeRepository.deleteAll();
    }

    public TransaktionNotiz createNotizTransaktion(TransaktionNotiz notiz) {
        return transaktionNotizRepository.save(notiz);
    }

    public List<TransaktionNotiz> getAllTransaktionNotizen() {
        return transaktionNotizRepository.findAll();
    }

    public void deleteAllTransaktionNotizen() {
        transaktionNotizRepository.deleteAll();
    }

    public TransaktionUebersicht getAllTransaktionen() {
        List<TransaktionEinnahme> einnahmen = getAllTransaktionEinnahmen();
        List<TransaktionAusgabe> ausgaben = getAllTransaktionAusgaben();
        List<TransaktionNotiz> notizen = getAllTransaktionNotizen();

        return new TransaktionUebersicht(einnahmen, ausgaben, notizen);
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

        return transaktionGruppiert;
    }

    private <T extends Transaktion> List<T> filterTransaktionNachJahr(List<T> transaktionen, String selectedJahr) {
        return transaktionen.stream()
                .filter(transaktion -> transaktion.getJahrTransaktion().equals(selectedJahr))
                .collect(Collectors.toList());
    }

    private List<TransaktionUebersichtTransformiert> gruppiereUndTransformiereNachMonat(List<Transaktion> transaktionen) {
        Map<String, TransaktionUebersichtTransformiert> gruppiert = new HashMap<>();
        String gesamtKey = "gesamt";
        initTransformierterEintragFuerMonat(gruppiert, gesamtKey);

        transaktionen.stream()
                .forEach(transaktion -> {
                    String monat = transaktion.getMonatTransaktion();
                    Boolean istEinnahme = istEinnahme(transaktion);
                    Boolean istAussgabe = istAussagabe(transaktion);
                    double betragHoehe = 0.00;

                    if(gruppiert.get(monat) == null) {
                        initTransformierterEintragFuerMonat(gruppiert, monat);
                    }

                    TransaktionUebersichtTransformiert eintragAktuellerMonat = gruppiert.get(monat);
                    TransaktionUebersichtTransformiert eintragGesamt = gruppiert.get(gesamtKey);

                    if(istEinnahme && transaktion instanceof TransaktionEinnahme) {
                        berechneEinnahmeMonat((TransaktionEinnahme) transaktion, betragHoehe, eintragAktuellerMonat, eintragGesamt);
                    } else if(istAussgabe && transaktion instanceof TransaktionAusgabe) {
                        berechneAusgabeMonat((TransaktionAusgabe) transaktion, betragHoehe, eintragAktuellerMonat, eintragGesamt);
                    } else if(istNotiz(transaktion) && transaktion instanceof TransaktionNotiz) {
                        eintragAktuellerMonat.setNotiz(((TransaktionNotiz) transaktion).getNotiz());
                    }
                });

        //double saldo = gruppiert.get(gesamtKey).getEinnahmen().getHoehe() - gruppiert.get(gesamtKey).getGesamtausgaben().getHoehe();
        //gruppiert.get(gesamtKey).getSaldo().setHoehe(saldo);

        return gruppiert.values().stream().toList();
    }

    private Boolean istEinnahme(Transaktion transaktion) {
        return transaktion.getTransaktionsArt().equals(EingabeArt.EINNAHME);
    }

    private Boolean istAussagabe(Transaktion transaktion) {
        return transaktion.getMonatTransaktion().equals(EingabeArt.AUSGABE);
    }

    private Boolean istNotiz(Transaktion transaktion) {
        return transaktion.getTransaktionsArt().equals(EingabeArt.NOTIZ);
    }

    private void initTransformierterEintragFuerMonat(Map<String, TransaktionUebersichtTransformiert> gruppiert, String monat) {
        TransaktionUebersichtTransformiert initial = new TransaktionUebersichtTransformiert.Builder()
                .setMonatTransaktion(monat)
                .setEinnahmen(new GeldbetragNumerisch(0, "€"))
                .setAusgaben(new HashMap<>())
                .setGesamtausgaben(new GeldbetragNumerisch(0, "€"))
                .setSaldo(new GeldbetragNumerisch(0, "€"))
                .setNotiz("")
                .build();
        gruppiert.put(monat, initial);
    }

    private void initAusgabeKategorieEintrag(TransaktionUebersichtTransformiert eintrag, String kategorie) {
        if(!eintrag.getAusgaben().containsKey(kategorie)) {
            eintrag.getAusgaben().put(kategorie, new GeldbetragNumerisch(0, "€"));
        }
    }

    private void berechneEinnahmeMonat(TransaktionEinnahme transaktionEinnahme,
                                       double betragHoehe,
                                       TransaktionUebersichtTransformiert eintragAktuellerMonat,
                                       TransaktionUebersichtTransformiert eintragGesamt) {

        betragHoehe = Double.parseDouble(((TransaktionEinnahme) transaktionEinnahme).getBetragEinnahme().getHoehe());
        eintragAktuellerMonat.getEinnahmen().plus(betragHoehe);
        eintragAktuellerMonat.getSaldo().plus(betragHoehe);
        eintragGesamt.getEinnahmen().plus(betragHoehe);
        eintragGesamt.getSaldo().plus(betragHoehe);
    }

    private void berechneAusgabeMonat(TransaktionAusgabe transaktionAusgabe,
                                      double betragHoehe,
                                      TransaktionUebersichtTransformiert eintragAktuellerMonat,
                                      TransaktionUebersichtTransformiert eintragGesamt) {
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

        return benutzerdefinierteKategorie.isEmpty() ?
                Character.toUpperCase(benutzerdefinierteKategorie.charAt(0)) + benutzerdefinierteKategorie.substring(1).toLowerCase() :
                kategorieLabel;
    }
}
