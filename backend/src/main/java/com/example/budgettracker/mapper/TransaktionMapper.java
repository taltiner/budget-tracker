package com.example.budgettracker.mapper;

import com.example.budgettracker.dto.*;
import com.example.budgettracker.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransaktionMapper {

    TransaktionMapper INSTANCE = Mappers.getMapper( TransaktionMapper.class );

    TransaktionEinnahme toTransaktionEinnahmeEntity(TransaktionEinnahmeDTO einnahmeDto);

    TransaktionEinnahmeDTO toTransaktionEinnahmeResponseDTO(TransaktionEinnahme transaktionEinnahme);

    @Mapping(source = "transaktionsArt", target = "transaktionsArt")
    @Mapping(source = "jahrTransaktion", target = "jahrTransaktion")
    @Mapping(source = "monatTransaktion", target = "monatTransaktion")
    @Mapping(source = "kategorie", target = "kategorie")
    @Mapping(source = "benutzerdefinierteKategorie", target = "benutzerdefinierteKategorie")
    @Mapping(source = "betragAusgabe", target = "betragAusgabe")
    @Mapping(source = "istSchulden", target = "istSchulden")
    TransaktionAusgabe toTransaktionAusgabeEntity(TransaktionAusgabeDTO ausgabeDTO);

    @Mapping(source = "transaktionsArt", target = "transaktionsArt")
    @Mapping(source = "jahrTransaktion", target = "jahrTransaktion")
    @Mapping(source = "monatTransaktion", target = "monatTransaktion")
    @Mapping(source = "kategorie", target = "kategorie")
    @Mapping(source = "benutzerdefinierteKategorie", target = "benutzerdefinierteKategorie")
    @Mapping(source = "betragAusgabe", target = "betragAusgabe")
    @Mapping(source = "istSchulden", target = "istSchulden")
    TransaktionAusgabeDTO toTransaktionAusgabeResponseDTO(TransaktionAusgabe transaktionAusgabe);

    TransaktionNotiz toTransaktionNotizEntity(TransaktionNotizDTO notizDTO);

    TransaktionNotizDTO toTransaktionNotizResponseDTO(TransaktionNotiz notiz);

    TransaktionUebersichtDTO toTransaktionUebersichtResponseDTO(TransaktionUebersicht transaktionUebersicht);

    SchuldenDTO toSchuldenDTO(Schulden schulden);

    Schulden toSchuldenEntity(SchuldenDTO schuldenDTO);
}
