
# FIW-Studienplaner Backend


### Vorraussetzungen

	Java 21
	
	lokale PostgreSQL-Datenbank mit folgenden default-Werten:
	
```
    Datenbankname: studyplannerdb
    Datenbank-User: postgres
    Datenbank-Password: postgres
```

oder 

	Zugang zu einer entfernten PostgreSQL-Datenbank über eine Datei mit dem Namen "application-local.properties" und folgendem Inhalt:
	

```
    POSTGRESQL_URL=jdbc:postgresql://<psql-link-zum-Datenbank-Server>:<port>/<Datenbank-Name>
    POSTGRESQL_USERNAME=<Datenbank-Username>
    POSTGRESQL_PASSWORD=<Datenpank-Password>
```


### Herunterladen und ausführen

Im Terminal:

```
git clone https://github.com/fiw-studienplaner-team1/backend.git
```

In der Lieblings-IDE den entstandenen Ordner "/backend" als Projekt importieren 
Falls entfernte Datenbank: In der IDE unter "/studyPlanner/src/main/resources/" die Datei `application-local.properties` erstellen und in der oben genannten Form füllen. Falls der Server-Standort das erfordert, jetzt auch VPN einschalten.

Im Ordner backend im Terminal `./mvnw spring-boot:run` ausführen (unter Windows ruft man den Maven-Wrapper mit `mvnw.cmd spring-boot:run` auf).

​Nun im Browser [localhost:8080](localhost:8080) aufrufen. Es erscheint vorläufig eine Fehlermeldung "Whitelabel Error Page", die hier anzeigt, dass der Backend-Server läuft 

