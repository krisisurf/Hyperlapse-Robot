// Include the AccelStepper library:
#include <AccelStepper.h> // library by Patrick Wasp
#include <MultiStepper.h> // library by Patrick Wasp
#include <ArduinoJson.h> // library by Benoit Blanchon
#include <Wire.h>

#define BUZZER_PIN 9 // buzzer to arduino pin 9
const int BUZZER_TIME_MILLIS = 100; // How long will the buzzer make sound after every rule load

// Define the AccelStepper interface type; 4 wire motor in half step mode:
#define MotorInterfaceType 8

// Initialize with pin sequence IN1-IN3-IN2-IN4 for using the AccelStepper library with 28BYJ-48 stepper motor:
AccelStepper stepperLeft = AccelStepper(MotorInterfaceType, 44, 40, 42, 38);  // IMPORTANT!!! THE PINS FOR THE LEFT SIDE MOTOR ARE FLIPPED. This will lead the left side motor to rotate in the opposite direction to the right side motor
AccelStepper stepperRight = AccelStepper(MotorInterfaceType, 28, 32, 30, 34);
AccelStepper stepperPan = AccelStepper(MotorInterfaceType, 35, 39, 37, 41);
AccelStepper stepperTilt = AccelStepper(MotorInterfaceType, 47, 51, 49, 53);



// Define a StepMotor structure to help organizing the motors
struct StepMotor{
  String name;                      // name of the stepper motor as defined in the JSON rule document
  AccelStepper &stepper;            // Instance of the stepper
};

// Initialize a stepper motors array
StepMotor steppers[4] = {
    {"lm", stepperLeft},
    {"rm", stepperRight},
    {"pm", stepperPan},
    {"tm", stepperTilt}
  };

// The number of steps for a 360 degrees rotation in the AccelStepper library.
// NOTE: Values for step motor with different number of steps per revolution should be mapped/scaled to this
const int LIB_STEPS_PER_REVOLUTION = 4096;

String receivedStringFromMaster = "";
bool isFullyReceivedFromMaster = false;
bool readRequest = false;

void setup() {
  pinMode(BUZZER_PIN, OUTPUT);
  
  // Set the maximum steps per second:
  stepperLeft.setMaxSpeed(1024);
  stepperLeft.setMinPulseWidth(80000);

  stepperRight.setMaxSpeed(1024);
  stepperRight.setMinPulseWidth(80000);

  stepperPan.setMaxSpeed(1024);
  stepperPan.setMinPulseWidth(80000);

  stepperTilt.setMaxSpeed(1024);
  stepperTilt.setMinPulseWidth(80000);
  
  // setStepMotorsOutputs(false);  // Disable motors pinouts by default to save power.
  
  Wire.begin(8);                // join I2C bus with address #8
  Wire.onReceive(receiveEvent); // register event

  // Unmark this to get program logs. NOTE THAT THIS SLOWS THE PROGRAM and also the BUZZER will not work properly
  // Serial.begin(9600);
}

void loop() {
  static DynamicJsonDocument rules(2000);    // Json object that will contain the movement rules
  static int currentRuleIndex;
  static int rulesCount;
  static double wheelRadius = 5.0; // Default value of the wheel radius is 5 cm. It will be changed when a rule from the backend is received.
  static bool rulesFinished;

  static int lastTimeLoopedBuzzer = millis(); // last time looped millisecond
  static int buzzerTimeLeft; // How much time left to stop the buzzer from producing noise

  int currentTime = millis();
  // UPDATE BUZZER
  if(buzzerTimeLeft > 0){
    int timePassed = currentTime - lastTimeLoopedBuzzer;
    buzzerTimeLeft -= timePassed;
    tone(BUZZER_PIN, 300);
  } else {
    noTone(BUZZER_PIN);
  }
  lastTimeLoopedBuzzer = currentTime;
  // End of UPDATE BUZZER

  if(readRequest){
    readAndAnswerRequestFromMaster();
    readRequest = false;
  }
  
  // Waits for JSON format with movement rules to be inputted to the Serial
  if(isFullyReceivedFromMaster){
    Serial.println("New rules received");
    rules.clear();
    deserializeJSON(rules, receivedStringFromMaster);
    receivedStringFromMaster = "";
    isFullyReceivedFromMaster = false;
    
    resetMotors();
    rulesCount = rules["rc"];
    wheelRadius = rules["wr"];
    currentRuleIndex = 0;
    rulesFinished = false;
    
    // setStepMotorsOutputs(true);
  }

  // Does not move the motors, if there are no movement rules or they have been already finished
  if(rulesCount == 0 || rulesFinished)
    return;

  // Run motors  
  bool hasFinished = runMotorsToPosition();

  // Stops there if the motors have not reached their target positions
  if(!hasFinished)
    return;
  
  if(currentRuleIndex < rulesCount){
    buzzerTimeLeft = BUZZER_TIME_MILLIS; // Makes a buzzer noise
    
    // Loads the next rule from the Json Document
    loadRule(rules, currentRuleIndex, wheelRadius);
    currentRuleIndex++;
  }else{
    rulesFinished = true;
    // All rules have been finished, so we disable motor outputs to save power
    // setStepMotorsOutputs(false);
  }
}

