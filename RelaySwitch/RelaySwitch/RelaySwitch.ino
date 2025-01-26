#include <ESP8266WiFi.h>
#include <WiFiClient.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>

#ifndef STASSID
#define STASSID "username"
#define STAPSK "password"
#endif

// Define veriables that Aux will use in code.
#define statusLedPin 2
#define relayPin 3

bool readOnlyMode = false;

//WebPages webpages;

const char* ssid = STASSID;
const char* password = STAPSK;

static ESP8266WebServer server(80);

// When the webpage link is "/" AKA the root of the domain then dispay this text.
void mainWebPage() {

  StreamString temp;
  temp.reserve(500);  // Preallocate a large chunk to avoid memory fragmentation
  temp.printf("\
<html>\
  <head>\
    <meta http-equiv='refresh' content='5'/>\
    <title>Aux Relay Switch</title>\
    <style>\
      body { background-color: Black; font-family: Arial, Helvetica, Sans-Serif; Color: #000088; }\
    </style>\
  </head>\
  <body>\
    <h1>Aux Relay Switch</h1>\
    <p>Mode(Switch/Read): </p>\
    <p>Status:</p>\
    <a>Turn ON</a>\
    <a>Turn OFF</a>\
  </body>\
</html>");
  server.send(200, "text/html", temp.c_str());

  //server.send(200, "text/plain", "hello from esp8266!\r\n");
}

void onWebPage() {

  if(readOnlyMode) {
  digitalWrite(relayPin, 0);
  } else {
  digitalWrite(relayPin, 1);
  }
}

void offWebPage() {

  digitalWrite(relayPin, 0);
}

// Respond 404 if a page is not found.
void handleNotFound() {

  String message = "File Not Found\n\n";
  message += "URI: ";
  message += server.uri();
  message += "\nMethod: ";
  message += (server.method() == HTTP_GET) ? "GET" : "POST";
  message += "\nArguments: ";
  message += server.args();
  message += "\n";
  for (uint8_t i = 0; i < server.args(); i++) { message += " " + server.argName(i) + ": " + server.arg(i) + "\n"; }
  server.send(404, "text/plain", message);

}


// Setup Starts configuring pinouts.
void setup(void) {

  Serial.begin(115200);

  // Setup Pinmodes for the chip.
  pinMode(statusLedPin, OUTPUT);
  pinMode(relayPin, OUTPUT);

  digitalWrite(statusLedPin, 1);
  digitalWrite(relayPin, 0);

  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  // Wait for connection. Print via serial untill recived.
  Serial.println("Connecting to network.");
  while (WiFi.status() != WL_CONNECTED) {
    digitalWrite(statusLedPin, 0);
    delay(500);
    Serial.print(".");
    digitalWrite(statusLedPin, 1);
  }

  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  if (MDNS.begin("esp8266")) { Serial.println("MDNS responder started"); }

  server.on("/", mainWebPage);

  server.on("/on", onWebPage);

  server.on("/off", offWebPage);

  //server.on("/read", offWebPage);

  //server.on("/modeswitch", modeSwitchWebPage);

  //server.on("/moderead", modeReadWebPage);


  server.onNotFound(handleNotFound);

  /////////////////////////////////////////////////////////
  // Hook examples

  server.addHook([](const String& method, const String& url, WiFiClient* client, ESP8266WebServer::ContentTypeFunction contentType) {
    (void)method;       // GET, PUT, ...
    (void)url;          // example: /root/myfile.html
    (void)client;       // the webserver tcp client connection
    (void)contentType;  // contentType(".html") => "text/html"
    Serial.printf("A useless web hook has passed\n");
    Serial.printf("(this hook is in 0x%08x area (401x=IRAM 402x=FLASH))\n", esp_get_program_counter());
    return ESP8266WebServer::CLIENT_REQUEST_CAN_CONTINUE;
  });

  server.addHook([](const String&, const String& url, WiFiClient*, ESP8266WebServer::ContentTypeFunction) {
    if (url.startsWith("/fail")) {
      Serial.printf("An always failing web hook has been triggered\n");
      return ESP8266WebServer::CLIENT_MUST_STOP;
    }
    return ESP8266WebServer::CLIENT_REQUEST_CAN_CONTINUE;
  });

  server.addHook([](const String&, const String& url, WiFiClient* client, ESP8266WebServer::ContentTypeFunction) {
    if (url.startsWith("/dump")) {
      Serial.printf("The dumper web hook is on the run\n");

      // Here the request is not interpreted, so we cannot for sure
      // swallow the exact amount matching the full request+content,
      // hence the tcp connection cannot be handled anymore by the
      // webserver.
#ifdef STREAMSEND_API
      // we are lucky
      client->sendAll(Serial, 500);
#else
      auto last = millis();
      while ((millis() - last) < 500) {
        char buf[32];
        size_t len = client->read((uint8_t*)buf, sizeof(buf));
        if (len > 0) {
          Serial.printf("(<%d> chars)", (int)len);
          Serial.write(buf, len);
          last = millis();
        }
      }
#endif
      // Two choices: return MUST STOP and webserver will close it
      //                       (we already have the example with '/fail' hook)
      // or                  IS GIVEN and webserver will forget it
      // trying with IS GIVEN and storing it on a dumb WiFiClient.
      // check the client connection: it should not immediately be closed
      // (make another '/dump' one to close the first)
      Serial.printf("\nTelling server to forget this connection\n");
      static WiFiClient forgetme = *client;  // stop previous one if present and transfer client refcounter
      return ESP8266WebServer::CLIENT_IS_GIVEN;
    }
    return ESP8266WebServer::CLIENT_REQUEST_CAN_CONTINUE;
  });

  // Hook examples
  /////////////////////////////////////////////////////////

  server.begin();
  Serial.println("HTTP server started");
}

void loop(void) {
  server.handleClient();
  MDNS.update();
}
