#include <ESP8266HTTPClient.h>
#include <DHT.h>
#include <DHT_U.h>
#include <ESP8266WiFi.h>


DHT dht(5, DHT11);
float temp, hume;
//String host = "192.168.54.15";
String host = "192.168.43.143";  // en la nueva red
//String host = "192.168.1.194";
int port = 6060;
//int port = 6000;
String mensaje = "";
int c = 0;
String id = "";
String tiempo = "";
String ssid = "HUAWEI Y6 2019";
String pass = "julio12345+";
//String ssid = "A20";
//String pass = "0040200011";

void setup() {
  Serial.begin(9600);
  dht.begin();
  WiFi.disconnect();
  WiFi.mode(WIFI_STA);
  //WiFi.beginWPSConfig();
  WiFi.begin(ssid, pass);
  
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.println(".");
  }

  Serial.println("WiFi connected");
  Serial.print("IP address: "); 
  Serial.println(WiFi.localIP());  

  wdt_disable();
  wdt_enable(WDTO_8S);
}

void loop() {  
  c = 0;  
  WiFiClient cliente;
  
  if (WiFi.status() == WL_CONNECTED) { // Estado de la conexión WIFI: WL_CONNECTED = true      
      if (cliente.connect(host, port)){ // Crea la conexión Socket    
          while (cliente.connected()){  
              //Serial.println("Respuesta:" + String(cliente.read()));                
              id = WiFi.macAddress();
              hume = dht.readHumidity();
              temp = dht.readTemperature();
              tiempo = getTime();
              mensaje = "ID=" + id + "|Temp=" + String(temp) + "|Hum=" + String(hume) + "|Tiempo=" + tiempo;
              Serial.println("Send to Server ->" + mensaje);
              cliente.println(mensaje);           
              delay(5000);    
              //yield();          
          }
          //cliente.stop();  
          Serial.println("Cliente Socket desconectado");             
      } else { // Si la conexión Socket falla          
          Serial.println("La conexión socket ha fallado");
          delay(5000);
          cliente.stop();
      }
  } else { // Estado de la conexión WIFI: WL_CONNECTED = false
      WiFi.disconnect();
      WiFi.mode(WIFI_STA);
      //WiFi.beginWPSConfig();
      WiFi.begin(ssid, pass);
      Serial.print("Intento de conexión    ");
      while (WiFi.status() != WL_CONNECTED && c < 30) {
          delay(600);
          Serial.print(".");
          c++;
      }
      if (WiFi.status() == WL_CONNECTED){
          Serial.println("OK");
      } else{
          Serial.println("Falló");
      }
      //return;
  }

    
}

// Función para obtener el tiempo, hora en segundos
  String getTime() {
    WiFiClient cliente2;
    HTTPClient http;
    String tiempo = "";
    long result = 0;
  
    http.begin(cliente2, "http://worldtimeapi.org/api/timezone/America/Caracas");
    int httpCode = http.GET();
    if( httpCode == HTTP_CODE_OK || httpCode == HTTP_CODE_MOVED_PERMANENTLY ) {
      String resultado = http.getString();
      int inicio = resultado.indexOf("unixtime");
      int fin = resultado.indexOf("utc_datetime");      
      tiempo = resultado.substring(inicio+10,fin-2);
      Serial.println(tiempo);
      
    }
    return tiempo;
  }