/*
  Rotates every step motor, which has not yet reached its target position.
  Returns:
    * true   -> if all the motors have reached their target position and the total execution time of the rule has passed
    * false  -> if one or more step motors have not yet reached their target position or the total execution time of the rule has not passed
*/
bool runMotorsToPosition(){
  int steppersFinished = 0;
  static int lastTime = millis();
  
  for(StepMotor stepMotor : steppers){
    AccelStepper &as = stepMotor.stepper;
            
    as.setSpeed(as.speed());
    as.runSpeedToPosition();

    if(as.distanceToGo() == 0)
      steppersFinished++;
  }
  
  return steppersFinished == 4;
}


// deserializes json string
void deserializeJSON(DynamicJsonDocument& rules, String json){
  // Deserialize the JSON document
  DeserializationError error = deserializeJson(rules, json);

  // Test if parsing succeeds.
  if (error) {
    Serial.print(F("deserializeJson() failed: "));
    Serial.println(error.f_str());
    return;
  }
}


/*
  Loads a rule from json document and sets the motors target position and speed and etc.
  Parameters:
  * rules             -> json document with structure for the rules
  * ruleIndexToLoad   -> index of the rule from the rules document, which will be loaded
*/
void loadRule(DynamicJsonDocument& rules, const int ruleIndexToLoad, const double wheelRadius){
    Serial.print("Initializing rule No.");Serial.println(ruleIndexToLoad);
    
    // Sets left and right side step motors
    for(int i = 0; i < 2; i++){
      StepMotor stepMotor = steppers[i];
      double distance = rules["r"][ruleIndexToLoad][stepMotor.name]["ds"];
      double executionTime = rules["r"][ruleIndexToLoad][stepMotor.name]["t"];
      
      int steps = convertCentimetersToSteps(distance, wheelRadius, LIB_STEPS_PER_REVOLUTION);
      double speed = convertStepsAndExecutionTimeToSpeed(steps, executionTime);

      stepMotor.stepper.move(steps);
      stepMotor.stepper.setSpeed(speed);

      // Displays received and calculated data
      Serial.print("Motor: ");Serial.println(stepMotor.name);
      Serial.print("Distance: ");Serial.println(distance);
      Serial.print("Execution time: ");Serial.println(executionTime);
      Serial.print("Steps: ");Serial.println(steps);
      Serial.print("Speed: ");Serial.println(speed);
      Serial.print("Motor target position: ");Serial.println( stepMotor.stepper.targetPosition());
      Serial.println();
    }

    // Sets pan and tilt step motors
    for(int i = 0; i < 2; i++){
      StepMotor stepMotor = steppers[i + 2];
      double degrees = rules["r"][ruleIndexToLoad][stepMotor.name]["dg"];
      double executionTime = rules["r"][ruleIndexToLoad][stepMotor.name]["t"];
        
      int steps = convertDegreesToSteps(degrees, LIB_STEPS_PER_REVOLUTION);
      double speed = convertStepsAndExecutionTimeToSpeed(steps, executionTime);

      stepMotor.stepper.move(steps);
      stepMotor.stepper.setSpeed(speed);

      // Displays received and calculated data
      Serial.print("Motor: ");Serial.println(stepMotor.name);
      Serial.print("Degrees: ");Serial.println(degrees);
      Serial.print("Execution time: ");Serial.println(executionTime);
      Serial.print("Steps: ");Serial.println(steps);
      Serial.print("Speed: ");Serial.println(speed);
      Serial.print("Motor target position: ");Serial.println( stepMotor.stepper.targetPosition());
      Serial.println();
    }
}

