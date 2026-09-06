# my-market-app
Веб-приложение «Витрина интернет-магазина» с использованием Spring Boot на реактивном стеке технологий
Приложение использует сервис платежей и redis в качестве кеша

# Проекты
1) market-app - основной проект «Витрина интернет-магазина»
2) payment-openapi - схема API к сервису платежей для кода генерации клиентской и серверной части 
3) payment-service - сервис платежей

# Основные возможности
    Просмотр витрины товаров
    Поиск и сортировка товаров
    Просмотр карточки товара
    Добавление и удаление товаров в корзину
    Управление количеством товаров в корзине
    Оформление заказа
    Просмотр списка заказов
    Просмотр деталей заказа
    Загрузка и отображение изображений товаров

| Method | Endpoint | Description | Template / Redirect |
|--------|----------|-------------|---------------------|
| GET | `/cart/items` | Просмотр корзины (список товаров и итог) | `cart` |
| POST | `/cart/items` | Изменение количества или удаление товара в корзине | `cart` |
| GET | `/items` (или `/`) | Список товаров с поиском, сортировкой и пагинацией | `items` |
| POST | `/items` (или `/`) | Выполнить действие над товаром и перенаправить с сохранением параметров | `redirect:/items` |
| GET | `/items/{id}` | Детальная информация о товаре | `item` |
| POST | `/items/{id}` | Выполнить действие над товаром и показать обновлённую детальную страницу | `item` |
| GET | `/orders` | Список всех заказов | `orders` |
| GET | `/orders/{id}` | Детали заказа (с опциональным флагом `newOrder`) | `order` |
| POST | `/buy` | Создать новый заказ и перенаправить на его страницу | `redirect:/orders/{id}` |

# Запуск приложения
Redis
docker run --name redis-server -it --rm -p 6379:6379 redis:7.4.2-bookworm sh -c "redis-server && sleep 7 && redis-cli"
## Maven
`mvn clean package -DskipTests && java -jar market-app/target/market-app-*.jar & java -jar payment-service/target/payment-service-*.jar`

## Docker
1) Выполнить - `mvn clean package`
2) Сборка Docker-образа - build -t my-java-app:latest .
3) Запуск Docker-контейнера - docker run -d -p 8080:8080 my-java-app:latest

Приложение доступно по адресу:
http://localhost:8080

## Запуск тестов
`mvn test`