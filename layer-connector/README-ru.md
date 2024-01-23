# Руководство Layer Connector

## Обзор
Библиотека Layer Connector представляет собой процессор аннотаций Java, созданный для упрощения интеграции многослойных архитектур приложении Spring Boot. 
Он автоматизирует создание классов адаптеров, которые связывают два слоя вашего приложения. 
Этот README предоставляет руководство по эффективному использованию библиотеки.

## Содержание
1. [Введение](#введение) 
2. [Начало работы](#начало-работы) 
3. [Аннотации](#аннотации) 
4. [Пример использования](#пример-использования) 
5. [Сгенерированные классы](#сгенерированные-классы) 
6. [Обработка ошибок](#обработка-ошибок) 
7. [Версионирование](#версионирование) 
8. [Внесение вклада](#внесение-вклада) 
9. [Лицензия](#лицензия)

## Введение
Layer Connector нацелен на упрощение взаимодействия между разными слоями вашего приложения, 
уменьшая шаблонный код и способствуя более чистой архитектуре. Он использует обработку аннотаций для автоматического создания классов адаптеров, 
избавляя от необходимости вручную реализовывать эти соединители.

## Начало работы
Чтобы интегрировать Layer Connector в ваш проект, выполните следующие шаги:
1. Добавьте библиотеку Layer Connector в зависимости вашего проекта. 
2. Аннотируйте ваш интерфейс службы с @LayerConnector, предоставив необходимые детали, такие как класс-источник и мапперы для конвертации нужных полей. 
3. Постройте ваш проект, чтобы запустить процессор аннотаций, который сгенерирует соответствующий класс адаптера.

## Аннотации
### @LayerConnector
Основная аннотация, используемая для пометки интерфейса службы для обработки. 
Принимает параметры, такие как класс-источник и необходимые мапперы.

## Пример использования
Допустим у нас есть контроллер, который принимает запрос на получение данных. К нему поступает объект dto `UsernameDto`. 
```java
@RestController
@RequiredArgsConstructor
public class UserController {
    // ... fields
    @GetMapping("/user")
    public Mono<UserInfoDto> load(@RequestParam("username") UsernameDto usernameDto) {
        return userControllerService.load(usernameDto);
    }
    // ... other methods
}
```
И так же есть `UserService`, который содержит некую логику обработки данных перед тем, как отправить их в ответ.
Сервис в работе использует уже другие модели, в частности `Username`.
```java
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    // ... fields
    @Override
    public Mono<UserInfo> load(Username username) {
        return Mono.just(username)
                .map(userDao::load);
    }
    // ... other methods
}
```
Ввозникает запрос на то, чтобы связать эти два слоя приложения.
Здесь и включается в работу **Layer connector**.

Рассмотрим интерфейс `UserControllerService`, аннотированный `@LayerConnector`. 
```java
@LayerConnector(source = UserService.class, mapper = {UsernameDtoMapper.class, UserInfoDtoMapper.class, UserDtoMonoMapper.class})
public interface UserControllerService {
    Mono<UserInfoDto> load(UsernameDto usernameDto);
    // ... other methods
}
```
### Обязательные условия для создания Layer Connector
1. Интерфейс, помеченный аннотацией `@LayerConnector`, должен содержать те же названия методов, что и класс `source`.
2. Интерфейс и `source` классы должны иметь только по одному параметру в аргументах методов
3. Мапперы обязаны содержать методы для конвертации всех необходимых моделей.

Процессор аннотаций создаст соответствующий класс адаптера `UserControllerServiceLayerConnector`, 
который выступает в качестве адаптера, соединяющего служебный слой с данными.

```java
import com.niksob.domain.mapper.dto.user.UserDtoMonoMapper;
import com.niksob.domain.mapper.dto.user.UserInfoDtoMapper;

@javax.annotation.processing.Generated(
        value = "com.niksob.layer_connector.processor.LayerConnectorProcessor",
        date = "2024-01-18T01:03:06+0300",
        comments = "comments = version: 0.0.1-SNAPSHOT, compiler: javac, environment: Java 17"
)
@org.springframework.stereotype.Component
public class UserControllerServiceLayerConnector implements com.niksob.database_service.controller.user.UserControllerService {
    private final UserDtoMonoMapper userDtoMonoMapper;
    private final UserInfoDtoMapper userInfoDtoMapper;
    private final com.niksob.database_service.service.user.UserService source;

    @Override
    public reactor.core.publisher.Mono<com.niksob.domain.dto.user.UserInfoDto> load(com.niksob.domain.dto.user.UsernameDto value) {
        com.niksob.domain.model.user.Username mapped = usernameDtoMapper.fromDto(value);
        reactor.core.publisher.Mono<com.niksob.domain.model.user.UserInfo> result = source.load(mapped);
        reactor.core.publisher.Mono<com.niksob.domain.dto.user.UserInfoDto> mappedResult = userDtoMonoMapper.toDtoMono(result);
        return mappedResult;
    }
    // ... other methods
}
```
Теперь можно использовать `UserService` в `UserController`, обращаясь к `UserControllerService` и не используя никакого явного маппинга. 

## Сгенерированные классы
Сгенерированные классы адаптеров, такие как `UserControllerServiceLayerConnector`, инкапсулируют логику, 
необходимую для моста между двемя разными слоями приложения. 
Эти классы создаются автоматически процессором аннотаций, уменьшая усилия по ручной реализации.

## Обработка ошибок
Библиотека предоставляет набор механизмов обработки ошибок по умолчанию для общих исключений в методах службы. 
Настройте обработку ошибок, предоставив свою собственную реализацию в интерфейсе службы.

### Приемры исключительных ситуаций
1. В качестве параметров аннотации `@LayerConnector` были переданы null значения
2. В маппере отсутствует необходимый метод для конверсии параметра или возвращаемого значения метода
2. В паппере есть несколько методов, которые возвращают одно и то же значение и Layer connector не может выбрать, какой из них использовать
3. Аннотации `@LayerConnector` был передан `source` класс, содержащий не полный перечень методов, указанных в помеченном интерфейсе

## Логгирование
Логгирование организованно в виде передачи предупреждений во время сборки проекта. В основном сообщения содержат информацию о классе `Element`, 
который представляет собой абстракцию над всеми используемыми классами: `LayerConnector`, `source`, `mappers`.

## Версионирование
Эта библиотека следует семантическому версионированию. 
Проверьте аннотацию `@Generated` в сгенерированных классах для получения подробной информации о версии библиотеки, 
версии компилятора и среде выполнения.

## Внесение вклада
Мы приветствуем вклады от сообщества.

## Лицензия
Эта библиотека Layer Connector лицензирована в соответствии с лицензией MIT. См. файл LICENSE для получения дополнительной информации.