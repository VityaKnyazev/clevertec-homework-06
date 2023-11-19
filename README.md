<h1>CLEVERTEC (homework-06)</h1>

<p>CLEVERTEC homework-06:</p>
<ol>
<li>Создать любой gradle проект</li>
<li>Проект должен быть совместим с java 17</li>
<li>Придерживаться GitFlow: master -> develop -> feature/fix</li>
<li>Создать реализацию кэша, используя алгоритмы LRU и LFU</li>
<li>Создать в приложении слои service и dao (service будет вызывать слой dao, слой dao будет временная замена database). В этих сервисах реализовать CRUD операции для работы с entity. Работу организовать через интерфейсы.</li>
<li>Результат работы dao должен синхронизироваться с кешем через proxy (или кастомная аннотация, или АОП/aspectj). При работе с entity оперируем id. Алгоритм работы с кешем:</li>
<ul>
<li>GET - ищем в кеше и если там данных нет, то достаем объект из dao, сохраняем в кеш и возвращаем</li>
<li>POST - сохраняем в dao и потом сохраняем в кеше</li>
<li>DELETE - удаляем из dao и потом удаляем из кеша</li>
<li>PUT - обновление/вставка в dao и потом обновление/вставка в кеше</li>
</ul>
<li>Алгоритм и максимальный размер коллекции должны читаться из файла resources/application.yml</li>
<li>Создать entity, в нем должно быть поле id и еще минимум 4 поля</li>
<li>Service работает с dto.</li>
<li>Объекты (dto), которые принимает service, должны валидироваться. В т.ч. добавить regex валидацию</li>
<li>Кеши должны быть покрыты unit tests</li>
<li>Должен содержать javadoc и описанный README.md</li>
<li>Использовать lombok</li>
<li>*Реализовать метод для получения информации в формате xml</li>
<li>Заполнить и отправить форму</li>
</ol>
<p>Доп задание:</p>
<p>***Самописный JsonParser подтягивать как библиотеку и парсировать json через него</p>
<p>***В самописный JsonParser добавить возможность работы с xml</p>

<h2>Что сделано:</h2>
<ol>
<li>Создан gradle проект</li>
<li>Проект совместим с java 17</li>
<li>При написании следовал концепции GitFlow: master -> develop -> feature/fix</li>
<li>Созданы реализации кэша, используя алгоритмы LRU и LFU</li>
<li>Созданы в приложении слои service и dao (service вызывает слой dao, слой dao работает с postgresql). В сервисе реализованы CRUD операции для работы с entity. Работа организована через интерфейсы.</li>
<li>
Результат работы dao синхронизируется с кешем через proxy (используется dynamic proxy из коробки). При работе с entity оперируем id. Алгоритм работы с кешем:
</li>
<ul>
<li>GET - ищем в кеше и если там данных нет, то достаем объект из dao, сохраняем в кеш и возвращаем</li>
<li>POST - сохраняем в dao и потом сохраняем в кеше</li>
<li>DELETE - удаляем из dao и потом удаляем из кеша</li>
<li>PUT - обновление/вставка в dao и потом обновление/вставка в кеше</li>
</ul>
<li>Алгоритм и максимальный размер коллекции читаются из файла resources/application.yml</li>
<li>Создан entity, в нем есть поле id и еще 4 поля</li>
<li>Service работает с dto.</li>
<li>Объекты (dto), которые принимает service, валидируются в сервисе. В т.ч. в dto добавлена regex валидация</li>
<li>Кеши покрыты unit tests</li>
<li>Содержаться javadoc для методов классов и интерфейсов и описанный README.md</li>
<li>Использован lombok</li>
<li>*Реализован метод для получения информации в формате xml в dto</li>
<li>Заполнена и отправлена форма</li>
</ol>

<h3>Как запускать:</h3>
<ol>
<li>Билдим проект: .\gradlew clean build</li>
<li>Поднимаем docker: docker-compose up -d</li>
<li>Добавляем информацию в базу данных с помощью liquibase: .\gradlew update</li>
</ol>
<p>Запускаем main метод из Main класса, проверяем работу приложения.</p>