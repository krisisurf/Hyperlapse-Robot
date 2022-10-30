/* Sketch to control a stepper motor with ULN2003 driver board with AccelStepper library and Arduino UNO: number of steps/revolutions. More info: https://www.makerguides.com */

// Include the AccelStepper library:
#include <AccelStepper.h>
#include <MultiStepper.h>

// Motor pin definitions
// Right side step motor:
#define motorRightPin1  22     // IN1 on the ULN2003 driver
#define motorRightPin2  23     // IN2 on the ULN2003 driver
#define motorRightPin3  24     // IN3 on the ULN2003 driver
#define motorRightPin4  25     // IN4 on the ULN2003 driver
// Left side step motor:
#define motorLeftPin1  26     // IN1 on the ULN2003 driver
#define motorLeftPin2  27     // IN2 on the ULN2003 driver
#define motorLeftPin3  28     // IN3 on the ULN2003 driver
#define motorLeftPin4  29     // IN4 on the ULN2003 driver

// Define the AccelStepper interface type; 4 wire motor in half step mode:
#define MotorInterfaceType 8

// Initialize with pin sequence IN1-IN3-IN2-IN4 for using the AccelStepper library with 28BYJ-48 stepper motor:
AccelStepper stepperRight = AccelStepper(MotorInterfaceType, motorRightPin1, motorRightPin3, motorRightPin2, motorRightPin4);
// By reordering the sequence of left side motor pins, we can flip the rotating direction since the left motor should move opposite to the right motor
AccelStepper stepperLeft = AccelStepper(MotorInterfaceType, motorLeftPin4, motorLeftPin2, motorLeftPin3, motorLeftPin1);

const int STEPS_PER_REVOLUTION_28BYJ48 = 64;
const int LIB_STEPS_PER_REVOLUTION = 4096;

void setup() {
  // Right side step motor
  pinMode(motorRightPin1, OUTPUT);
  pinMode(motorRightPin2, OUTPUT);
  pinMode(motorRightPin3, OUTPUT);
  pinMode(motorRightPin4, OUTPUT);

  // Left side step motor
  pinMode(motorLeftPin1, OUTPUT);
  pinMode(motorLeftPin2, OUTPUT);
  pinMode(motorLeftPin3, OUTPUT);
  pinMode(motorLeftPin4, OUTPUT);

  // Set the maximum steps per second:
  stepperRight.setMaxSpeed(1024);
  stepperRight.setMaxSpeed(1024);

  Serial.begin(9600);
}

void loop() {

  // Rotates the motor to keyboard inputted targetPosition in steps
  if(Serial.available()){
    long steps = atol(Serial.readString().c_str());
    Serial.print("Target position in steps has been set to: ");
    Serial.println(steps);

    // That will rotate the step motor to the given number of steps and speed of 1024 steps per second
    // This process will take: [ time_to_complete_in_seconds = steps/speed ]
    rotateStepper(stepperRight, steps, STEPS_PER_REVOLUTION_28BYJ48, 1024);
  }
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
void rotateStepper(AccelStepper stepper, long steps, const int stepsPerRevolution, int speed){
  // Does not make anything when the speed is 0
  if(speed == 0)
    return;
    
  speed = abs(speed);
  
  // Calculating directional speed:
  // * negative value -> clockwise rotation
  // * positive value -> counter-clockwise direction
  int directionalSpeed = speed;
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

  // Run the motor forward at 500 steps/second until the motor reaches 4096 steps (1 revolution):
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
