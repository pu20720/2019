/* Board: ESP8266_main
**
**
**
**
*/

#include <SoftwareSerial.h>
SoftwareSerial NodeMCU(D2,D3);//uart腳位
///////////////////////////////////////////////////////////////////
#define BLYNK_PRINT Serial
#include <SPI.h>
#include <ESP8266WiFi.h>
#include <BlynkSimpleEsp8266.h>
#include <SimpleTimer.h>
///////////////////////////////////////////////////////////////////
char auth[] = "";
char ssid[] = "";
char pass[] = "";
/////////////////////////////////////以上，blynk辨識碼、WIFI帳密
float val;
float ph;
float TURB;
int temp;
int HIGh;
int feed;
////////////////////////////////////uart send 變數
#include <DHTesp.h>
const byte pin_DHT=D9;     
DHTesp dht;
///////////////////////////////////////////溫溼度
int t = 0 ;  //宣告控制溫度變數
int b = 0 ;  //宣告控制濁度變數
int p = 0 ;  //宣告控制ph值變數
int l = 0 ;  //宣告換水變數
int s = 0 ;  //宣告魚缸水位高度變數
int k = 0 ;  //宣告餵食數量變數

int o = 0 ;  //控制換水階段變數
//int m = 0 ;  //宣告真實的魚缸水位高度
int n = 0 ;  //宣告真實的飼料高度
WidgetTerminal terminal1(V11); //terminal虛擬腳位
WidgetTerminal terminal2(V12); //terminal虛擬腳位
WidgetTerminal terminal3(V13); //terminal虛擬腳位
WidgetTerminal terminal4(V16); //terminal虛擬腳位
WidgetTerminal terminal5(V17); //terminal虛擬腳位
WidgetTerminal terminal6(V18); //terminal虛擬腳位
//////////////////////////////////////////////////////terminal虛擬腳位
int Pin1 = D4; //加溫棒腳位
int Pin2 = D1; //加水電磁閥
int Pin3 = D5; //抽水馬達控制
/////////////////////////////////////////////////////繼電器腳位
#include <Servo.h>
Servo myservo; 
////////////////////////////// 建立Servo物件，控制伺服馬達
SimpleTimer timer;
void setup(){
  digitalWrite(Pin3, HIGH);//關閉抽水馬達初始化
  digitalWrite(Pin2, HIGH); //關閉進水電磁閥初始化
  digitalWrite(Pin1, HIGH);//加溫腳為初始化
  myservo.attach(D7); // 連接數位腳位9，伺服馬達的訊號線
  Blynk.begin(auth, ssid, pass);
  dht.setup(pin_DHT, DHTesp::DHT11); // DHT11,DHT22
  Serial.begin(9600);
  NodeMCU.begin(4800);
  pinMode(Pin1, OUTPUT);
  pinMode(Pin2, OUTPUT);
  pinMode(Pin3, OUTPUT);
  pinMode(D2,INPUT);//uart tx rx 腳位
  pinMode(D3,OUTPUT);//uart tx rx 腳位
  timer.setInterval(1000L, sendSensor);
}

void loop(){
 
  while(NodeMCU.available()>0){
  val = NodeMCU.parseFloat();
  if(NodeMCU.read()== '\n'){
  Serial.println(val);
 ////////////////////////////////////////////////
 temp=val/100000;
 Serial.print("temp:");
 Serial.println(temp);
 HIGh = (val/1000)-temp*100;
 //m= s-HIGh+10;//魚缸高度減去超音波數值
 Serial.print("HIGh:");
 Serial.println(HIGh);
 int i =val/10;
 int u =int(val/1000)*100;
 feed = i-u;
 n= 23-feed;//飼料和高度減去超音波數值
 Serial.print("feed:");
 Serial.println(n);
 ph =val-(i*10);
 Serial.print("ph:");
 Serial.println(ph);
  int sensorValue = analogRead(A0);// read the input on analog pin 0:
     TURB = sensorValue * (5.0 / 1024.0); // Convert the analog reading (which goes from 0 - 1023) to a voltage (0 - 5V):
   Serial.print("TURB:");
   Serial.println(TURB);
 ////////////////////////////////////////////////
 Serial.println(t);//監控視窗顯示變換值
 Serial.println(b);//監控視窗顯示變換值
 Serial.println(p);//監控視窗顯示變換值
 Serial.println(l);//監控視窗顯示變換值
 Serial.println(s);//監控視窗顯示變換值
 Serial.println(k);//監控視窗顯示變換值
 
  }
}

  Blynk.run(); // Initiates Blynk
  timer.run(); // Initiates SimpleTimer
  
}
BLYNK_WRITE(V11) { //called when V0 updated (user send data to V0) //利用terminal來客製化溫度設定
  //Serial.println(param.asStr()); //監控視窗顯示
  t = atoi(param.asStr()); //字串轉整數
  terminal1.print("您已設定溫度為:");
  terminal1.println(t);
  terminal1.flush(); //Ensure everything is sent
  }
