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
          <img alt="Software Architecture" src="https://user-images.githubusercontent.com/61236255/205655081-3bcbe266-a052-41d1-bb01-a1cb441ebf27.png">
        </td>
      </tr>
    </table>

## Главни функционалности (на POC до момента):

- `Java Spring Boot back-end` софтуер, предназначен за Raspberry компютър
  - HTTP крайни точки позволяващи контрол върху робота
    - Изпращане на `правила за движение` до робота
    - Получаване на хардуерна информация (диаметър на колелата и параметри на стъпковите мотори)
    - Изключване на Java приложението
  - Изпращане на данни до Arduino, чрез I2C протокол.
  - Създаване на `правила за движение`, съобразени с физическите възможности на робота. (Runtime Exception Handling)
  - TODO: Получаване на данни от Arduino.
- Arduino
  - Получаване на `правила за движение` под формата на JSON string.
  - Изпълнение на получениете `правила за движение` в последователен ред.
  - TODO: Изпращане на данни за състоянието на робота до `Java Spring Boot back-end`.
- TODO: `Android Application front-end`
  - Прозорец за тестване на връзката към `Java Spring Boot back-end`
  - TODO: Изпращане на HTTP заявки до back-end

## Диаграми и изображения:
1. Начинат на свързване на отделните електрически компоненти:
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
    
2. Концепция за изгледът на мобилното приложение:
    <table>
    <th>
      <td>
          <img alt="Android App Concept" src="https://user-images.githubusercontent.com/61236255/205665030-26153516-7c7a-4e35-860b-e8bee4cdbec9.png">
      </td>
      <td>
          <img alt="Component Diagram" src="https://user-images.githubusercontent.com/61236255/205666403-f396560f-9822-4ea2-a923-83595a230cb3.png">
      </td>
    </th>
    </table>
3. Изработка на 3D-модел на робота (3D принтер):
   ![image](https://user-images.githubusercontent.com/61236255/205667209-8b177f33-c008-4231-b9d3-cafc775e5091.png)
   
## Използвани технологии
- IntelliJ IDEA Community Edition
  - Разработка, чрез [Java Spring Boot](https://spring.io/projects/spring-boot)
  - библиотека [pi4j](https://mvnrepository.com/artifact/com.pi4j/pi4j-core), позволява изпращане/получаване на данни по I2C протокол.
- Arduino IDE
  - използвани библиотеки:
    - [Wire](https://www.arduino.cc/reference/en/language/functions/communication/wire/), позволява изпращане/получаване на данни по I2C протокол.
    - [AccelStepper](https://www.airspayce.com/mikem/arduino/AccelStepper/), позволява контролирането на стъпкови мотори
    - [ArduinoJson](https://arduinojson.org/), позволява работа с JSON тип
- Blender - софтуер за 3D моделиране




