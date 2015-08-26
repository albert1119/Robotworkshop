// Adafruit Motor shield library
// copyright Adafruit Industries LLC, 2009
// this code is public domain, enjoy!
//#include <SoftwareSerial.h> 
#include <Wire.h>  //引用二個函式庫SoftwareSerial及Wire SoftwareSerial 
#include <string.h>
#include <AFMotor.h>
//#include <Servo.h> 
#include <ServoTimer2.h>

const int DCMotorPin =  21;

//SoftwareSerial I2CBT(50,52); //定義PIN24及PIN22分別為RX及TX腳位 

String lastCmd = "";
ServoTimer2 servo1;

AF_DCMotor motorFL(1);
AF_DCMotor motorFR(4);
AF_DCMotor motorBL(2);
AF_DCMotor motorBR(3);

void setup() {
  Serial.begin(9600);           // set up Serial library at 9600 bps
  Serial2.begin(9600); //藍牙鮑率9600
  Serial.println("Santa Version 1 beta");
  
  pinMode(DCMotorPin, OUTPUT);
  digitalWrite(DCMotorPin, HIGH);
  servo1.attach(10);
  // turn on motor
  motorFL.setSpeed(200);
  motorFR.setSpeed(200);
  motorBL.setSpeed(200);
  motorBR.setSpeed(200); 
  servo1.write(500);
  motorFL.run(RELEASE);
  motorFR.run(RELEASE);
  motorBL.run(RELEASE);
  motorBR.run(RELEASE);
  
}

void loop() {
  //servo1.write(0);
  while (Serial2.available() > 0) {
    String cmmd = Serial2.readStringUntil('\n');
    if(lastCmd != cmmd)
    {
      //Serial.println(cmmd);
      int Cmd = getValue(cmmd, '#', 0).toInt();
      int FR = getValue(cmmd, '#', 1).toInt(); 
      int BR = getValue(cmmd, '#', 2).toInt(); 
      int FL = getValue(cmmd, '#', 3).toInt();
      int BL = getValue(cmmd, '#', 4).toInt();
      int SERVO_ANG = getValue(cmmd, '#', 5).toInt();
      int DCMotor = getValue(cmmd, '#', 6).toInt();
      //Serial.println(SERVO_ANG);
      if(FR > 255)
      {
        FR = 255;
      }
      
      if(FL > 255)
      {
        FL = 255;
      }
      
      if(BR > 255)
      {
        BR = 255;
      }
      
      if(BL > 255)
      {
        BL = 255;
      }
      
      
      switch(Cmd)
      {
        case 0:
          Stop();
          break;
        case 1:
          if(FR >=0)
          {
            MoveForward(FR,BR,FL,BL);
          }
          else
          {
            MoveBackward(-FR,-BR,-FL,-BL);
          }
          break;
        case 2:
          break;
        case 3:
          break;
        case 4:
          break;
      }
      if(DCMotor == 0)
      {
        digitalWrite(DCMotorPin, HIGH);
      }
      else
      {
        digitalWrite(DCMotorPin, LOW);
      }
      if(SERVO_ANG == 0)
      {
        servo1.write(500);
      }
      else
      {
        servo1.write(2300);
      }
      
      
    }
    lastCmd = cmmd; 
  }
  
}

void Stop()
{
  motorFL.run(RELEASE);
  motorFR.run(RELEASE);
  motorBL.run(RELEASE);
  motorBR.run(RELEASE);
}

void MoveForward(int FR, int BR, int FL, int BL)
{
  motorFL.run(FORWARD);
  motorFR.run(FORWARD);
  motorBL.run(FORWARD);
  motorBR.run(FORWARD);
  motorFL.setSpeed(FL);
  motorFR.setSpeed(FR);
  motorBL.setSpeed(BL);
  motorBR.setSpeed(BR); 
}

void MoveBackward(int FR, int BR, int FL, int BL)
{
  motorFL.run(BACKWARD);
  motorFR.run(BACKWARD);
  motorBL.run(BACKWARD);
  motorBR.run(BACKWARD);
  motorFL.setSpeed(FL);
  motorFR.setSpeed(FR);
  motorBL.setSpeed(BL);
  motorBR.setSpeed(BR);  
}

String getValue(String data, char separator, int index)
{
 int found = 0;
  int strIndex[] = {
0, -1  };
  int maxIndex = data.length()-1;
  for(int i=0; i<=maxIndex && found<=index; i++){
  if(data.charAt(i)==separator || i==maxIndex){
  found++;
  strIndex[0] = strIndex[1]+1;
  strIndex[1] = (i == maxIndex) ? i+1 : i;
  }
 }
  return found>index ? data.substring(strIndex[0], strIndex[1]) : "";
}

// this function just increments a value until it reaches a maximum
int incPulse(int val, int inc){
   if( val + inc  > 2000 )
	return 1000 ;
   else
	 return val + inc;
}


