import {HttpClient} from "@angular/common/http";
import {Transaktion, TransaktionAusgabe, TransaktionEinnahme, TransaktionUebersicht} from "../models/transaktion.model";
import {catchError, Observable} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class TransaktionService {
  private apiUrl = 'http://localhost:3000/transaktionen';


  constructor(private http: HttpClient) { }

  createTransaktion(transaktion: TransaktionEinnahme): void {
    this.getAllTransaktionen().subscribe(alleTransaktionen => {
      if(transaktion.tranksaktionsArt === 'einnahme') {
        console.log('einnahme')
        alleTransaktionen.einnahmen.push(transaktion);
      } else if (transaktion.tranksaktionsArt === 'ausgabe') {
        alleTransaktionen.ausgaben.push(transaktion);
      }

      this.http.put<TransaktionUebersicht>(this.apiUrl, alleTransaktionen).pipe(
        catchError(error => {
          console.error('Fehler beim Erzeugen der Transaktion', error);
          throw error;
        })
      ).subscribe();
    })
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
