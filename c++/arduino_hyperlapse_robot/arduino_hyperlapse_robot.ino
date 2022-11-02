// Include the AccelStepper library:
#include <AccelStepper.h>
#include <MultiStepper.h>
#include <ArduinoJson.h>

// Motor pin definitions
// Left side step motor:
#define motorLeftPin1  22     // IN1 on the ULN2003 driver
#define motorLeftPin2  23     // IN2 on the ULN2003 driver
#define motorLeftPin3  24     // IN3 on the ULN2003 driver
#define motorLeftPin4  25     // IN4 on the ULN2003 driver
// Right side step motor:
#define motorRightPin1  26     // IN1 on the ULN2003 driver
#define motorRightPin2  27     // IN2 on the ULN2003 driver
#define motorRightPin3  28     // IN3 on the ULN2003 driver
#define motorRightPin4  29     // IN4 on the ULN2003 driver
// Pan step motor:
#define motorPanPin1  44     // IN1 on the ULN2003 driver
#define motorPanPin2  45     // IN2 on the ULN2003 driver
#define motorPanPin3  46     // IN3 on the ULN2003 driver
#define motorPanPin4  47     // IN4 on the ULN2003 driver
// Tilt step motor:
#define motorTiltPin1  48     // IN1 on the ULN2003 driver
#define motorTiltPin2  49     // IN2 on the ULN2003 driver
#define motorTiltPin3  50     // IN3 on the ULN2003 driver
#define motorTiltPin4  51     // IN4 on the ULN2003 driver

// Define the AccelStepper interface type; 4 wire motor in half step mode:
#define MotorInterfaceType 8

// Initialize with pin sequence IN1-IN3-IN2-IN4 for using the AccelStepper library with 28BYJ-48 stepper motor:
AccelStepper stepperLeft = AccelStepper(MotorInterfaceType, motorLeftPin1, motorLeftPin3, motorLeftPin2, motorLeftPin4);
AccelStepper stepperRight = AccelStepper(MotorInterfaceType, motorRightPin1, motorRightPin3, motorRightPin2, motorRightPin4);
AccelStepper stepperPan = AccelStepper(MotorInterfaceType, motorPanPin1, motorPanPin3, motorPanPin2, motorPanPin4);
AccelStepper stepperTilt = AccelStepper(MotorInterfaceType, motorTiltPin1, motorTiltPin3, motorTiltPin2, motorTiltPin4);


// Define a StepMotor structure to help organizing the motors
struct StepMotor{
  int id;                           // In this program the id is an array index
  String name;                      // A way in which we could conversationally name the specific motor
  AccelStepper &stepper;            // Instance of the stepper
  double speed;                     // Run speed of the motor
  const int STEPS_PER_REVOLUTION;   // How much steps does the motor works with
};

// Initialize a stepper motors array
StepMotor steppers[4] = {
    {0, "motorLeft", stepperLeft, 500, 64},
    {1, "motorRight", stepperRight, 500, 64},
    {2, "Motor Pan", stepperPan, 500, 64},
    {3, "Motor Tilt", stepperTilt, 500, 64}
  };

// The number of steps for a 360 degrees rotation in the AccelStepper library.
// NOTE: Values for step motor with different number of steps per revolution should be mapped/scaled to this
const int LIB_STEPS_PER_REVOLUTION = 4096;

// Logic
StaticJsonDocument<500> rules;
int currentRuleIndex = 0;
int rulesCount = 0;

void setup() {
  // Set the maximum steps per second:
  stepperLeft.setMaxSpeed(1024);
  stepperLeft.setMinPulseWidth(80000);
  stepperLeft.setPinsInverted(true, true, false, false, false);   // This will lead the left side motor to rotate in the opposite direction to the right side motor

  stepperRight.setMaxSpeed(1024);
  stepperRight.setMinPulseWidth(80000);

  stepperPan.setMaxSpeed(1024);
  stepperPan.setMinPulseWidth(80000);

  stepperTilt.setMaxSpeed(1024);
  stepperTilt.setMinPulseWidth(80000);
  
  Serial.begin(9600);
}

void loop() {
  if(Serial.available()){
    String json = Serial.readString();
    deserializeJSON(json);
  }

  // Rotates the motor to keyboard inputted targetPosition in steps
  // if(Serial.available()){
  //   long steps = atol(Serial.readString().c_str());
  //   Serial.print("Target position in steps has been set to: ");
  //   Serial.println(steps);

  //   // That will rotate the step motor to the given number of steps and speed of 1024 steps per second
  //   // This process will take: [ time_to_complete_in_seconds = steps/speed ]
  //   rotateStepper(stepperRight, steps, 64, 1024);
  // }

  if(rulesCount > 0)
    motorsHandler();
}

