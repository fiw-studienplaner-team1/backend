## Was wurde gemacht?
<!-- Was sich geändert hat, in 1 bis 3 Stichpunkten. -->

## Zugehörige User Story
Closes/Fixes #<!-- Issuenummer, z. B. 8 -->

## Worauf soll die Reviewerin achten?

<!-- Was soll die Reviewerin besonders prüfen oder testen? Gibt es offene Fragen oder Besonderheiten? -->

## Testanleitung
1.
2.

## Checkliste (Autorin)

- [ ] Der Branch ist auf dem aktuellen stand von main (Branch vor dem PR mit `git rebase origin/main` aktualisieren).
- [ ] GitHub zeigt keinen Merge-Konflikt mit `main` an.
- [ ] Falls Konflikte angezeigt werden, sind diese gelöst (`git add <Konfliktdatei>` und `git rebase --continue`)
- [ ] `git push --force-with-lease -u origin <branch>`, dann PR erstellen.
- [ ] Die Akzeptanzkriterien des Issues sind erfüllt.
- [ ] Die Tests laufen lokal durch und die Anwendung startet.
- [ ] Die CI-Pipeline ist grün (sobald eingerichtet).
- [ ] Der PR enthält keine unbeteiligten Dateien oder Debug-Ausgaben.
- [ ] Eine andere Person ist als Reviewerin eingetragen.
- [ ] Doku angepasst oder nicht nötig.

## Checkliste  (Reviewerin)

- [ ] Alle Akzeptanzkriterien des Issues sind erfüllt.
- [ ] Der Code ist verständlich und folgt den Namenskonventionen.
- [ ] Für die neue Funktionalität gibt es Tests.
- [ ] Der PR entspricht der DoR and DoD.
- [ ] Der `Status` des Issues im Board steht auf `In review`.

## Hinweis:
- "Muss:"      muss vor dem Approve geändert werden.
- "Vorschlag:" ist optional, die Autorin entscheidet.
- Ablauf: Review → ggf. Request changes → Autorin überarbeitet, antwortet und klickt "Re-request review" → Reviewerin löst Threads auf und gibt Approve → Autorin mergt und löscht den Branch.