BLYNK_WRITE(V12) { //called when V0 updated (user send data to V0) //利用terminal來客製化溫度設定
  //Serial.println(param.asStr()); //監控視窗顯示
   b = atoi(param.asStr()); //字串轉整數
 if(b<6 && b>=0){
  terminal2.print("您已設定濁度為:");
  terminal2.println(b);
 }else{
  terminal2.println("設定錯誤");
 }
  terminal2.flush(); //Ensure everything is sent
  }
BLYNK_WRITE(V13) { //called when V0 updated (user send data to V0) //利用terminal來客製化溫度設定
  //Serial.println(param.asStr()); //監控視窗顯示
  p = atoi(param.asStr()); //字串轉整數
  if(p<15 && p>=0){
  terminal3.print("您已設定ph為:");
  terminal3.println(p);
  }else{
  terminal3.println("設定錯誤");
  }
  terminal3.flush(); //Ensure everything is sent
  }
  BLYNK_WRITE(V16) { //called when V0 updated (user send data to V0) //利用terminal來客製化溫度設定
  //Serial.println(param.asStr()); //監控視窗顯示
  l = atoi(param.asStr()); //字串轉整數
  if(l>0){
  terminal4.print("您已設定換水高度為:");
  terminal4.println(l);
  }else{
  terminal4.println("設定錯誤");
  }
  terminal4.flush(); //Ensure everything is sent
  }
  BLYNK_WRITE(V17) { //called when V0 updated (user send data to V0) //利用terminal來客製化溫度設定
  //Serial.println(param.asStr()); //監控視窗顯示
  s = atoi(param.asStr()); //字串轉整數
  if(s>0){
  terminal5.print("您已設定魚缸高度為:");
  terminal5.println(s);
  }else{
  terminal5.println("設定錯誤");
  }
  terminal5.flush(); //Ensure everything is sent
  }
  BLYNK_WRITE(V18) { //called when V0 updated (user send data to V0) //利用terminal來客製化溫度設定
  //Serial.println(param.asStr()); //監控視窗顯示
  k = atoi(param.asStr()); //字串轉整數
  terminal6.print("您已設定次數為:");
  terminal6.println(k);
  terminal6.flush(); //Ensure everything is sent
  }
BLYNK_WRITE(V15) {
  Serial.print("Timer fired: ");
  Serial.println(param.asStr()); //get 1 or 0  
  for (int z=0; z <= k; z++){
   myservo.write(15);
  delay(300);
   myservo.write(70);
  delay(300);
  }
   
   Blynk.notify("以餵食");
  }
void sendSensor()
{
  
  if(ph<=p && TURB<=b && o==0){
    o=o+1;
    digitalWrite(Pin3, LOW);//打開抽水馬達
    digitalWrite(Pin2, HIGH); //關閉進水電磁閥
    Serial.println("o");//監控視窗顯示變換值
  }
  else if(HIGh>=l && o==1){ //水位高度小於設定換水高度
    o=o+1;
    digitalWrite(Pin3, HIGH);//關閉抽水馬達
    digitalWrite(Pin2, LOW); //打開進水電磁閥
    Serial.println("i");//監控視窗顯示變換值
  }
  else if(HIGh<=s && o==2){ //水位高度大於魚缸水位最高高度
    o=0;//
    digitalWrite(Pin3, HIGH);//關閉抽水馬達
    digitalWrite(Pin2, HIGH); //關閉進水電磁閥
    Serial.println("y");//監控視窗顯示變換值
  }
  if(temp>t){  //加溫器控制
    digitalWrite(Pin1, HIGH);
  }else{
    digitalWrite(Pin1, LOW);
    }
  float h = dht.getHumidity();
  float airtemp = dht.getTemperature();   // degree C
  if (isnan(ph)){
    Blynk.notify("ph");
  }else if (isnan(temp)){
    Blynk.notify("temp");
  }else if (isnan(TURB)){
    Blynk.notify("TURB");
  }else if (isnan(HIGh)){
    Blynk.notify("HIGh");
    
  }else if (isnan(feed)){
    Blynk.notify("feed");
  }
  Blynk.virtualWrite(V4,ph);
  Blynk.virtualWrite(V6,temp);
  Blynk.virtualWrite(V7,TURB);
  Blynk.virtualWrite(V8,HIGh);
  Blynk.virtualWrite(V9,h);
  Blynk.virtualWrite(V10,airtemp);
  Blynk.virtualWrite(V14,n);
}
/*
 if (isnan(ph)||isnan(temp)||isnan(TURB)||isnan(HIGh)||isnan(h)||isnan(airtemp)||isnan(feed)) {
    Serial.println("Failed to read from the sensor!");
    Blynk.notify("one of sensor Failed to read ");
 */