void motorsHandler(){
  int steppersFinished = 0;
  
  for(StepMotor stepMotor : steppers){
    AccelStepper &as = stepMotor.stepper;
            
    as.setSpeed(as.speed());
    as.runSpeedToPosition();

    if(as.distanceToGo() == 0){
      steppersFinished++;
    }
  }

  // Stops executing rules after all of them finish
  if(currentRuleIndex >= rulesCount)
    rulesCount = 0;

  // Checks if all the steppers has finished their work for the current rule ()
  if(steppersFinished == 4){
    currentRuleIndex++;
    Serial.print("Initializing rule No.");Serial.println(currentRuleIndex);
    
    // Sets left and right side step motors
    for(int i = 0; i < 2; i++){
      StepMotor stepMotor = steppers[i];
      double distance = rules["robotRules"][currentRuleIndex][stepMotor.name]["distance"];
      double executionTime = rules["robotRules"][currentRuleIndex][stepMotor.name]["executionTime"];
      
      int steps = convertCentimetersToSteps(distance, 5.0, 4096);
      int speed = convertStepsAndCompletionTimeToSpeed(steps, executionTime);

      stepMotor.stepper.move(steps);
      stepMotor.stepper.setSpeed(speed);   

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
      double degrees = rules["robotRules"][currentRuleIndex][stepMotor.name]["degrees"];
      double executionTime = rules["robotRules"][currentRuleIndex][stepMotor.name]["executionTime"];

      // Not finished !!!
      //stepMotor.stepper.move(steps);
      //stepMotor.stepper.setSpeed(rules["robotRules"]["leftMotor"]["speed"]);      
    }
  }
}

// deserializes json string and saves the data in the global variable 'rules'
void deserializeJSON(String json){
  // Deserialize the JSON document
  DeserializationError error = deserializeJson(rules, json);

  // Test if parsing succeeds.
  if (error) {
    Serial.print(F("deserializeJson() failed: "));
    Serial.println(error.f_str());
    return;
  }
  
  rulesCount = rules["rulesCount"];
  currentRuleIndex = 0;
  Serial.print("Json received. Rules count: ");Serial.println(rulesCount);
}




/*
    Rotate Stepper

    Runs the motor with a given parameter 'speed' (steps/second), until the motor reaches the number of given parameter 'steps'
    Parameters:
    * steps              -> The number of steps that the step motor will make.
    * stepsPerRevolution -> The hardware given number of steps that the step motor makes per one revolution.
    * speed              -> Rotating speed counted in steps per second.
    
    This process will take: [ time_to_complete_in_seconds = steps/speed ]
*/
void rotateStepper(AccelStepper stepper, long steps, const int stepsPerRevolution, double speed){
  // Does not make anything when the speed is 0
  if(speed == 0)
    return;
    
  speed = abs(speed);
  
  // Calculating directional speed:
  // * negative value -> clockwise rotation
  // * positive value -> counter-clockwise direction
  double directionalSpeed = speed;
  if(steps < 0)
    directionalSpeed = -speed;
    
  // Maps the given params steps and stepsPerRevolution to the AccelStepper.h library steps per revolution, which is 4096 by default.
  long libSteps = map(steps, -stepsPerRevolution, stepsPerRevolution, -LIB_STEPS_PER_REVOLUTION, LIB_STEPS_PER_REVOLUTION);
  
  double timeToCompleteInSeconds = convertStepsToSeconds(abs(libSteps), speed);

  // Prints a log to the console ----------------
  String message = String("Moving stepper ");
  if(speed < 0)
    message.concat("counter-");
  message.concat("clockwise for ( ");
  message.concat(steps);
  message.concat(" ) steps with speed of ( ");
  message.concat(speed);
  message.concat(" ) steps per second. The process will take: ");
  message.concat(timeToCompleteInSeconds);
  message.concat(" seconds.");
  Serial.println(message);
  //---------------------------------------------

  stepper.setCurrentPosition(0);

  // Run the motor until it reach the given number of steps
  while (stepper.currentPosition() != libSteps) {
    stepper.setSpeed(directionalSpeed);
    stepper.runSpeed();
  }
  
  Serial.print("Target position ( ");
  Serial.print(steps);
  Serial.println(" ) in steps reached.");
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
