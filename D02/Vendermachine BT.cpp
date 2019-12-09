#include <SoftwareSerial.h>
#include <LiquidCrystal_I2C.h> 
#include <Wire.h> 
#include <Servo.h>

#define BUTTON1_PIN 4
#define BUTTON2_PIN 5
#define BUTTON3_PIN 6
#define BUTTON4_PIN 7
Servo myservo1,myservo2,myservo3,myservo4;
//unsigned int selection;

bool BTS= false ;
bool BTL= false ;
int count = 0 ;

SoftwareSerial BT(12,3); //定義 PIN12 及 PIN3 分別為 RX 及 TX 腳位
LiquidCrystal_I2C lcd(0x3F,16,2);  //設定LCD位置0x3F,設定LCD大小為16*2

void setup() {
   //==========I2C LCD========// 
  BT.begin(9600); //藍牙鮑率：9600 
  //==========I2C LCD========// 
  // put your setup code here, to run once:
  lcd.init(); //初始化LCD 
  lcd.backlight(); //開啟背光
  lcd.setCursor(0, 0);
  lcd.print("Plz Insert Coin!"); 
  //=======counter========//
  pinMode( 2 , INPUT);
  pinMode( 13 , OUTPUT);
  Serial.begin(9600);

//=======motor========// 
  myservo1.attach(8);
  myservo2.attach(9);
  myservo3.attach(10);
  myservo4.attach(11);

//========button========// 
  pinMode(BUTTON1_PIN,INPUT);
  pinMode(BUTTON2_PIN,INPUT);
  pinMode(BUTTON3_PIN,INPUT);
  pinMode(BUTTON4_PIN,INPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  //Set price 1,2,3,4
  int price01 = 10;//define 1's price
  int price02 = 10;
  int price03 = 20;
  int price04 = 20;
  
  
  BTS = digitalRead(2) ;
  if (( ( BTS ) != ( BTL ) ))
  {
    if (( ( BTS ) == ( HIGH ) ))
    {
      digitalWrite( 13 , HIGH );
      count = ( count + 10 ) ;
      lcd.setCursor(0, 0);
      lcd.clear();
      lcd.print("Your Insert Coin : ");
      lcd.setCursor(0, 1);
    lcd.print(count); 
    }
  }
  BTL = BTS ;
  digitalWrite( 13 , LOW );
  delay( 50 );
  
  
  /*if(count !=0){
    Serial.println("Plz selext");
  }*/
  
  while(count!=0){//Enable Selection
    //你要繼續投就丟這 公道價啦! 
    //..... 
    BTS = digitalRead(2) ;
    if (( ( BTS ) != ( BTL ) )){
      if (( ( BTS ) == ( HIGH ) )){
        digitalWrite( 13 , HIGH );
      count = ( count + 10 ) ;
        lcd.setCursor(0, 0);
          lcd.clear();
          lcd.print("Your Insert Coin : ");
          lcd.setCursor(0, 1);
        lcd.print(count); 
      }
    }
  BTL = BTS ;
  digitalWrite( 13 , LOW );
  delay( 50 );
    
    int inSize;
    char input;
    if( (inSize = (BT.available())) > 0) { //讀取藍牙訊息
        Serial.print("size = ");
        Serial.println(inSize);
        Serial.print("Input = ");
        Serial.println(input=(char)BT.read());

    //選商品 BT
    if(input == 'b' && count>=price01){
            selextion1();
            count = 0;
        }else 
        if (input == 'b' && count>=price01)
        {
            s1_notEnough();
        }
        
        if(input == 'c'&& count>=price02){
            selextion2();
            count = 0;
        }
        else if (input == 'c'&& count>=price02)
        {
            s2_notEnough();
        }
        
        if(input == 'e'&& count>=price03){
            selextion3();
            count = 0;
        }else if (input == 'e'&& count>=price03)
        {
            s3_notEnough();
        }
        
        if(input == 'd'&& count>=price04){
            selextion4();
            count = 0;
        }else if (input == 'd'&& count>=price04)
        {
            s4_notEnough();
        }
    }
    
  //選商品Key
    if(digitalRead(BUTTON1_PIN)==HIGH && count >=price01){
            selextion1();
            count = 0;
        }else if(digitalRead(BUTTON1_PIN)==HIGH && count <=price01){
            s1_notEnough();
        }
        
        if(digitalRead(BUTTON2_PIN)==HIGH && count >=price02){
            selextion2();
            count = 0;
        }else if(digitalRead(BUTTON2_PIN)==HIGH && count <=price02){
            s2_notEnough();
        }
        
        if(digitalRead(BUTTON3_PIN)==HIGH && count >=price03){
            selextion3();
            count = 0;
        }else if(digitalRead(BUTTON3_PIN)==HIGH && count <=price03){
            s3_notEnough();
        }
        
        if(digitalRead(BUTTON4_PIN)==HIGH && count >=price04){
            selextion4();
            count = 0;
        }else if(digitalRead(BUTTON4_PIN)==HIGH && count <=price04){
            s4_notEnough();
        }
  }
}

//motor rotate
void selextion1(){
    lcd.setCursor(0, 1);
    lcd.clear();
    lcd.print("Selextion 001");
    lcd.setCursor(0, 1);
    lcd.print("Thanks!"); 
            
    myservo1.write(0);//push 
    delay(2000);//push time
    myservo1.write(90);//stop push
            
    delay(1000);
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("Plz Insert Coin!");
            
    //how u do??
}

void selextion2(){
    lcd.setCursor(0, 1);
    lcd.clear();
    lcd.print("Selextion 002");
    lcd.setCursor(0, 1);
    lcd.print("Thanks!"); 
            
    myservo2.write(0);
    delay(1800);
    myservo2.write(90);
            
    delay(1000);
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("Plz Insert Coin!");       
}

void selextion3(){
    lcd.setCursor(0, 1);
    lcd.clear();
    lcd.print("Selextion 003");
    lcd.setCursor(0, 1);
    lcd.print("Thanks!"); 
            
    myservo3.write(0);
    delay(1800);
    myservo3.write(90);
            
    delay(1000);
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("Plz Insert Coin!");       
}

void selextion4(){
    lcd.setCursor(0, 1);
    lcd.clear();
    lcd.print("Selextion 004");
    lcd.setCursor(0, 1);
    lcd.print("Thanks!"); 
            
    myservo4.write(0);
    delay(1800);
    myservo4.write(90);
            
    delay(1000);
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("Plz Insert Coin!");       
}

//Not Enough
void s1_notEnough(){
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("001_Not Enough!!");
}

void s2_notEnough(){
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("002_Not Enough!!");
}

void s3_notEnough(){
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("003_Not Enough!!");
}

void s4_notEnough(){
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("004_Not Enough!!");
}