#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <Servo.h>

//WiFi
const char * WIFI_SSID = "AndroidAP700D";
const char * WIFI_PASS = "pokemon123";

//MQTT
const char * BROKER = "broker.emqx.io";
const int BROKER_PORT = 1883;
const char * READ_TOPIC = "bee3";

WiFiClient espClient;
PubSubClient client(espClient);

#define MSG_BUFFER_SIZE  (50)
char msg_to_publish[MSG_BUFFER_SIZE];

//Servo motor
Servo servo;

bool IsEqualCommand(byte * payload, char * command){
  int count = 0;
  char * curr = command;
  while(*curr)
  {
    count++;
    curr++;
  }

  for (int i = 0; i < count; ++i)
  {
    
    if ((char)payload[i] != (char)command[i])
    {
      return false;
    }
  }

  return true;
}

void setup_wifi() {
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(WIFI_SSID);
  
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASS);
  servo.attach(0);

  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);  
  }

  Serial.println("");
  Serial.println("WiFi connected.");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

void reconnect() {
  while(!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Create a random client ID
    String clientId = "ESP8266Client-";
    clientId+= String(random(0xffff), HEX);

    if(client.connect(clientId.c_str())) {
      Serial.println("connected");
      client.publish(READ_TOPIC, "Hello! Unorthobox is up and running!");
      client.subscribe(READ_TOPIC);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

void callback(char *topic, byte *payload, unsigned int length) { //Sets the callback (What should esp do when it receives a message)
    Serial.print("Message arrived in topic: ");
    Serial.println(topic);
    Serial.print("Message: ");
    String message;
    for (int i = 0; i < length; i++) {
        message = message + (char) payload[i];  // convert *byte to string
    }
    Serial.println(message);

    if(message == "unlock"){
      //Turn the servo to unlock box
      Serial.println("Unlocking... turning off");
      servo.write(180);
    }
    else if(message == "lock"){
      //Turn the servo to lock box
      Serial.println("Locking.... turning on");
      servo.write(0);
    }
    
}

void setup(){
  Serial.begin(115200);
  pinMode(0, OUTPUT);
  digitalWrite(0, HIGH);
  setup_wifi(); //Setup wifi
  client.setServer(BROKER, BROKER_PORT);
  client.setCallback(callback);

}

void loop(){
  if(!client.connected()) {
    reconnect();
  }
  client.loop();
}
