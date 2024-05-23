# Руководство по использованию Logger

## Обзор
Модуль Logger представляет собой надстройку над Logback логером и представляет свои интерфейсы Logger и LoggerFactory: ObjectStateLogger и ObjectStateLoggerFactory соответственно. Данный реализация позволяет отельно логировать состояние объекта в формате JSON с помощью специального формата и MDC. А так же маскирует чувствительные данные в логах.

## Применение
Использование ObjectStateLogger особенно удобно в случае, когда логи собираются и отображаются другими системами. С таким логером можно отдельно собирать состояние объекта и следить за его изменениями. Такой подход более наглядный.  

## Начало работы
Чтобы интегрировать Logger в ваш проект, выполните следующие шаги:
### 1. Добавьте зависимость на logger в ваш проект

_Для Maven:_
```
<dependency>
    <groupId>com.niksob</groupId>
    <artifactId>logger</artifactId>
    <version>1.0.0</version>
</dependency>
```
_Для gradle:_
```
implementation 'com.niksob:logger:1.0.0'
```

2. Добавьте фабрику в качестве стандартного логера в файле конфигурации
_Для application.yml_:
```yaml
org:
  slf4j:
    LoggerFactory: com.niksob.logger.object_state.factory.ObjectStateLoggerFactory
```
_Для application.properties:_
```properties
org.slf4j.LoggerFactory=com.niksob.logger.object_state.factory.ObjectStateLoggerFactory
```

3. Добавьте поля, которые нужно маскировать
   _Для application.yml_:
```yaml
logger:
  message:
    masked:
      field-names:
        - password
        - rowPassword
        - accessToken
        - access
        - refreshToken
        - refresh
```
_Для application.properties:_
```properties
logger.message.masked.field-names=password,rowPassword,accessToken,access,refreshToken,refresh
```

## Пример работы
```java
public class UserRepository {
    private final ObjectStateLogger log = ObjectStateLoggerFactory.getLogger(CachedUserEntityLoaderDao.class);
    // Other fields

    public UserEntity loadById(Long id) {
        try {
            // Other code
        } catch (Exception e) {
            log.error("User entity not loaded from repository", e, user);
        }
        // Other code
    }
}
```
В данном коде мы логируем состояние объекта user в качестве отдельного поля в логах, а не интегрируем в само сообщение.

## Лицензия
Эта библиотека Layer Connector лицензирована в соответствии с лицензией MIT. См. файл LICENSE для получения дополнительной информации.
