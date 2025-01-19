import { HttpClient } from "@angular/common/http";
import {
  Transaktion,
  TransaktionAusgabe,
  TransaktionEinnahme, TransaktionNotiz,
  TransaktionUebersicht,
  TransaktionUebersichtTransformiert
} from "../models/transaktion.model";
import {BehaviorSubject, catchError, filter, map, Observable, of, switchMap, take, throwError} from "rxjs";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root'
})
export class TransaktionService {
  private backendUrl = 'http://localhost:8080/transaktionen';
  private frontendUrl = 'http://localhost:3000/transaktionen';
  private apiUrlSubject = new BehaviorSubject<string | null>(null);
  jahrAuswahl$ = new BehaviorSubject<string | null>(null);
  dataSourceSubject = new BehaviorSubject<TransaktionUebersichtTransformiert[] | null>(null);
  kategorieSubject = new BehaviorSubject<string[] >([]);

  kategorie$ = this.kategorieSubject.asObservable();
  dataSource$ = this.dataSourceSubject.asObservable();
  apiUrl$ = this.apiUrlSubject.asObservable();

  constructor(private http: HttpClient) {
    this.checkBackendStatus();
  }

  private checkBackendStatus(): void {
    this.http.get(this.backendUrl + '/health', { responseType: 'text' }).pipe(
      map(response => response === 'Server is running' ? this.backendUrl : this.frontendUrl),
      catchError(() => of(this.frontendUrl))
    ).subscribe((url: string) => {
      this.apiUrlSubject.next(url);
      console.log('Aktualisierte API-URL:', url);
    });
  }

  createEinnahmeTransaktion(transaktion: TransaktionEinnahme): void {
    this.apiUrl$.pipe(
      filter((url): url is string => url !== null),
      take(1),
      switchMap(apiUrl=>
        this.getAllTransaktionen().pipe(
          take(1),
          map(alleTransaktionen => {
            if (transaktion.transaktionsArt === 'einnahme') {
              alleTransaktionen.einnahmen.push(transaktion as TransaktionEinnahme);
            }
            return alleTransaktionen;
          }),
          switchMap(aktualisierteTransaktionen =>
            this.http.put<TransaktionUebersicht>(`${apiUrl}`, aktualisierteTransaktionen)
          )
        )
      ),
      catchError(error => {
        console.error('Fehler beim Erzeugen der Transaktion', error);
        return throwError(() => error);
      })
    ).subscribe(() => {
      console.log('Transaktion erfolgreich gespeichert:', transaktion);
    });
  }

  createAusgabenTransaktion(transaktion: TransaktionAusgabe[]): void {
    this.apiUrl$.pipe(
      filter((url): url is string => url !== null),
      take(1),
      switchMap(apiUrl=>
        this.getAllTransaktionen().pipe(
          take(1),
          map(alleTransaktionen => {
            transaktion.forEach((abschnitt: TransaktionAusgabe ) => {
              if (abschnitt.transaktionsArt === 'ausgabe') {
                console.log('Wird gepusht:', transaktion);
                alleTransaktionen.ausgaben.push(abschnitt as TransaktionAusgabe);
              }
            });

            return alleTransaktionen;
          }),
          switchMap(aktualisierteTransaktionen =>
            this.http.put<TransaktionUebersicht>(`${apiUrl}`, aktualisierteTransaktionen)
          )
        )
      ),
      catchError(error => {
        console.error('Fehler beim Erzeugen der Transaktion', error);
        return throwError(() => error);
      })
    ).subscribe(() => {
      console.log('Transaktion erfolgreich gespeichert:', transaktion);
    });
  }

  createNotizTransaktion(transaktion: TransaktionNotiz): void {
    this.apiUrl$.pipe(
      filter((url): url is string => url !== null),
      take(1),
      switchMap(apiUrl=>
        this.getAllTransaktionen().pipe(
          take(1),
          map(alleTransaktionen => {
            if (transaktion.transaktionsArt === 'notiz') {
              alleTransaktionen.notizen.push(transaktion as TransaktionNotiz);
            }

            return alleTransaktionen;
          }),
          switchMap(aktualisierteTransaktionen =>
            this.http.put<TransaktionUebersicht>(`${apiUrl}`, aktualisierteTransaktionen)
          )
        )
      ),
      catchError(error => {
        console.error('Fehler beim Erzeugen der Transaktion', error);
        return throwError(() => error);
      })
    ).subscribe(() => {
      console.log('Transaktion erfolgreich gespeichert:', transaktion);
    });
  }

  getTransaktion(id: string): Observable<Transaktion> {
    return this.apiUrl$.pipe(
      filter((url): url is string => url !== null),
      take(1),
      switchMap(apiUrl=>
        this.http.get<Transaktion>(`${apiUrl}/${id}`).pipe(
          catchError(error => {
            console.error('Fehler beim Laden der Transaktion:', error);
            throw error;
          })
        )
      )
    );
  }

  getAllTransaktionen(): Observable<TransaktionUebersicht> {
    console.log('getAllTransaktionen')
    return this.apiUrl$.pipe(
      filter((url): url is string => url !== null),
      take(1),
      switchMap(apiUrl=>
        this.http.get<TransaktionUebersicht>(`${apiUrl}`).pipe(
          catchError(error => {
            console.error('Fehler beim Laden aller Transaktionen', error);
            throw error;
          })
        )
      )
    );
  }

  deleteTransaktion() {

  }

}
3
