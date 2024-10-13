import {TransaktionAusgabe, TransaktionEinnahme} from "../models/transaktion.model";


export interface TransaktionState {
  transaktionEinnahme: TransaktionEinnahme[];
  transaktionAusgabe: TransaktionAusgabe[];
}

export const initialTransaktionState: TransaktionState = {
  transaktionEinnahme: [],
  transaktionAusgabe: []
}
