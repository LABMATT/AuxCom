// Read acs712 30A

#define ascPIN A0
#define adcBIT 1024.0
float ascOffsetAuto = 2.5;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);

  calibrateOffset();
  Serial.print(ascOffsetAuto, 6);
  Serial.println("offset");

  delay(4000);
}

void loop() {
  // put your main code here, to run repeatedly:

  float temp = getCurrent();

  delay(500);
}



float getCurrent() {
  // Gets the current flowing though our ASC712

// Sample the analog out 200 times. Then get the average.
long readings = 0;
for(int i = 0; i < 200; i++) {

  int currentSample = analogRead(ascPIN);
  readings = readings + currentSample;

  delay(1);
}

float average = readings/200;


//For a ASC712 30A moduel, When the moduel is reading 0A the voltage output is half its supply voltage.
// IF mesured current is 0A. Supply voltage is 5v. Then the output will be 2.5v (Half of the logic supply voltage).
// 5/adc resultion gives will allow us to know what sample eqyates to what Millivoltage.
// Multipling our analogRead value gives us the Millivolts at that pin.
// As mentioned at 0A the asc712 output is 2.5v Thus subtracting this from our millivolt reading leaves us with the usable postive amprage range.
// For every 1A of current, the asc712 output rises buy 0.066mv. By using our millivolts value we divivde by that value to work out the current.

float millivolt = (5.0/adcBIT)*average;
float millivoltPostOffset = millivolt - ascOffsetAuto;
float current = millivoltPostOffset/0.066;

  Serial.print(millivolt,6);
  Serial.println("mv");
  Serial.print(millivoltPostOffset,6);
  Serial.println("mv/OS");
  Serial.print(current,6);
  Serial.println("A");
  Serial.println(" ");

return millivolt;
}



void calibrateOffset() {
  // MAKE SURE NO CURRENT IS FLOWING WHEN CALLING THIS!
  // Calibrate Offset will get the voltage that the asc712 is outputing with no current flowing.
  // As the asc712 sits at 2.5 ish volts, at 0A flowing. we need to subtract this from any reading.

  // Sample the analog out 200 times. Then get the average.
long readings = 0;
for(int i = 0; i < 200; i++) {

  int currentSample = analogRead(ascPIN);
  readings = readings + currentSample;

  delay(3);
}

float average = readings/200;

// 5v output divied but the ADC resulution gives us our Milivolt output per sample of the adc.
// Milivolt output is then multipled by what we read in the analogpin.
ascOffsetAuto = (5.0/adcBIT)*average;
}