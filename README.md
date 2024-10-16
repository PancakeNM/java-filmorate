# java-filmorate Study project
____
## База данных
### ER-Диаграмма базы данных проекта:
![Диаграмма, отображающая связи таблиц в базе данных проекта](/assets/images/Filmorate_db.png)
#### Примеры запросов:
1. Топ-N Самых популярных фильмов:
````
SELECT name
FROM films
WHERE id IN (SELECT film_id
    FROM likes
    GROUP BY film_id
    ORDER BY COUNT(user_id) DESC)
LIMIT N
````
