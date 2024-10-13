import {createAction, props} from "@ngrx/store";
import {TransaktionAusgabe, TransaktionEinnahme} from "../models/transaktion.model";

export const createTransaktionEinnahme = createAction(
  '[Transaktion] Create Transaktion für Einnahme',
  props<{transaktionEinnahme: TransaktionEinnahme}>());

export const createTransaktionAusgabe = createAction(
  '[Transaktion] Create Transaktion Ausgabe',
  props<{transaktionAusgabe: TransaktionAusgabe}>());
