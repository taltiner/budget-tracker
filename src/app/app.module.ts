import {NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {Routes, RouterModule, RouterOutlet} from '@angular/router';
import { AppComponent } from './app.component';
import {TransaktionComponent} from "./transaktion/transaktion.component";
import {MatCard, MatCardContent, MatCardHeader} from "@angular/material/card";
import {ReactiveFormsModule} from "@angular/forms";
import {MatFormField} from "@angular/material/form-field";
import { MatLabel } from '@angular/material/form-field';
import {MatInput} from "@angular/material/input";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import { MatIconModule } from '@angular/material/icon';
import {MatNativeDateModule, MatOption} from '@angular/material/core';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatButton} from "@angular/material/button";
import {MatRadioButton, MatRadioGroup} from "@angular/material/radio";
import {MatSelect} from "@angular/material/select";
import {StoreModule} from "@ngrx/store";
import {transaktionReducer} from "./state/transaktion.reducer";
import {EffectsModule} from "@ngrx/effects";
import { HttpClientModule } from '@angular/common/http';
import {TransaktionService} from "./service/transaktion.service";
import {TransaktionUebersichtComponent} from "./transaktion-uebersicht/transaktion-uebersicht.component";
import {MatTable} from "@angular/material/table";
import {MatTableModule} from '@angular/material/table';
import {MatCardModule} from '@angular/material/card';

const appRoutes: Routes = [
  { path: '', component: TransaktionUebersichtComponent},
  { path: 'neu', component: TransaktionComponent },
];

@NgModule({
  declarations: [
    AppComponent,
    TransaktionComponent,
    TransaktionUebersichtComponent
  ],
  imports: [
    BrowserModule,
    StoreModule.forRoot({transaktionen: transaktionReducer}),
    RouterModule.forRoot(appRoutes),
    EffectsModule.forRoot([]),
    MatCard,
    ReactiveFormsModule,
    MatCardHeader,
    MatCardContent,
    MatFormField,
    MatInput,
    MatLabel,
    BrowserAnimationsModule,
    MatDatepickerToggle,
    MatDatepicker,
    MatDatepickerInput,
    MatIconModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatButton,
    MatRadioGroup,
    MatRadioButton,
    MatSelect,
    MatOption,
    RouterOutlet,
    HttpClientModule,
    MatTable,
    MatTableModule,
    MatCardModule
  ],
  providers: [TransaktionService],
  bootstrap: [AppComponent]
})
export class AppModule {

}
