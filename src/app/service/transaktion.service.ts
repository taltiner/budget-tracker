import {HttpClient} from "@angular/common/http";
import {Transaktion, TransaktionAusgabe, TransaktionEinnahme, TransaktionUebersicht} from "../models/transaktion.model";
import {catchError, map, Observable, switchMap, take, throwError} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class TransaktionService {
  private apiUrl = 'http://localhost:3000/transaktionen';


  constructor(private http: HttpClient) { }

  createEinnahmeTransaktion(transaktion: TransaktionEinnahme): void {
    this.getAllTransaktionen().pipe(
      take(1),
      map(alleTransaktionen => {
        if (transaktion.tranksaktionsArt === 'einnahme') {
          alleTransaktionen.einnahmen.push(transaktion as TransaktionEinnahme);
        }
        return alleTransaktionen;
      }),
      switchMap(aktualisierteTransaktionen =>
        this.http.put<TransaktionUebersicht>(this.apiUrl, aktualisierteTransaktionen)
      ),
      catchError(error => {
        console.error('Fehler beim Erzeugen der Transaktion', error);
        return throwError(() => error); // Fehler weiterleiten
      })
    ).subscribe(() => {
      console.log('Transaktion erfolgreich gespeichert:', transaktion);
    });
  }

  createAusgabenTransaktion(transaktion: TransaktionAusgabe[]): void {
    this.getAllTransaktionen().pipe(
      take(1),
      map(alleTransaktionen => {
        transaktion.forEach((abschnitt: TransaktionAusgabe ) => {
          if (abschnitt.tranksaktionsArt === 'ausgabe') {
            console.log('Wird gepusht:', transaktion);
            alleTransaktionen.ausgaben.push(abschnitt as TransaktionAusgabe);
          }
        })

        return alleTransaktionen;
      }),
      switchMap(aktualisierteTransaktionen =>
        this.http.put<TransaktionUebersicht>(this.apiUrl, aktualisierteTransaktionen)
      ),
      catchError(error => {
        console.error('Fehler beim Erzeugen der Transaktion', error);
        return throwError(() => error); // Fehler weiterleiten
      })
    ).subscribe(() => {
      console.log('Transaktion erfolgreich gespeichert:', transaktion);
    });
  }


  getTransaktion(id: string): Observable<Transaktion> {

    return this.http.get<Transaktion>(`${this.apiUrl}/${id}`).pipe(
      catchError(error => {
        console.error('Fehler beim Laden der Transaktion:', error);
        throw error;
      })
    );
  }

  getAllTransaktionen(): Observable<TransaktionUebersicht> {

    return this.http.get<TransaktionUebersicht>(`${this.apiUrl}`).pipe(
      catchError(error => {
        console.error('Fehler beim Laden aller Transaktionen', error);
        throw error;
      })
    );
  }

  deleteTransaktion() {

  }

}
