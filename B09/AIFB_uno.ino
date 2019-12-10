/* 
** Board: ArduinoUno
** 水位超音波、水中溫度、水中PH
*/

#include <SoftwareSerial.h>
SoftwareSerial ArduinoUno(3,2);
String s;
String w;
#include <OneWire.h>
#include <DallasTemperature.h>
#define ONE_WIRE_BUS 4// Arduino數位腳位2接到1-Wire裝置
OneWire oneWire(ONE_WIRE_BUS);// 運用程式庫建立物件
DallasTemperature sensors(&oneWire);

#include <HCSR04.h> //超音波函示庫
HCSR04 hc(8,7); // 超音波腳位
#include <NewPing.h>
#define TRIG_PIN     10        // trigger Pin
#define ECHO_PIN     9     // Echo Pin
#define MAX_DISTANCE 200        // Maximum distance we want to ping for (in centimeters). Maximum sensor distance is rated at 400-500cm.

NewPing sonar(TRIG_PIN, ECHO_PIN, MAX_DISTANCE);    // 設定 NewPing 物件，並給與最遠測試距離

#define SensorPin A1           //pH meter Analog output to Arduino Analog Input 0
#define Offset 0.00            //deviation compensate
#define LED 13
#define samplingInterval 20
#define printInterval 800
#define ArrayLenth  40    //times of collection
int pHArray[ArrayLenth];   //Store the average value of the sensor feedback
int pHArrayIndex=0;  
float a;  
int tr;
int te;
int temp;
void setup(){
  
  Serial.begin(9600);
  ArduinoUno.begin(4800);
  sensors.begin();//水中溫度初始化 

}

void loop(){ 
//------------------------------------------------------------------
  static unsigned long samplingTime = millis();
  static unsigned long printTime = millis();
  static float voltage,pHValue;
  if(millis()-samplingTime > samplingInterval)
  {
      pHArray[pHArrayIndex++]=analogRead(SensorPin);
      if(pHArrayIndex==ArrayLenth)pHArrayIndex=0;
      voltage = avergearray(pHArray, ArrayLenth)*5.0/1024;
      pHValue = 3.5*voltage+Offset;
      if(pHValue>10||pHValue<0){
      a=8.9;
      }else{
      a=pHValue;
      }
      samplingTime=millis();
  }
  if(millis() - printTime > printInterval)   //Every 800 milliseconds, print a numerical, convert the state of the LED indicator
  {
 Serial.print("Voltage:");
        Serial.print(voltage,2);
        Serial.print("    pH value: ");
 Serial.println(pHValue,2);
        digitalWrite(LED,digitalRead(LED)^1);
        printTime=millis();
  }
//------------------------------------------------------------------
  sensors.requestTemperatures(); // 要求匯流排上的所有感測器進行溫度轉換
  if(sensors.getTempCByIndex(0)<=0){
     temp=26;
  }else{
     temp=sensors.getTempCByIndex(0);
  }
  Serial.println(temp); // 取得溫度讀數（攝氏）並輸出，參數0代表匯流排上第0個1-Wire裝置 
  int sensorValue = analogRead(A0);// read the input on analog pin 0:
  int TURB = sensorValue * (5.0 / 1024.0); // Convert the analog reading (which goes from 0 - 1023) to a voltage (0 - 5V):
   unsigned int uS = sonar.ping();   // 送出 ping，並取得微秒 microseconds(uS) 時間
  Serial.print("Ping: ");
  
  Serial.print(sonar.convert_cm(uS));    // 換算時間為公分，如顯示 0，表示超出距離範圍
  
  Serial.println("cm");
     delay(1000);
   
/////////////////////////////////////////////////
ArduinoUno.print(temp);

 int level =hc.dist();
 Serial.print("level:");
   Serial.println(level);
   if(level>=99 || level<0){
    tr=99;
   }else{
    tr=level;
   }
  if(tr<10){
w = String(tr);
w = '0'+w;
ArduinoUno.print(w);
Serial.println(w);
  }else{
    ArduinoUno.print(tr);
    Serial.println(tr);
  }
/////////////////////////////////////////////////
if(sonar.convert_cm(uS)>=99){
  te=27;
}else{
  te=sonar.convert_cm(uS);
}
  if( te<10){
s = String(te);

s = '0'+s; 
ArduinoUno.print(s);
}else{
ArduinoUno.print(te);
}
///////////////////////////////////////////////////
  
  ArduinoUno.print(a);
  ArduinoUno.println("\n");
}

double avergearray(int* arr, int number){
  int i;
  int max,min;
  double avg;
  long amount=0;
  if(number<=0){
    Serial.println("Error number for the array to avraging!}/n");
    return 0;
  }
  if(number<5){   //less than 5, calculated directly statistics
    for(i=0;i<number;i++){
      amount+=arr[i];
    }
    avg = amount/number;
    return avg;
  }else{
    if(arr[0]<arr[1]){
      min = arr[0];max=arr[1];
    }
    else{
      min=arr[1];max=arr[0];
    }
    for(i=2;i<number;i++){
      if(arr[i]<min){
        amount+=min;        //arr<min
        min=arr[i];
      }else {
        if(arr[i]>max){
          amount+=max;    //arr>max
          max=arr[i];
        }else{
          amount+=arr[i]; //min<=arr<=max
        }
      }//if
    }//for
    avg = (double)amount/(number-2);
  }//if
  return avg;
}
