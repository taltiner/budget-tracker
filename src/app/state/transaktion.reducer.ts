import {createReducer, on} from "@ngrx/store";
import {createTransaktionAusgabe, createTransaktionEinnahme} from "./transaktion.actions";
import {initialTransaktionState} from "./transaktion.state";

export const transaktionReducer = createReducer(
  initialTransaktionState,

  on(createTransaktionEinnahme, (state, {transaktionEinnahme}) => {
    return {
      ...state,
      transaktionEinnahme: [...state.transaktionEinnahme, transaktionEinnahme],
    }
  }),

  on(createTransaktionAusgabe, (state, {transaktionAusgabe}) => {
    return {
      ...state,
      transaktionAusgabe: [...state.transaktionAusgabe, transaktionAusgabe]
    }
  }),

)
