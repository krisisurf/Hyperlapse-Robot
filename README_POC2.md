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


## Главни функционалности на POC №1:
>- `Java Spring Boot back-end` софтуер, предназначен за Raspberry компютър
>  - HTTP крайни точки позволяващи контрол върху робота
>    - Изпращане на `правила за движение` до робота
>    - Получаване на хардуерна информация (диаметър на колелата и параметри на стъпковите мотори)
>    - Изключване на Java приложението
>  - Изпращане на данни до Arduino, чрез I2C протокол.
>  - Създаване на `правила за движение`, съобразени с физическите възможности на робота. (Runtime Exception Handling)
>- Arduino
>  - Получаване на `правила за движение` под формата на JSON string.
>  - Изпълнение на получениете `правила за движение` в последователен ред.

## Главни функционалности на POC №2:
>### BACKEND - компоненти намиращи се под капака на робота:
>1. Автоматично стартиране на програмата при включването на робота с предварително зададени стойности за радиус на гумата и разстоянието между задвижващите колела.
>
>### FRONTEND - мобилно приложение:
>1. Страница за създаване на правило/инструкция.
>    - удобни за потребителя менюта за въвеждане на числови данни (растояние, време, градусни мярки).
>2. Страница за свързване към робота, посредством IP-адрес.
>   - валидиране на връзката
>   - екран за зареждане, докато връзката се тества
>   - получаване на конфигурационни данни (радиус на гумата и дължина задвижващата ос), след установяване на връзката
>3. Проверка дали връзката е установена на всеки 5 секунди (още наричано - Heartbeat).
>4. Списък на всички създадени правила в началната страница на мобилното приложение.
>   - всяко правило има опция за:
>     - редактиране
>     - дублиране/копиране
>     - изтриване

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

2. DEMO, изработка по време на POC:
    <table>
          <th>
            <td>
                <img alt="Android App Concept" src="https://user-images.githubusercontent.com/61236255/205867218-a4a9945c-3b5f-4544-ac0c-73f1c9a0384a.jpg">
            </td>
            <td>
                <img alt="Component Diagram" src="https://user-images.githubusercontent.com/61236255/205867284-47fdb931-df37-4cae-8039-24e42011bc98.jpg">
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
   ![image](https://user-images.githubusercontent.com/61236255/205667209-8b177f33-c008-4231-b9d3-cafc775e5091.png)
   
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




