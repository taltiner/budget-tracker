import {HttpClient, HttpParams} from "@angular/common/http";
import {
  TransaktionAusgabe,
  TransaktionEinnahme, TransaktionNotiz,
  TransaktionUebersicht,
  TransaktionUebersichtTransformiert
} from "../models/transaktion.model";
import {BehaviorSubject, catchError, concatMap, EMPTY, filter, from, map, Observable, of, switchMap, take, throwError} from "rxjs";
import {Injectable} from "@angular/core";
import {ToggleType} from "../models/toggle.model";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";

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

  updateEinnahmeTransaktion(transaktion: TransaktionEinnahme): void {
    this.apiUrl$.pipe(
      filter((url): url is string => url !== null),
      take(1),
      switchMap(apiUrl=>
      this.http.put<TransaktionEinnahme>( `${apiUrl}/einnahmen`, transaktion)
    ),
      catchError(error => {
        console.error('Fehler beim Aktualisieren der Transaktion', error);
        return throwError(() => error);
      })
    ).subscribe(() => {
      console.log('Transaktion erfolgreich aktualisiert:', transaktion);
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

  updateAusgabenTransaktion(transaktionen: TransaktionAusgabe[]): void {
    this.apiUrl$.pipe(
      filter((url): url is string => url !== null),
      take(1),
      switchMap(apiUrl =>
        this.http.put<TransaktionAusgabe[]>(`${apiUrl}/ausgaben`, transaktionen)

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

  getTransaktion(monat: string, jahr: string): Observable<TransaktionUebersicht> {
    let params = new HttpParams();
    params = params
      .set('monat', monat)
      .set('jahr', jahr);

    console.log('params', params);
    return this.http.get<TransaktionUebersicht>(`${this.backendUrl}`, {params}).pipe(
      catchError(error => {
        console.log('Fehler beim Laden der Transaktion', error);
        throw error;
      })
    )
  }

  getAllTransaktionen(): Observable<TransaktionUebersicht> {
    return this.apiUrl$.pipe(
      filter((url): url is string => url !== null),
      take(1),
      switchMap(apiUrl=>
        this.http.get<TransaktionUebersicht>(`${apiUrl}/alle`).pipe(
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

  deleteTransaktion(monat: string, jahr: string): Observable<{message: string}> {
    let params = new HttpParams();
    params = params
      .set('monat', monat)
      .set('jahr', jahr);

    return this.http.delete<{message: string}>(`${this.backendUrl}`, {params})
      .pipe(
        catchError(error => {
          console.log('Fehler beim Löschen der Transaktion', error);
          throw error;
        })
      )
  }

  filterTransaktion(selectedJahr: string): Observable<TransaktionUebersichtTransformiert[]> {
    return this.http.post<TransaktionUebersichtTransformiert[]>(`${this.backendUrl}/filtered`, selectedJahr).pipe(
      catchError(error => {
        console.log('Fehler beim filtern aller Transaktionen', error);
        throw error;
      })
    )
  }

  getToggles(): Observable<ToggleType[]> {
    return this.apiUrl$.pipe(
      filter((url): url is string => url !== null),
      take(1),
      switchMap(apiUrl=>
        this.http.get<ToggleType[]>(`${apiUrl}/toggle`).pipe(
          catchError(error => {
            console.error('Fehler beim Laden der Toggles', error);
            throw error;
          })
        )
      )
    );
  }

}
