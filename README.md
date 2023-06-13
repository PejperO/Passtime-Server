## Passtime-Server
For each client, the server keeps a log of its queries and their results, as well as a general log of all client requests. Logs are stored in the server's internal memory (outside the file system)

The server (server class) provides information about the elapsed time.
Clients (Client class):
- connect to the server
- send requests


### Protocol
| Task        | Answer           | Example |
| ------------- |:-------------:| ------------- |
| login id      | logged in | login Adam |
| date from | description of the passage of time according to the specification | 2021-02-20 2021-04-30 |
| bye | logged out | |
| bye and log transfer | client log content | in the example printout of the Main class |


### Construction of the Server class
constructor: :
**public Server(String host, int port)**

method **public void stratServer()** - runs the server in a separate thread

method **public void stopServer()** - stops the server and the thread it is running on

method **String getServerLog()** - returns general server log

- multiplexing of socket channels

- The server can handle multiple clients in parallel, but client requests are handled in a single thread


### Construction of the Client class
constructor: :
**public Client(String host, int port, String id)**

method **public void connect()** - connects to the server

method **public String send(String req)** - sends a req request and returns the server response

- non-blocking input - output


### Console results
the contents of the PassTimeServerOptions.yaml file

    host: localhost
    port: 7777
    concurMode: true
    showSendRes: true
    clientsMap:
     Asia:
      - 2016-03-30T12:00 2020-03-30T:10:15
      - 2019-01-10 2020-03-01
      - 2020-03-27T10:00 2020-03-28T10:00
      - 2016-03-30T12:00 2020-03-30T10:15
    Adam:
     - 2018-01-01 2020-03-27
     - 2019-01-01 2020-02-28
     - 2019-01-01 2019-02-29
      - 2020-03-28T10:00 2020-03-29T10:00
      
result

    localhost 7777 true true
    Asia: [2016-03-30T12:00 2020-03-30T:10:15, 2019-01-10 2020-03-01, 2020-03-27T10:00 2020-03-28T10:00, 2016-03-30T12:00 2020-03-30T10:15]
    Adam: [2018-01-01 2020-03-27, 2019-01-01 2020-02-28, 2019-01-01 2019-02-29, 2020-03-28T10:00 2020-03-29T10:00]

    Asia
    *** java.time.format.DateTimeParseException: Text '2020-03-30T:10:15' could not be parsed, unparsed text found at index 10
    Od 10 stycznia 2019 (czwartek) do 1 marca 2020 (niedziela)
    - mija: 416.0 dni, tygodni 59.43
    - kalendarzowo: 1 rok, 1 miesi�c, 20 dni
    Od 27 marca 2020 (pi�tek) godz. 10:00 do 28 marca 2020 (sobota) godz. 10:00
     - mija: 1 dzie�, tygodni 0.14
    - godzin: 24, minut: 1440
    - kalendarzowo: 1 dzie�
    Od 30 marca 2016 (�roda) godz. 12:00 do 30 marca 2020 (poniedzia�ek) godz. 10:15
    - mija: 1460 dni, tygodni 208.57
    - godzin: 35062, minut: 2103735
     - kalendarzowo: 4 lata
    Adam
    Od 1 stycznia 2018 (poniedzia�ek) do 27 marca 2020 (pi�tek)
     - mija: 816.0 dni, tygodni 116.57
     - kalendarzowo: 2 lata, 2 miesi�ce, 26 dni
    Od 1 stycznia 2019 (wtorek) do 28 lutego 2020 (pi�tek)
     - mija: 423.0 dni, tygodni 60.43
     - kalendarzowo: 1 rok, 1 miesi�c, 27 dni
    *** java.time.format.DateTimeParseException: Text '2019-02-29' could not be parsed: Invalid   date 'February 29' as '2019' is not a leap year
    Od 28 marca 2020 (sobota) godz. 10:00 do 29 marca 2020 (niedziela) godz. 10:00
     - mija: 1 dzie�, tygodni 0.14
     - godzin: 23, minut: 1380
     - kalendarzowo: 1 dzie�

    
## What I Learned
* serverSocketChannel
* socketChannel
* localDate
* DecimalFormat
* ZonedDateTime
* LinkedHashMap
* yaml files

