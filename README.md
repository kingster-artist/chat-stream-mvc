# chat-stream-mvc

## Sinn und Zweck
* Beispielprogramm, welches einen EventStream liefert
* Da wir mit WebMVC arbeiten ist dieses Beispiek auch mit MVC realisiert
* Szenario ist das Ausliefern und Erzeugen von Chatnachrichten

## Zur Technik
* `MessageController` ist der REST-Endpunkt
* `MessageRepository` ist die "Datenbank"
* `MessageController.getMessagesStream` erzeugt einen EventStream mit speziellem Content-Type, der Browser hält daraufhin die Verbindung offen
* In einem eigenen Thread wird auf neue Nachrichten geprüft und mittels des Streams ausgeliefert

  
