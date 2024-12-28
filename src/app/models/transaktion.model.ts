import {Geldbetrag, GeldbetragNumerisch} from "./geldbetrag.model";


export enum EingabeArt {
  Einnahme = 'einnahme',
  Ausgabe = 'ausgabe',
  Notiz = 'notiz'
}

export interface Transaktion {
  tranksaktionsArt: EingabeArt | undefined | null,
}

export interface TransaktionEinnahme extends Transaktion {
  jahrTransaktion: string,
  monatTransaktion: string,
  betragEinnahme: Geldbetrag,
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

export interface TransaktionNotiz extends Transaktion {
  jahrTransaktion: string,
  monatTransaktion: string,
  notiz: string
}

export interface TransaktionUebersicht {
  einnahmen: TransaktionEinnahme[],
  ausgaben: TransaktionAusgabe[],
  notizen: TransaktionNotiz[],
}

export interface TransaktionUebersichtTransformiert {
  einnahmen: GeldbetragNumerisch,
  ausgaben: { [kategorie: string]: GeldbetragNumerisch };
  monatTransaktion: string,
  gesamtausgaben: GeldbetragNumerisch,
  saldo: GeldbetragNumerisch,
  notiz?: string
}

export const initialTransaktionUebersicht = {
  einnahmen: [],
  ausgaben: [],
  notizen: []
}
