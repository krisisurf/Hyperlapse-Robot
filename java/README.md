# Java Spring Application for Raspberry Pi

The Graphical User Interface of the project, which manages services to control the Arduino by sending JSON string with rules/commands to it.
The connection is one-directional (`RaspberryPI -> Arduino`) and it is made via `I2C protocol`.

## Setup Instructions
1. Open java/Hyperlapse-Robot project with IntelliJ or other Java IDE.
2. Run Maven command:
          ```
          mvn clean install
          ```
3. After the build is ready, copy the exported jar file `./target/Hyperlapse-Robot-0.0.1-SNAPSHOT.jar` to the Raspberry Pi
4. Enable I2C on the Raspberry Pi. You can see how [here](https://ozzmaker.com/i2c/).
5. Wire Raspberry Pi with the Arduino using pinouts.<table>
          <tr>
            <th>Raspberry Pi</th>
            <th>Arduino</th>
          </tr>
          <tr>
            <td>Ground</td>
            <td>Ground</td>
          </tr>
          <tr>
            <td>GPIO 2 Serial Data (I2C)</td>
            <td>SDA (PWM)</td>
          </tr>
          <tr>
            <td>GPIO 3 Serial Clock (I2C)</td>
            <td>SCL (PWM)</td>
          </tr>
    </table>
6. Power on the Arduino and Raspberry Pi.
7. Run the java application on the Raspberry Pi. Navigate to the location of the jar file and run the command:
          ```
          sudo java -jar Hyperlapse-Robot-0.0.1-SNAPSHOT.jar
          ```
8. You are ready to open the GUI interface in the browser.
