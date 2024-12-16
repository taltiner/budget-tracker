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
  betragAusgabe: Geldbetrag
}

export interface TransaktionUebersicht {
  einnahmen: TransaktionEinnahme[],
  ausgaben: TransaktionAusgabe[],
}

export const initialTransaktionUebersicht = {
  einnahmen: [],
  ausgaben: []
}
