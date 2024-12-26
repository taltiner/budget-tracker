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
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import {TransaktionService} from "./service/transaktion.service";
import {TransaktionUebersichtComponent} from "./transaktion-uebersicht/transaktion-uebersicht.component";
import {MatTable} from "@angular/material/table";
import {MatTableModule} from '@angular/material/table';
import {MatCardModule} from '@angular/material/card';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {MatProgressSpinner} from "@angular/material/progress-spinner";

const appRoutes: Routes = [
  { path: '', component: TransaktionUebersichtComponent},
  { path: 'neu', component: TransaktionComponent },
];

@NgModule({ declarations: [
        AppComponent,
        TransaktionComponent,
        TransaktionUebersichtComponent
    ],
    bootstrap: [AppComponent], imports: [BrowserModule,
        RouterModule.forRoot(appRoutes),
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
        MatTable,
        MatTableModule,
        MatCardModule,
        MatProgressSpinner], providers: [TransaktionService, provideAnimationsAsync(), provideHttpClient(withInterceptorsFromDi())] })
export class AppModule {

}
