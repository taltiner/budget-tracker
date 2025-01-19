import {Geldbetrag, GeldbetragNumerisch} from "./geldbetrag.model";


export enum EingabeArt {
  Einnahme = 'einnahme',
  Ausgabe = 'ausgabe',
  Notiz = 'notiz'
}

export interface Transaktion {
  id?: string;
  transaktionsArt: EingabeArt | undefined | null,
  jahrTransaktion: string,
  monatTransaktion: string,
}

export interface TransaktionEinnahme extends Transaktion {
  betragEinnahme: Geldbetrag,
}

export interface TransaktionAusgabe extends Transaktion {
  kategorie: string,
  benutzerdefinierteKategorie: string,
  betragAusgabe: Geldbetrag,
}

export interface TransaktionNotiz extends Transaktion {
  notiz: string
}

export interface TransaktionUebersicht {
  einnahmen: TransaktionEinnahme[],
  ausgaben: TransaktionAusgabe[],
  notizen: TransaktionNotiz[]
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
