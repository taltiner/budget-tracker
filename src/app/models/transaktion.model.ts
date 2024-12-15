export interface Transaktion {
  tranksaktionsArt: string
}

export interface TransaktionEinnahme extends Transaktion {
  jahrTransaktion: string,
  monatTransaktion: string,
  betragEinnahme: string,
  notiz?: string
}

export interface TransaktionAusgabe extends Transaktion {
  datumTransaktion: string,
  kategorie: string,
  betragAusgabe: string
}

export interface TransaktionUebersicht {
  einnahmen: TransaktionEinnahme[],
  ausgaben: TransaktionEinnahme[]
}

export const initialTransaktionUebersicht = {
  einnahmen: [],
  ausgaben: []
}
