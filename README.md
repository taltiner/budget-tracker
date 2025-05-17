Transaktions-Management-System
Überblick
Ein System zur Verwaltung von monatlichen Einnahmen, Ausgaben und Notizen mit einer tabelarischen und grafischen (mit Chart.js) Ansicht. Das Backend ist mit Spring Boot erstellt, und das Frontend nutzt Angular.

Features:
  - Monatliche Transaktionen (Einnahmen, Ausgaben, Notizen)

  - Tabelarische und grafische Ansicht

  - Diagramme mit Chart.js

  - Auswahl der Merkmale für das Diagramm

  - Fallback-Mechanismus mit einem JSON-Server, wenn das Backend nicht verfügbar ist

Installation:
  Frontend (Angular)

  In das Frontend-Verzeichnis navigieren:
    cd frontend

  Abhängigkeiten installieren:
    npm install
  
  Entwicklungsserver starten:
    ng serve

  Backend (Spring Boot)
  In das Backend-Verzeichnis navigieren:
    cd backend

  Abhängigkeiten installieren:
    mvn clean install

  Spring Boot-Anwendung starten:
    mvn spring-boot:run

  JSON-Server für Fallback
  JSON-Server installieren:
    npm install -g json-server
    
  JSON-Server starten:
    json-server --watch db.json --port 3000
    
Tests:
  Unit-Tests im Backend mit JUnit und Mockito

  Frontend-Tests mit Jasmine
