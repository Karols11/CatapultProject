#include <Servo.h>
Servo servoLeft;// Declare left servo 
Servo servoRight;
char Incoming_value = 0;//Variable for storing Incoming_value
void setup(){
  servoLeft.attach(9); 
  servoRight.attach(10);
  Serial.begin(9600);
}

void loop() {
  if(Serial.available() > 0){
    Incoming_value = Serial.read();      
    Serial.print(Incoming_value);

    if(Incoming_value =='1'){//fullarmed
    servoLeft.writeMicroseconds(1300);
    delay(2500);
    servoLeft.writeMicroseconds(1500);
    delay(500);
    }
    if(Incoming_value =='0'){//release
    servoRight.writeMicroseconds(1700);
    delay(315);
    servoRight.writeMicroseconds(1500);
    delay(500);
    }
    if(Incoming_value =='2'){//halfarmed
    servoLeft.writeMicroseconds(1300);
    delay(1260);
    servoLeft.writeMicroseconds(1500);
    delay(1000);
    }
    if(Incoming_value =='3'){//disarmed
    servoLeft.writeMicroseconds(1700);
    delay(1950);
    servoLeft.writeMicroseconds(1500);
    delay(500);
    }
    if(Incoming_value =='4'){//block
    servoRight.writeMicroseconds(1300);
    delay(315);
    servoRight.writeMicroseconds(1500); 
    delay(500);
    }
    
  }
}
