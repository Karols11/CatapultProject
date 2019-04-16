#include <Servo.h>
Servo servoLeft; 
Servo servoRight;// Declare left servo
char Incoming_value = 0;                //Variable for storing Incoming_valuechar Incoming_value = 0;
void setup(){
  servoLeft.attach(9); 
  Serial.begin(9600);
}

void loop() {
  if(Serial.available() > 0){
    Incoming_value = Serial.read();      
    Serial.print(Incoming_value);

    if(Incoming_value =='1'){
    servoLeft.writeMicroseconds(1300);
    delay(1260);
    servoLeft.writeMicroseconds(1500);
    delay(1000);
    }
    if(Incoming_value =='2'){
    servoLeft.writeMicroseconds(1700);
    delay(1260);
    servoLeft.writeMicroseconds(1500);
    delay(1000);
    }
  }
}
