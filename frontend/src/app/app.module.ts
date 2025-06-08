import {NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {Routes, RouterModule, RouterOutlet} from '@angular/router';
import { AppComponent } from './app.component';
import {TransaktionComponent} from "./transaktion/transaktion.component";
import {MatCard, MatCardContent, MatCardHeader} from "@angular/material/card";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
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
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {TransaktionService} from "./service/transaktion.service";
import {TransaktionUebersichtComponent} from "./transaktion-uebersicht/transaktion-uebersicht.component";
import {MatTable} from "@angular/material/table";
import {MatTableModule} from '@angular/material/table';
import {MatCardModule} from '@angular/material/card';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {MatProgressSpinner} from "@angular/material/progress-spinner";
import { BaseChartDirective } from 'ng2-charts';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';
import {UebersichtDiagrammComponent} from "./transaktion-uebersicht/uebersicht-diagramm/uebersicht-diagramm.component";
import {MatCheckbox} from "@angular/material/checkbox";
import {SchuldenComponent} from "./transaktion-uebersicht/schulden/schulden.component";
import {LoginComponent} from "./login/login.component";
import {AuthInterceptor} from "./service/auth-interceptor.service";
import {RegisterComponent} from "./register/register.component";

const appRoutes: Routes = [
  { path: '', component: TransaktionUebersichtComponent},
  { path: 'neu', component: TransaktionComponent },
  { path: 'bearbeiten', component: TransaktionComponent},
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '**', redirectTo: 'login'}
];

@NgModule({
  declarations: [
    AppComponent,
    TransaktionComponent,
    TransaktionUebersichtComponent,
    UebersichtDiagrammComponent,
    SchuldenComponent,
    LoginComponent,
    RegisterComponent
    ],
  bootstrap: [AppComponent],
    imports: [
        BrowserModule,
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
        MatProgressSpinner,
        BaseChartDirective,
        FormsModule,
        MatCheckbox,
    ],
  providers: [
    TransaktionService,
    provideAnimationsAsync(),
    provideHttpClient(withInterceptorsFromDi()),
    provideCharts(withDefaultRegisterables()),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ] })
export class AppModule {

}
