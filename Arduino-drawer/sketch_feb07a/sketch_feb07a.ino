#include <Adafruit_GFX.h>
#include <Adafruit_PCD8544.h>

#define true 1
#define false 0

Adafruit_PCD8544 display = Adafruit_PCD8544(3, 4, 5, 6, 7);

void setup() {
  display.begin();
  display.clearDisplay();
  display.display();
  delay(500);
  // set contrast
  display.setContrast(60);
  delay(1000);
  Serial.begin(9600); //set transmission rates 9600 b/s
  }

void loop() {
  display.clearDisplay();
  display.display();
  delay(5000);
  int y , x;
  char incomingByte ;
  
  for(y=0;y<48;y++){
    if(Serial.available()>0){
      Serial.setTimeout(1000);
      for(x=0; x<84;x=x+7){
        incomingByte = Serial.read();
        Serial.write(incomingByte);

        if(incomingByte == 0b11100000){
        display.display();
        delay(5000);
        display.clearDisplay();
        display.println("End of enter");
        display.display();
        delay(5000);
        display.clearDisplay();}

        if ((incomingByte & 0b01000000) ==  0b1000000){
        display.drawPixel(x, y, BLACK);    //drawing
        display.display();
        }
        if ((incomingByte & 0b00100000) ==  0b100000){
        display.drawPixel(x+1, y, BLACK);    //drawing
        display.display();
        }
        if ((incomingByte & 0b00010000) ==  0b10000){
        display.drawPixel(x+2, y, BLACK);    //drawing
        display.display();
        }
        if ((incomingByte & 0b00001000) ==  0b1000){
        display.display();
        display.drawPixel(x+3, y, BLACK);    //drawing
        }
        if ((incomingByte & 0b00000100) ==  0b100){
        display.display();
        display.drawPixel(x+4, y, BLACK);    //drawing
        }
        if ((incomingByte & 0b00000010) ==  0b10){
        display.display();
        display.drawPixel(x+5, y, BLACK);    //drawing
        }
        if ((incomingByte & 0b00000001) ==  0b1){
        display.display();
        display.drawPixel(x+6, y, BLACK);    //drawing
        }
        display.display();
        }
        display.display();
        }
        else{
        display.display();
        delay(5000);
        display.clearDisplay();

        display.println("End of buffer");
        display.display();
        delay(5000);
        }
  }
        display.display();
        delay(5000);
        display.clearDisplay();
        display.drawPixel(84, 48, WHITE);//return a index
} 


   
    


