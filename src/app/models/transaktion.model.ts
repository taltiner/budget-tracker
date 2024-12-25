import {Geldbetrag, GeldbetragNumerisch} from "./geldbetrag.model";

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
  einnahmen: GeldbetragNumerisch,
  ausgaben: { [kategorie: string]: GeldbetragNumerisch };
  monatTransaktion: string,
  gesamtausgaben: GeldbetragNumerisch,
  saldo: GeldbetragNumerisch,
}

export const initialTransaktionUebersicht = {
  einnahmen: [],
  ausgaben: []
}
