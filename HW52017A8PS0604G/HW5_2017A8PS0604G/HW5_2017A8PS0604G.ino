#include "esp_bt_main.h"
#include "esp_gap_bt_api.h"
#include "esp_bt_device.h"
#include "BluetoothSerial.h"

BluetoothSerial SerialBT;
int LED_BUILTIN = 2;
bool initBluetooth(const char *deviceName)
{
  if (!btStart()) {
    Serial.println("Failed to initialize controller");
    return false;
  }
 
  if (esp_bluedroid_init()!= ESP_OK) {
    Serial.println("Failed to initialize bluedroid");
    return false;
  }
 
  if (esp_bluedroid_enable()!= ESP_OK) {
    Serial.println("Failed to enable bluedroid");
    return false;
  }
 
  esp_bt_dev_set_device_name(deviceName);
 
  esp_bt_gap_set_scan_mode(ESP_BT_SCAN_MODE_CONNECTABLE_DISCOVERABLE);
  
 
}
 
void setup() {
  Serial.begin(115200);
  if (SerialBT.begin("ESP32 BT")) {
    pinMode(LED_BUILTIN, OUTPUT);
  }
  Serial.println("An error");
 
}
 
void loop() {
  while (SerialBT.available()) {
      Serial.println(SerialBT.read());
      digitalWrite(LED_BUILTIN, HIGH);
      delay(1000);
    }
    digitalWrite(LED_BUILTIN, LOW);
    delay(1000);
  }
