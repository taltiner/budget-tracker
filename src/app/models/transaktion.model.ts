import {Geldbetrag} from "./geldbetrag.model";

export interface Transaktion {
  tranksaktionsArt: string
}

export interface TransaktionEinnahme extends Transaktion {
  jahrTransaktion: string,
  monatTransaktion: string,
  betragEinnahme: Geldbetrag,
  notiz?: string
}

export interface TransaktionAusgabe extends Transaktion {
  datumTransaktion: string,
  kategorie: string,
  benutzerdefinierteKategorie: string,
  betragAusgabe: Geldbetrag,

  tagTransaktion?: string,
  monatTransaktion?: string,
  jahrTransaktion?: string,
}

export interface TransaktionUebersicht {
  einnahmen: TransaktionEinnahme[],
  ausgaben: TransaktionAusgabe[],
}

export interface TransaktionUebersichtTransformiert {
  einnahmen: {hoehe: number, waehrung: '€'},
  ausgaben: { [kategorie: string]: {hoehe: number, waehrung: '€'} };
  monatTransaktion: string
}

export const initialTransaktionUebersicht = {
  einnahmen: [],
  ausgaben: []
}

export const initialTransaktionAusgabe: TransaktionAusgabe = {
  tranksaktionsArt: 'aussgabe',
  datumTransaktion: '',
  kategorie: '',
  benutzerdefinierteKategorie: '',
  betragAusgabe: {hoehe: '0', waehrung: '€'},

  tagTransaktion: '',
  monatTransaktion: '',
  jahrTransaktion: '',
}
