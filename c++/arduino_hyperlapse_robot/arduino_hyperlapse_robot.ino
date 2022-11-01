// Include the AccelStepper library:
#include <AccelStepper.h>
#include <MultiStepper.h>

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
// Horizontal step motor:
#define motorHorizontalPin1  44     // IN1 on the ULN2003 driver
#define motorHorizontalPin2  45     // IN2 on the ULN2003 driver
#define motorHorizontalPin3  46     // IN3 on the ULN2003 driver
#define motorHorizontalPin4  47     // IN4 on the ULN2003 driver

// Define the AccelStepper interface type; 4 wire motor in half step mode:
#define MotorInterfaceType 8

// Initialize with pin sequence IN1-IN3-IN2-IN4 for using the AccelStepper library with 28BYJ-48 stepper motor:
AccelStepper stepperLeft = AccelStepper(MotorInterfaceType, motorLeftPin1, motorLeftPin3, motorLeftPin2, motorLeftPin4);
AccelStepper stepperRight = AccelStepper(MotorInterfaceType, motorRightPin1, motorRightPin3, motorRightPin2, motorRightPin4);
AccelStepper stepperHorizontal = AccelStepper(MotorInterfaceType, motorHorizontalPin1, motorHorizontalPin3, motorHorizontalPin2, motorHorizontalPin4);


// Define a StepMotor structure to help organizing the motors
struct StepMotor{
  int id;                           // In this program the id is an array index
  String name;                      // A way in which we could conversationally name the specific motor
  AccelStepper &stepper;            // Instance of the stepper
  const int STEPS_PER_REVOLUTION;   // How much steps does the motor works with
};

// Initialize a stepper motors array
StepMotor steppers[3] = {
    {0, "Left Motor", stepperLeft, 64},
    {1, "Right Motor", stepperRight, 64},
    {2, "Horizontal Motor", stepperLeft, 64}
};

// The number of steps for a 360 degrees rotation in the AccelStepper library.
// NOTE: Values for step motor with different number of steps per revolution should be mapped/scaled to this
const int LIB_STEPS_PER_REVOLUTION = 4096;

void setup() {
  // Set the maximum steps per second:
  stepperLeft.setMaxSpeed(1024);
  stepperLeft.setMinPulseWidth(80000);
  stepperLeft.setPinsInverted(true, true, false, false, false);   // This will lead the left side motor to rotate in the opposite direction to the right side motor

  stepperRight.setMaxSpeed(1024);
  stepperRight.setMinPulseWidth(80000);

  stepperHorizontal.setMaxSpeed(1024);
  stepperHorizontal.setMinPulseWidth(80000);
  
  Serial.begin(9600);
}

void loop() {

  // Rotates the motor to keyboard inputted targetPosition in steps
  if(Serial.available()){
    String command = Serial.readString();
    executeCommand(command);    
    
    // long steps = atol(Serial.readString().c_str());
    // Serial.print("Target position in steps has been set to: ");
    // Serial.println(steps);

    // That will rotate the step motor to the given number of steps and speed of 1024 steps per second
    // This process will take: [ time_to_complete_in_seconds = steps/speed ]
    //rotateStepper(stepperLeft, steps, STEPS_PER_REVOLUTION_28BYJ48, 1024);
    switch
  }

  motorsHandler();
}

void motorsHandler(){
  for(StepMotor stepMotor : steppers){
    AccelStepper &as = stepMotor.stepper;
    as.setSpeed(as.speed());
    as.runSpeed();
  }
}

void executeCommand(String command){
    String commandParts[3];
    int stringCount = 0;
    while (input.length() > 0) {
      int index = input.indexOf(' ');
      
      if (index == -1){ // No space found
        commands[stringCount++] = command;
        break;
      } else {
        commands[stringCount++] = input.substring(0, index);
        command = input.substring(index + 1);
      }
    }

    String movingType = command[0];
    int motorId = command[1].toInt();
    int steps = command[2].toInt();
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
  
  double timeToCompleteInSeconds = stepsToSeconds(abs(libSteps), speed);

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
*/
double stepsToSeconds(int steps, double speed){
  double timeToCompleteInSeconds = 1.0 * steps / speed;
  return timeToCompleteInSeconds;
}
