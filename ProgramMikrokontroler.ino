#include <WiFi.h>
#include <Firebase_ESP_Client.h>
#include <SPI.h>
#include <MFRC522.h>
#include <time.h>
#include <iostream>
#include <string>  

#define RST_PIN    2 
#define SS_PIN     5  
#define LED_PIN    13 

#define WIFI_SSID "----"
#define WIFI_PASSWORD "----"

#define FIREBASE_HOST "https://firebase.google.com/project/esp32-ea05b/"
#define API_KEY "----"
#define FIREBASE_PROJECT_ID "esp32-ea05b"

#define USER_EMAIL "iphone@zadanie.com"
#define USER_PASSWORD "11223344"

FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

struct tm timeinfo;

int readsuccess;
byte readcard[4];
char str[32] = "";
String StrUID;

const long  gmtOffset_sec = 3600;
const int   daylightOffset_sec = 3600;
const char* ntpServer = "europe.pool.ntp.org";

MFRC522 mfrc522(SS_PIN, RST_PIN);  // Create MFRC522 instance.
int proba = 1;

void fsInit(){
  config.host = FIREBASE_HOST;
  config.api_key = API_KEY;
  //przypisanie danych konfiguracynych
  auth.user.email = USER_EMAIL;
  auth.user.password = USER_PASSWORD;
  //przypisanie danych autoryzacyjnych
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);
  //inicjalizacja połączenia z platformą Firebase
  //ponowne połączenie Wi-Fi
}

void wifiInit(){
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
      digitalWrite(LED_PIN,LOW);
      Serial.print(".");     
      delay(200);
      digitalWrite(LED_PIN,HIGH);
      delay(200);
  }
  Serial.print("Connected to WiFi");
  digitalWrite(LED_PIN,HIGH);
}
void setup()
{
  SPI.begin();  //SPI ON
  mfrc522.PCD_Init(); //MFRC-522 ON
  pinMode(LED_PIN, OUTPUT); //Led setup
  Serial.begin(115200); //komunikacja szeregowa
  wifiInit(); //Wi-Fi ON
  fsInit(); //FS ON
  Serial.print(" Tap a RFID card");
  configTime(gmtOffset_sec, daylightOffset_sec, ntpServer);
}

int numer = 0;


void loop()
{
  //printLocalTime();
  digitalWrite(LED_PIN,HIGH);//tryb: GOTOWOSC DO ODCZYTU
  if(mfrc522.PICC_IsNewCardPresent() && mfrc522.PICC_ReadCardSerial()){
    digitalWrite(LED_PIN, LOW); //tryb: ODCZYT ZADAŃ
    getid();//odczytanie danych z karty
    setfs();//wysłanie danych do Firestore
    Serial.println("Zadanie wykonano o godzinie: " + getTime());
    Serial.println("Dnia: " + getDate()); //wypisanie daty i godziny
  }
}

String getTime(){
  time_t rawtime;
  struct tm * timeinfo;
  char buffer [100];
  time (&rawtime);
  timeinfo = localtime (&rawtime);
  strftime (buffer,100,"%T",timeinfo);
  String timeNow;
  timeNow = String(buffer);
  return timeNow;
}
String getDate(){
  time_t rawtime;
  struct tm * timeinfo;
  char buffer [100];
  time (&rawtime);
  timeinfo = localtime (&rawtime);
  strftime (buffer,100,"%F",timeinfo);
  String timeNow;
  timeNow = String(buffer);
  return timeNow;
}

void getid(){  
  Serial.println("THE UID OF THE SCANNED CARD IS: ");
  for(int i=0;i<4;i++){
    readcard[i]=mfrc522.uid.uidByte[i]; //przechowywanie danych karty w 'readcard'
    array_to_string(readcard, 4, str);
    StrUID = str;
  }
  Serial.println(StrUID);
  mfrc522.PICC_HaltA();
}
void setfs(){
  String content; //zmienna przechowująca zawartość w formie JSON
  FirebaseJson js; //inicjalizacja obiektu do budowy struktury JSON
  numer++; 
  String documentPath = "historia_" + String(proba) + "/pomiar_" + String(numer);//utworzenie ściezki
  js.set("fields/taskNumber/stringValue", String(StrUID).c_str()); //dodanie pola z identyfikatorem karty
  js.set("fields/taskTime/stringValue", String(getTime()).c_str()); //dodanie pola z godzina
  js.set("fields/taskDate/stringValue", String(getDate()).c_str()); 
  js.toString(content);//konwersja struktury JSON na string
  Serial.println("------------------------------------");
  Serial.println("Create a document...");//wypisanie o probie utworzenia dokumentu

  if (Firebase.Firestore.createDocument(&fbdo, FIREBASE_PROJECT_ID, "", documentPath.c_str(), content.c_str())){
    // Czy udalo się utworzyc nowy dokument w Firestore
    Serial.println("SUKCES");}
  else {
    Serial.println("BLAD: " + fbdo.errorReason());}
    //W przeciwnym razie, wypisany komunikat o błędzie zwróconym przez Firebase.
}

void array_to_string(byte array[], unsigned int len, char buffer[])
{
    for (unsigned int i = 0; i < len; i++)
    {
        byte nib1 = (array[i] >> 4) & 0x0F;
        byte nib2 = (array[i] >> 0) & 0x0F;
        buffer[i*2+0] = nib1  < 0xA ? '0' + nib1  : 'A' + nib1  - 0xA;
        buffer[i*2+1] = nib2  < 0xA ? '0' + nib2  : 'A' + nib2  - 0xA;
    }
    buffer[len*2] = '\0';
}
