import {HttpClient} from "@angular/common/http";
import {Transaktion} from "../models/transaktion.model";
import {Observable} from "rxjs";

export class TransaktionService {
  private apiUrl = 'http://localhost:3000/budget-tracker';


  constructor(private http: HttpClient) { }

  createTransaktion(transaktion: Transaktion): Observable<Transaktion> {

    return this.http.post<Transaktion>(this.apiUrl, transaktion);
  }

  deleteTransaktion() {

  }


}
