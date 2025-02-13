/*
    This sketch establishes a TCP connection to a "quote of the day" service.
    It sends a "hello" message, and then prints received data.
*/

#include <ESP8266WiFi.h>

#ifndef STASSID
#define STASSID "username"
#define STAPSK "password"
#endif

const char* ssid = STASSID;
const char* password = STAPSK;
const char deviceType[]            = "0001"; 
const char deviceSoftwareVersion[] = "0001";
const char deviceIdentifcationl[]  = "0000001";

WiFiClient client;

// The IP of host device is same as gateway IP. 
// The router has a socket server setup that recives socket connections and manges the network of accessorys. 
const char* host = "192.168.0.189";
const uint16_t port = 8080;

// Device ID should not change and is assigned at birth (upload).
const long deviceID = 0;

void setup() {
  Serial.begin(115200);

  // We start by connecting to a WiFi network

  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  /* Explicitly set the ESP8266 to be a WiFi-client, otherwise, it by default,
     would try to act as both a client and an access-point and could cause
     network-issues with your other WiFi-devices on your WiFi-network. */
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());

  Serial.print("connecting to ");
  Serial.print(host);
  Serial.print(':');
  Serial.println(port);

  // Use WiFiClient class to create TCP connections
  if (!client.connect(host, port)) {
    Serial.println("connection failed");
    delay(5000);
    return;
  }     
}

void loop() {
  while (client.available()) {
    char ch = static_cast<char>(client.read());
    Serial.print(ch);

    infoRequest(ch);
  }
}


// If the char we recived was 'I' Then we should send the info header.
void infoRequest(char inChar) {

  if(inChar == 'I')
  {
    client.print(deviceType);
    client.print(deviceSoftwareVersion);
    client.print(deviceIdentifcationl);
    client.print(';');
  }
}
