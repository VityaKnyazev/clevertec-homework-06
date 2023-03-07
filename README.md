<h1>CLEVERTEC (homework-06)</h1>

<p>CLEVERTEC homework-06:</p>
<ol>
<li>Создать реализацию кэша, используя алгоритмы LRU и LFU.</li>
<li>Алгоритм и максимальный размер коллекции должны читаться из файла resources/application.yml.</li>
<li>Коллекция должна инициализироваться через фабрику.</li>
<li>Код должен содержать javadoc и описанный README.md.</li>
<li>кэши должны быть покрыты тестами.</li>
<li>Создать entity, в нем должно быть поле id и еще min 4 поля. Добавить поле, проверяемое regex.</li>
<li>Создать в приложении слои service и dao (service будет вызывать слой dao, слой dao будет временная замена database).
В этих сервисах реализовать CRUD операции для работы с entity. Работу организовать через интерфейсы.</li>
<li>Результат работы dao должен синхронизироваться с кэшем через proxy (или кастомная аннотация или АОП/aspectj). При работе с entity оперируем id. Алгоритм работы с кэшем: 
GET - ищем в кэше и если там данных нет, то достаем объект из dao, сохраняем в кэш и возвращаем
POST - сохраняем в dao и потом сохраняем в кэше
DELETE - удаляем из dao и потом удаляем из кэша
PUT - обновление/вставка в dao и потом обновление/вставка в кэше.
</li>
<p>** Реализовать метод для получения информации в формате xml.</p>
<p>P.S. эти доработки реализовать в тестовом задании "чек".</p>
</ol>

<h2>Что сделано:</h2>
<ol>
<li>Создана реализация кэша, используя алгоритмы LRU и LFU в пакете ru.clevertec.knyazev.cache.</li>
<li>Алгоритм и максимальный размер коллекции читаются из файла resources/application.yml.</li>
<li>Инициализация кэша реализована через "Простую фабрику". в пакете ru.clevertec.knyazev.cache.</li>
<li>Код с алгоритмами кэширования содержит javadoc. Отредактирован README.md.</li>
<li>Кэши покрыты тестами в src/test/java пакет ru.clevertec.knyazev.cache.</li>
<li>Создан entity Seller в пакете ru.clevertec.knyazev.entity. В Seller добавлены поля с id и еще 4 поля. Поле email проверяется через regex. 
Реализована кастомная валидация в класе ru.clevertec.knyazev.data.validator.SellerValidator.</li>
<li>В слои DAO, Service, Controller добавлены классы (ru.clevertec.knyazev.dao.SellerDAOJPA, ru.clevertec.knyazev.service.SellerServiceImpl, 
ru.clevertec.knyazev.rest.controller.SellerController). Реализована возможность получения, добавления, изменения, удаления (CRUD-операции) 
для созданной entity Seller. </li>
<li>
Результат работы dao синхронизируется с кэшем через proxy (разработан, используя JDK Dynamic Proxy, класс ru.clevertec.knyazev.dao.proxy.SellerDAOProxy). При работе с entity оперируем id. Реализован алгоритм работы с кэшем: 
GET-запрос - ищем в кэше и если там данных нет, то достаем объект из dao, сохраняем в кэш и возвращаем в формате JSON. 
POST-запрос - сохраняем в dao и потом сохраняем в кэше. 
DELETE-запрос - удаляем из dao и потом удаляем в кэше. 
PUT-запрос - обновление/вставка в dao и потом обновление/вставка в кэше.
</li>
<li>**Добавлена возможность получения ответа в формате xml (вместо JSON) по GET для сущности Seller и коллекции таких сущностей.</li>
</ol>
<h3>Как запускать:</h3>
<ol>
<li>Билдим проект: .\gradlew build</li>
<li>Поднимаем docker: docker-compose up -d</li>
<li>Добавляем информацию в базу данных: .\gradlew update</li>
<ol>
<p>Делаем запросы из postman: </p>
<li>GET: URL: http://HOST:8080/clevertec/sellers или http://HOST:8080/clevertec/sellers/1</li>
<li>POST: URL: http://HOST:8080/clevertec/sellers Headers: Content-Type: application/json, Body: {"name":"Zaur","familyName":"Egorov","email":"zaur@mail.ru", "role": "manager"}</li>
<li>PUT: http://HOST:8080/clevertec/sellers Headers: Content-Type: application/json, Body: {"id":6, "name":"Zurab","familyName":"Petrov","email":"zurab@mail.ru", "role": "cashier"}</li>
<li>DELETE: http://HOST:8080/clevertec/sellers Headers: Content-Type: application/json, Body: {"id":6, "name":"Zurab","familyName":"Petrov","email":"zurab@mail.ru", "role": "cashier"}</li>
</ol>
</ol>