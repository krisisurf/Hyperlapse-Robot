# Java Spring Application for Raspberry Pi

The Graphical User Interface of the project, which manages services to control the Arduino by sending JSON string with rules/commands to it.
The connection is one-directional (`RaspberryPI -> Arduino`) and it is made via `I2C protocol`.

## Setup Instructions
1. Open `java/Hyperlapse-Robot` project with IntelliJ or other Java IDE.
2. Run Maven command:
          ```
          mvn clean install
          ```
3. After the build is ready, copy the exported jar file `./target/Hyperlapse-Robot-0.0.1-SNAPSHOT.jar` to the Raspberry Pi
4. Enable I2C on the Raspberry Pi. You can see how [here](https://ozzmaker.com/i2c/).
5. Wire `RaspberryPi Zero 2W` with the `Arduino MEGA 2560` using pinouts.<table>
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
            <td>SDA (D20)</td>
          </tr>
          <tr>
            <td>GPIO 3 Serial Clock (I2C)</td>
            <td>SCL (D21)</td>
          </tr>
    </table><p><span class="small">Optional for Arduino MEGA 2560: Digital IO Pins 20 and 21 can be configured as SDA (20) and SCL (21) to support I2C or I2C or Two Wire Interface (TWI) communication.</span></p>
6. Power on the Arduino and Raspberry Pi.
7. Run the java application on the Raspberry Pi. Navigate to the location of the jar file and run the command:
          ```
          sudo java -jar Hyperlapse-Robot-0.0.1-SNAPSHOT.jar
          ```
8. You are ready to open the GUI interface in the browser.
