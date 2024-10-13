export interface Transaktion {
  datumTransaktion: string,
  tranksaktionsArt: string
}

export interface TransaktionEinnahme extends Transaktion {
  betragEinnahme: string,
  notiz?: string
}

export interface TransaktionAusgabe extends Transaktion {
  kategorie: string,
  betragAusgabe: string
}
