/* 
** Board: ESP8266_extra(RGB Controller)
** 
*/


#define BLYNK_PRINT Serial
#include <Adafruit_NeoPixel.h>
#include <SPI.h>
#include <BlynkSimpleEsp8266.h>
#include <ESP8266WiFi.h>
#define PIN 15  //  DIN PIN (GPIO15, D8)
#define NUMPIXELS 60 // Number of you led
Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);
 
char auth[] = "";
char ssid[] = "";
char pass[] = "";
const byte interruptPin = 0;
int state = 6;
int R = 255;
int G = 255;
int B = 255;

WidgetLCD lcd(V5);

BLYNK_WRITE(V0) { // RGB Controll
  R = param[0].asInt();
  G = param[1].asInt();
  B = param[2].asInt();

  Blynk.virtualWrite(V1, R);
  Blynk.virtualWrite(V2, G);
  Blynk.virtualWrite(V3, B);
}

BLYNK_WRITE(V1) { // Red LED
  R = param[0].asInt();
}

BLYNK_WRITE(V2) { // Green LED
  G = param[0].asInt();
}

BLYNK_WRITE(V3) { // Blue LED
  B = param[0].asInt();
}


void setup() {
  Serial.begin(115200);
  Serial.println("hello");
  pinMode(interruptPin, INPUT);
  attachInterrupt(interruptPin, neoState, CHANGE);

  Blynk.begin(auth, ssid, pass);
  lcd.clear();
  pixels.begin();

}

void loop() {
  
  Serial.println(state);
  if (state == 0 || state == 1 || state == 2 || state == 6 ) Blynk.run();
  lcdShow();

  switch (state) {
    case 0:
    colorWipe(pixels.Color(0, 0, 0), 0); // Red
      //colorWipe(pixels.Color(0, 255, 0), 50); // Green
      //colorWipe(pixels.Color(0, 0, 255), 50); // Blue
      break;
    case 1: theaterChase(pixels.Color(R, G, B), 50);
      break;
    case 2:
      for (int i = 0; i < NUMPIXELS; i++) {
        pixels.setPixelColor(i, pixels.Color(R, G, B));
        pixels.show();
      }
      break;
    case 3: rainbow(20);
      break;
    case 4: rainbowCycle(20);
      break;
    case 5: theaterChaseRainbow(50);
      break;
  }
}

///////////////////////////////////////////////

void neoState() {
  state = state + 1;
  if (state > 5) state = 0;
}

void lcdShow() {
  lcd.print(6, 0, "MODE");
  if (state == 0) {
    lcd.print(0, 1, "       no       ");
  } else if (state == 1) {
    lcd.print(0, 1, "    THEATER     ");
  } else if (state == 2) {
    lcd.print(0, 1, "     MANUAL     ");
  } else if (state == 3) {
    lcd.print(0, 1, "     RAINBOW    ");
  } else if (state == 4) {
    lcd.print(0, 1, " RAINBOW CIRCLE ");
  } else if (state == 5) {
    lcd.print(0, 1, " THEATER RAINBOW");
  }
}

void theaterChase(uint32_t c, uint8_t wait) {
  for (int j = 0; j < 10; j++) { //do 10 cycles of chasing
    for (int q = 0; q < 3; q++) {
      for (uint16_t i = 0; i < pixels.numPixels(); i = i + 3) {
        pixels.setPixelColor(i + q, c);  //turn every third pixel on
      }
      pixels.show();
      delay(wait);

      for (uint16_t i = 0; i < pixels.numPixels(); i = i + 3) {
        pixels.setPixelColor(i + q, 0);      //turn every third pixel off
      }
    }
  }
}

void colorWipe(uint32_t c, uint8_t wait) {
  for (uint16_t i = 0; i < pixels.numPixels(); i++) {
    pixels.setPixelColor(i, c);
    pixels.show();
    delay(wait);
  }
}

void rainbow(uint8_t wait) {
  uint16_t i, j;

  for (j = 0; j < 256; j++) {
    if (state != 3) break;
    Blynk.run();
    for (i = 0; i < pixels.numPixels(); i++) {
      if (state != 3) break;
      Blynk.run();
      pixels.setPixelColor(i, Wheel((i + j) & 255));
    }
    pixels.show();
    delay(wait);
  }
}

void rainbowCycle(uint8_t wait) {
  uint16_t i, j;

  for (j = 0; j < 256 * 5; j++) { // 5 cycles of all colors on wheel
    if (state != 4) break;
    Blynk.run();
    for (i = 0; i < pixels.numPixels(); i++) {
      if (state != 4) break;
      Blynk.run();
      pixels.setPixelColor(i, Wheel(((i * 256 / pixels.numPixels()) + j) & 255));
    }
    pixels.show();
    delay(wait);
  }
}

void theaterChaseRainbow(uint8_t wait) {
  for (int j = 0; j < 256; j++) {   // cycle all 256 colors in the wheel
    if (state != 5) break;
    Blynk.run();
    for (int q = 0; q < 3; q++) {
      if (state != 5) break;
      Blynk.run();
      for (uint16_t i = 0; i < pixels.numPixels(); i = i + 3) {
        if (state != 5) break;
        Blynk.run();
        pixels.setPixelColor(i + q, Wheel( (i + j) % 255)); //turn every third pixel on
      }
      pixels.show();
      delay(wait);

      for (uint16_t i = 0; i < pixels.numPixels(); i = i + 3) {
        if (state != 5) break;
        Blynk.run();
        pixels.setPixelColor(i + q, 0);      //turn every third pixel off
      }
    }
  }
}

uint32_t Wheel(byte WheelPos) {
  WheelPos = 255 - WheelPos;
  if (WheelPos < 85) {
    return pixels.Color(255 - WheelPos * 3, 0, WheelPos * 3);
  }
  if (WheelPos < 170) {
    WheelPos -= 85;
    return pixels.Color(0, WheelPos * 3, 255 - WheelPos * 3);
  }
  WheelPos -= 170;
  return pixels.Color(WheelPos * 3, 255 - WheelPos * 3, 0);
}