/*
  Removes target positions of all the motors.
*/
void resetMotors(){
   for(int i = 0; i < 4; i++){
      StepMotor stepMotor = steppers[i];
      stepMotor.stepper.setCurrentPosition(0);
      stepMotor.stepper.moveTo(0);
   }
}

/* 
  Function that executes whenever data is received from master (in our case master is the Raspberry Pi).
  This function is registered as an event, see setup()
*/
void receiveEvent(int howMany) {
  static const char STOP_RULES_SIGNAL = '$'; // When arduino receive this character, it will load the received json document with rules
  static const char STOP_REQUEST_SIGNAL = '^'; // When arduino receive this character, it will load the received json document with rules
  isFullyReceivedFromMaster = false;
  
  while (Wire.available()) { // loop through all but the last
    char c = Wire.read(); // receive byte as a character
    if(c == STOP_RULES_SIGNAL){
      isFullyReceivedFromMaster = true;
      Serial.println("Received data from master:");
      Serial.println(receivedStringFromMaster);
      return;
    }else if(c == STOP_REQUEST_SIGNAL){
      readRequest = true;
      return;
    }
    
    receivedStringFromMaster.concat(c);
  }
}

void readAndAnswerRequestFromMaster(){
  static DynamicJsonDocument requests(200);
  
  requests.clear();
  deserializeJSON(requests, receivedStringFromMaster);

  Serial.print("Received request from master: ");
  Serial.println(receivedStringFromMaster);
  receivedStringFromMaster = "";

  String requestData = requests["rd"];
  Serial.print("RequestData: ");
  Serial.println(requestData);
  if(requestData.equals("ROBOT_HARDWARE_PROPERTIES")){
    Serial.println("Trying to send 'testing' message to master");
    Wire.write('b');
    Serial.println("Data should have been sent???");
  }
}

/*
  Enables or disables pinout outputs for every step motor in the array 'steppers'
  Parameters:
  * enabledOutputs  -> value of 'true' will enable outputs and value 'false' will disable outputs to save power
*/
void setStepMotorsOutputs(bool enabledOutputs){
  if(enabledOutputs)
    for(StepMotor stepMotor : steppers)
      stepMotor.stepper.enableOutputs();
  else
    for(StepMotor stepMotor : steppers)
      stepMotor.stepper.disableOutputs();
}

/*
    Converts distance to number of steps (step motor), that it takes for a wheel with radius to travel the given distance.
    Parameters:
    * centimeters               -> distance in centimeters
    * wheelRadiusInCentimeters  -> Radius of the rotational wheel placed on the stepper motor
    * stepsPerRevolution        -> The number of steps that the motor makes for one revolution (360 degrees)

    Returns:
    * The number of steps that it takes to travel the given distance
*/
int convertCentimetersToSteps(double centimeters, double wheelRadiusInCentimeters, const int stepsPerRevolution){
  double pi = 3.141592;  
  double wheelPerimeter = 2 * pi * wheelRadiusInCentimeters;
  
  double revolutionsToMake = centimeters / wheelPerimeter;
  
  int steps = stepsPerRevolution * revolutionsToMake;
  return steps;  
}

/*
    Calculates the time in seconds that will take to rotate a step motor for a certain number of steps.
    Parameters:
    * steps -> number of steps
    * speed -> steps per second, rotational speed of the step motor.
    Returns:
    * time in seconds
*/
double convertStepsToSeconds(int steps, double speed){
  double timeToCompleteInSeconds = 1.0 * steps / speed;
  return timeToCompleteInSeconds;
}

/*
    Calculates the speed at which a motor must move to a certain number of steps in a for a given time.
    Parameters:
    * steps          -> number of steps
    * executionTime  -> the time for rotating the given number of steps
    Returns:
    * speed in steps per second
*/
double convertStepsAndExecutionTimeToSpeed(int steps, double executionTime){
  double speed = 1.0 * steps / executionTime;
  return speed;  
}

/*
    Converts degrees to number of steps (step motor), that it takes for a motor to move to the given number of degrees.
    Parameters:
    * degrees            -> degrees from current position
    * stepsPerRevolution -> The number of steps that the motor makes for one revolution (360 degrees)
    Returns:
    * The number of steps that it takes to move the given number of degrees
 */
int convertDegreesToSteps(double degrees, const int stepsPerRevolution) {
    return (int) (stepsPerRevolution * degrees / 360);
}
