package com.example.budgettracker.mapper;

import com.example.budgettracker.dto.request.TransaktionAusgabeRequestDTO;
import com.example.budgettracker.dto.request.TransaktionEinnahmeRequestDTO;
import com.example.budgettracker.dto.request.TransaktionNotizRequestDTO;
import com.example.budgettracker.dto.response.TransaktionAusgabeResponseDTO;
import com.example.budgettracker.dto.response.TransaktionEinnahmeResponseDTO;
import com.example.budgettracker.dto.response.TransaktionNotizResponseDTO;
import com.example.budgettracker.dto.response.TransaktionUebersichtResponseDTO;
import com.example.budgettracker.model.TransaktionAusgabe;
import com.example.budgettracker.model.TransaktionEinnahme;
import com.example.budgettracker.model.TransaktionNotiz;
import com.example.budgettracker.model.TransaktionUebersicht;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransaktionMapper {

    TransaktionMapper INSTANCE = Mappers.getMapper( TransaktionMapper.class );

    @Mapping(target = "transaktionsArt", source = "transaktionsArt")
    TransaktionEinnahme toTransaktionEinnahmeEntity(TransaktionEinnahmeRequestDTO einnahmeDto);
    @Mapping(target = "transaktionsArt", source = "transaktionsArt")
    TransaktionEinnahmeResponseDTO toTransaktionEinnahmeResponseDTO(TransaktionEinnahme transaktionEinnahme);

    @Mapping(target = "transaktionsArt", source = "transaktionsArt")
    @Mapping(target = "betragAusgabe", source = "betragAusgabe")
    TransaktionAusgabe toTransaktionAusgabeEntity(TransaktionAusgabeRequestDTO ausgabeDTO);
    @Mapping(target = "transaktionsArt", source = "transaktionsArt")
    @Mapping(target = "betragAusgabe", source = "betragAusgabe")
    TransaktionAusgabeResponseDTO toTransaktionAusgabeResponseDTO(TransaktionAusgabe transaktionAusgabe);

    @Mapping(target = "transaktionsArt", source = "transaktionsArt")
    TransaktionNotiz toTransaktionNotizEntity(TransaktionNotizRequestDTO notizDTO);
    @Mapping(target = "transaktionsArt", source = "transaktionsArt")
    TransaktionNotizResponseDTO toTransaktionNotizResponseDTO(TransaktionNotiz notiz);

    TransaktionUebersichtResponseDTO toTransaktionUebersichtResponseDTO(TransaktionUebersicht transaktionUebersicht);
}
