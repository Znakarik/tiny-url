### TO DO

- создать БД
  - поставить в локаль PostgreSQL ✅
  - создать БД ✅
- создать таблицы 
  - определиться с моделью данных ✅
    - сделать скрипт создания таблицы URLS
  - сделать класс модели данных ✅
  - сделать скрипт создания таблиц 
  - сделать скрипт с заданием tmp данными
- создать уровни (сервис, БД, репозиторий)
- сделать REST контроллер с созданием ссылки 
- сделать простой UI с формой, которая принимает строку и отдает короткую ссылку
- сделать REST контроллер, который делает редирект по длинному URL из БД 


### Модель
- id (primary_key)
- short_url (string)
- long_url (string)
- date_time_created (string)
``

````
CREATE TABLE urls (
id VARCHAR(100) PRIMARY KEY,
short_url VARCHAR(100) NOT NULL,
long_url VARCHAR(200) NOT NULL,
create_date_time VARCHAR(100) NOT NULL
);
````

```
CREATE TABLE url_redirects (
id VARCHAR(100) PRIMARY KEY,
url_id VARCHAR(100) NOT NULL,
create_date_time VARCHAR(100) NOT NULL
);
```