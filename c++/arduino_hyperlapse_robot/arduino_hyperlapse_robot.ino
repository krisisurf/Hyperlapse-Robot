// Include the AccelStepper library:
#include <AccelStepper.h>
#include <MultiStepper.h>
#include <ArduinoJson.h>

// Define the AccelStepper interface type; 4 wire motor in half step mode:
#define MotorInterfaceType 8

// Initialize with pin sequence IN1-IN3-IN2-IN4 for using the AccelStepper library with 28BYJ-48 stepper motor:
AccelStepper stepperLeft = AccelStepper(MotorInterfaceType, 25, 23, 24, 22);  // IMPORTANT!!! THE PINS FOR THE LEFT SIDE MOTOR ARE FLIPPED. This will lead the left side motor to rotate in the opposite direction to the right side motor
AccelStepper stepperRight = AccelStepper(MotorInterfaceType, 26, 28, 27, 29);
AccelStepper stepperPan = AccelStepper(MotorInterfaceType, 44, 46, 45, 47);
AccelStepper stepperTilt = AccelStepper(MotorInterfaceType, 48, 50, 49, 51);


// Define a StepMotor structure to help organizing the motors
struct StepMotor{
  String name;                      // name of the stepper motor as defined in the JSON rule document
  AccelStepper &stepper;            // Instance of the stepper
};

// Initialize a stepper motors array
StepMotor steppers[4] = {
    {"motorLeft", stepperLeft},
    {"motorRight", stepperRight},
    {"motorPan", stepperPan},
    {"motorTilt", stepperTilt}
  };

// The number of steps for a 360 degrees rotation in the AccelStepper library.
// NOTE: Values for step motor with different number of steps per revolution should be mapped/scaled to this
const int LIB_STEPS_PER_REVOLUTION = 4096;

void setup() {
  // Set the maximum steps per second:
  stepperLeft.setMaxSpeed(1024);
  stepperLeft.setMinPulseWidth(80000);

  stepperRight.setMaxSpeed(1024);
  stepperRight.setMinPulseWidth(80000);

  stepperPan.setMaxSpeed(1024);
  stepperPan.setMinPulseWidth(80000);

  stepperTilt.setMaxSpeed(1024);
  stepperTilt.setMinPulseWidth(80000);
  
  setStepMotorsOutputs(false);  // Disable motors pinouts by default to save power.
  
  Serial.begin(9600);
}

void loop() {
  static DynamicJsonDocument rules(500);    // Json object that will contain the movement rules
  static int currentRuleIndex;
  static int rulesCount;
  static bool rulesFinished;
  
  // Waits for JSON format with movement rules to be inputted to the Serial
  if(Serial.available()){
    String json = Serial.readString();
    deserializeJSON(rules, json);

    rulesCount = rules["rulesCount"];
    currentRuleIndex = 0;
    rulesFinished = false;
    
    setStepMotorsOutputs(true);
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
    // Loads the next rule from the Json Document
    loadRule(rules, currentRuleIndex);
    currentRuleIndex++;
  }else{
    rulesFinished = true;
    // All rules have been finished, so we disable motor outputs to save power
    setStepMotorsOutputs(false);
  }
}

/*
  Rotates every step motor, which has not yet reached its target position.
  Returns:
    * true   -> if all the motors have reached their target position
    * false  -> if one or more step motors have not yet reached their target position
*/
bool runMotorsToPosition(){
  int steppersFinished = 0;
  
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
bool loadRule(DynamicJsonDocument& rules, int ruleIndexToLoad){
    Serial.print("Initializing rule No.");Serial.println(ruleIndexToLoad);
    
    // Sets left and right side step motors
    for(int i = 0; i < 2; i++){
      StepMotor stepMotor = steppers[i];
      double distance = rules["robotRules"][ruleIndexToLoad][stepMotor.name]["distance"];
      double executionTime = rules["robotRules"][ruleIndexToLoad][stepMotor.name]["executionTime"];
      
      int steps = convertCentimetersToSteps(distance, 5.0, LIB_STEPS_PER_REVOLUTION);
      int speed = convertStepsAndCompletionTimeToSpeed(steps, executionTime);

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
      double degrees = rules["robotRules"][ruleIndexToLoad][stepMotor.name]["degrees"];
      double executionTime = rules["robotRules"][ruleIndexToLoad][stepMotor.name]["executionTime"];

      // Not finished !!!
      //stepMotor.stepper.move(steps);
      //stepMotor.stepper.setSpeed(rules["robotRules"]["leftMotor"]["speed"]);      
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
    * timeToComplete -> the time for creating the given number of steps
    Returns:
    * speed in steps per second
*/
double convertStepsAndCompletionTimeToSpeed(int steps, double timeToComplete){
  double speed = 1.0 * steps / timeToComplete;
  return speed;  
}
