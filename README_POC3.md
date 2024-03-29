# Робот за създаване на hyperlapse/timelapse видеоклипове

## Описание:
> Електрически мобилен робот с отдалечено управление за създаване на "hyperlapse" видеоклипове с помощта на RaspberryPi компютър. Улеснява процеса на заснемането на така наречената "hyperlapse/time-lapse" фотография, като между отделните снимки се извършва прецизно преместване на позицията на робота, за да се придаде дълбочинен ефект на крайния видеоклип.
<sub>[Виж какво е hyperlapse тук.](https://youtu.be/y_4p6_KsqoE)</sub>

<table>
      <tr>
        <th>Софтуерна архитектура</th>
      </tr>
      <tr>
        <td>
          <img alt="Software Architecture" src="https://user-images.githubusercontent.com/61236255/214010093-9ca15fc7-d8e1-471a-8f83-3d7fa9c7d574.png">
        </td>
      </tr>
      </table>
    
    
>#### Frontend:</br>
>   - Мобилно приложение, което позволява на потребителя да създава и изпраща инструкции за движение към робота по интернет връзка.
>#### Hyperlapse Robot:</br>
>   - Spring Boot back-end - Сървърна програма, приемаща инструкции и изпращата ги към Control Board
>   - Control Board - Arduino микрокомпютър, който приема инструкции и контролира въртенето на стъпковите мотори
>   - Step Motors - стъпкови мотори, чрез които се извършва движението на робота и насочването на поставката за камера

## Главни функционалности на робота
1. Движение (напред, назад, с завой) по зададео разстояние и време за изпълнение.
2. Насочване на поставката за камера по зададени градусни мярки и време за изпълнение.
3. Мобилно приложение за управление, предназначено да се свързва с робота по WI-FI връзка.
   - бързо и лесно създаване на инструкции за движение
   - възможност за създаване на множество инструкции, които да се изпратят и изпълнят от робота
   - удобни за потребителя менюта за въвеждане на числови данни (растояние, време, градусни мярки).
4. Автоматично включване на робота, когато той се захранва от батерия/power bank

## Диаграми и изображения:
1. Хардуерна диаграма на архитектурата
    <table>
      <tr>
        <th>Hardware Wire диаграма</th>
      </tr>
      <tr>
        <td>
          <img alt="Hardware Wire Architecture" src="https://user-images.githubusercontent.com/61236255/205662820-f8bea474-e4ec-4c0f-8cb2-7d6e48166b0e.png">
        </td>
      </tr>
    </table>

2. DEMO, изработка по време на POC 2:
    <table>
          <th>
            <td>
                <img alt="photo of the robot" src="https://user-images.githubusercontent.com/61236255/214062435-a7b5c700-b1d1-4f08-a52a-a3f4095b711a.jpg">
            </td>
            <td>
                <img alt="components inside the robot" src="https://user-images.githubusercontent.com/61236255/205867284-47fdb931-df37-4cae-8039-24e42011bc98.jpg">
            </td>
          </th>
    </table>

3. Изглед на мобилното приложение
      <table>
          <tr>
            <td>
                <img alt="main window" src="https://user-images.githubusercontent.com/61236255/214032898-a0754529-66d9-4643-876c-6e3a7478e142.jpg">
            </td>
            <td>
                <img alt="connection window" src="https://user-images.githubusercontent.com/61236255/214032952-8b94aab9-e678-4145-b6e7-caa7d1e85064.jpg">
            </td>
            <td>
                <img alt="create rule window" src="https://user-images.githubusercontent.com/61236255/214032955-65326957-9101-432f-9422-05560bbb16c1.jpg">
            </td>
          </tr>
          <tr>
            <td>
                <img alt="create rule window" src="https://user-images.githubusercontent.com/61236255/214032956-7ed0fe67-89b1-4d99-8cf2-6ec9afb0c969.jpg">
            </td>
            <td>
                <img alt="create rule window" src="https://user-images.githubusercontent.com/61236255/214032958-971bf802-83bd-47a6-8ee0-cf19ace4ccf1.jpg">
            </td>
            <td>
                <img alt="main window" src="https://user-images.githubusercontent.com/61236255/214032949-ac855bd1-8f17-471e-8fbc-767e511cc21a.jpg">
            </td>
          </tr>
      </table>

4. Изработка на 3D-модел на робота (3D принтер):
   - Вариант 1
   ![Hyperlapse-Robot](https://user-images.githubusercontent.com/61236255/214060449-4cda8069-f7af-4cdc-a593-bddf4842a221.jpg)
   - Подобрена версия на вариант 1
   ![Hyperlapse-robot-render-v2-jpeg](https://user-images.githubusercontent.com/61236255/214297289-07ae3a4a-8328-485e-b00d-08072f7038a1.jpg)

   
## Използвани технологии
- IntelliJ IDEA Community Edition
  - Разработка, чрез [Java Spring Boot](https://spring.io/projects/spring-boot).
  - библиотека [pi4j](https://mvnrepository.com/artifact/com.pi4j/pi4j-core), позволява изпращане/получаване на данни по I2C протокол.
- Arduino IDE
  - използвани библиотеки:
    - [Wire](https://www.arduino.cc/reference/en/language/functions/communication/wire/), позволява изпращане/получаване на данни по I2C протокол.
    - [AccelStepper](https://www.airspayce.com/mikem/arduino/AccelStepper/), позволява контролирането на стъпкови мотори.
    - [ArduinoJson](https://arduinojson.org/), позволява работа с JSON тип.
- Android Studio - софтуер за разработка на мобилни приложения. 
- Blender - софтуер за 3D моделиране.




