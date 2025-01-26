import { HttpClient } from "@angular/common/http";
import {
  Transaktion,
  TransaktionAusgabe,
  TransaktionEinnahme, TransaktionNotiz,
  TransaktionUebersicht,
  TransaktionUebersichtTransformiert
} from "../models/transaktion.model";
import {BehaviorSubject, catchError, concatMap, EMPTY, filter, from, map, Observable, of, switchMap, take, throwError} from "rxjs";
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
  backendRunningSubject = new BehaviorSubject<boolean>(false);

  kategorie$ = this.kategorieSubject.asObservable();
  dataSource$ = this.dataSourceSubject.asObservable();
  apiUrl$ = this.apiUrlSubject.asObservable();
  backendRunning$ = this.backendRunningSubject.asObservable();
  constructor(private http: HttpClient) {
    this.checkBackendStatus();
  }

  private checkBackendStatus(): void {
    this.http.get(this.backendUrl + '/health', { responseType: 'text' }).pipe(
      map(response => {
        const isRunning = response === 'Server is running';
        this.backendRunningSubject.next(isRunning);
        return isRunning ? this.backendUrl : this.frontendUrl
      }),
      catchError(() => of(this.frontendUrl))
    ).subscribe((url: string) => {
      this.apiUrlSubject.next(url);
    });
  }

  createEinnahmeTransaktion(transaktion: TransaktionEinnahme): void {
    this.apiUrl$.pipe(
      filter((url): url is string => url !== null),
      take(1),
      switchMap(apiUrl=>
        this.http.post<TransaktionUebersicht>(`${apiUrl}/einnahmen`, transaktion)
      ),
      catchError(error => {
        console.error('Fehler beim Erzeugen der Transaktion', error);
        return throwError(() => error);
      })
    ).subscribe(() => {
      console.log('Transaktion erfolgreich gespeichert:', transaktion);
    });
  }

  createAusgabenTransaktion(transaktionen: TransaktionAusgabe[]): void {
    this.apiUrl$.pipe(
      filter((url): url is string => url !== null),
      take(1),
      switchMap(apiUrl =>
        from(transaktionen).pipe(
          concatMap((abschnitt: TransaktionAusgabe) => {
            if (abschnitt.transaktionsArt === 'ausgabe') {
              return this.http.post<TransaktionUebersicht>(`${apiUrl}/ausgaben`, abschnitt);
            }
            return EMPTY;
          })
        )
      ),
      catchError(error => {
        console.error('Fehler beim Erzeugen der Transaktion', error);
        return throwError(() => error);
      })
    ).subscribe(() => {
      console.log('Transaktionen erfolgreich gespeichert');
    });
  }

  createNotizTransaktion(transaktion: TransaktionNotiz): void {
    this.apiUrl$.pipe(
      filter((url): url is string => url !== null),
      take(1),
      switchMap(apiUrl=>
        this.http.post<TransaktionUebersicht>(`${apiUrl}/notizen`, transaktion)
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

  getAllTransaktionenDBServer(): Observable<TransaktionUebersicht> {
    return this.http.get<TransaktionUebersicht>(`${this.frontendUrl}/`).pipe(
      catchError(error => {
        console.error('Fehler beim Laden aller Transaktionen', error);
        throw error;
      })
    )
  }

  deleteTransaktion() {

  }

  filterTransaktion(selectedJahr: string): Observable<Transaktion> {
    return this.http.post<Transaktion>(`${this.backendUrl}/filtered`, selectedJahr).pipe(
      catchError(error => {
        console.log('Fehler beim filtern aller Transaktionen', error);
        throw error;
      })
    )
  }

}
