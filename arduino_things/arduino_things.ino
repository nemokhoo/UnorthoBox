#include <ESP8266WiFi.h>
#include <PubSubClient.h>

//WiFi
const char * WIFI_SSID = "Loading Network SSID...";
const char * WIFI_PASS = "too awesome";

//MQTT
const char * BROKER = "broker.emqx.io";
const int BROKER_PORT = 1883;
const char * READ_TOPIC = "Unorthobox";

WiFiClient espClient;
PubSubClient client(espClient);

#define MSG_BUFFER_SIZE  (50)
char msg_to_publish[MSG_BUFFER_SIZE];

#define PIN_LED D1

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
      //Turn off the LED
      digitalWrite(PIN_LED, LOW);
    }
    else if(message == "lock"){
      //Turn it on
      digitalWrite(PIN_LED, HIGH);
    }
    
}

void setup(){
  Serial.begin(115200);
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
