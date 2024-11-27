package ega.spring.fitnessClubJdbc.generators;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Random;

public class OrderGenerator {

    public static void main(String[] args) {
        String[] statuses = {"Оформлен", "Обрабатывается", "Получен"};
        int numberOfUsers = 13; // Количество пользователей
        int maxOrdersPerUser = 5; // Максимальное количество заказов на пользователя
        int maxItemsPerOrder = 3; // Максимальное количество товаров в заказе

        try (FileWriter writer = new FileWriter("generated_orders.sql")) {
            Random random = new Random();
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusMonths(1);

            int productCount = 12; // Количество товаров
            int orderCounter = 1;  // Счетчик заказов

            // Устанавливаем локаль с точкой как разделителем
            Locale.setDefault(Locale.US);

            for (int userId = 1; userId <= numberOfUsers; userId++) {
                int ordersPerUser = random.nextInt(maxOrdersPerUser - 3 + 1) + 3; // От 3 до maxOrdersPerUser

                for (int j = 0; j < ordersPerUser; j++) {
                    // Случайная дата и время заказа
                    LocalDate orderDate = startDate.plusDays(random.nextInt((int) (endDate.toEpochDay() - startDate.toEpochDay())));
                    LocalTime orderTime = LocalTime.of(random.nextInt(24), random.nextInt(60), random.nextInt(60));

                    // Случайный статус и общая цена
                    String status = statuses[random.nextInt(statuses.length)];
                    double totalPrice = 0.0;

                    // Генерация товаров для заказа
                    int itemsInOrder = random.nextInt(maxItemsPerOrder) + 1; // От 1 до maxItemsPerOrder
                    StringBuilder itemsSql = new StringBuilder();
                    for (int k = 0; k < itemsInOrder; k++) {
                        int productId = random.nextInt(productCount) + 1; // ID товара от 1 до productCount
                        int quantity = random.nextInt(5) + 1; // Количество товара от 1 до 5
                        double price = 10 + random.nextDouble() * 40; // Случайная цена от 10 до 50
                        totalPrice += price * quantity;

                        itemsSql.append(String.format(
                                "INSERT INTO order_item (order_id, product_id, quantity, price) " +
                                        "VALUES (%d, %d, %d, %.2f);\n",
                                orderCounter, productId, quantity, price
                        ));
                    }

                    // Генерация запроса для заказа
                    String orderSql = String.format(
                            "INSERT INTO orders (user_id, order_date, total_price, status, time) " +
                                    "VALUES (%d, '%s', %.2f, '%s', '%s');\n",
                            userId, orderDate, totalPrice, status, orderTime
                    );

                    // Запись в файл
                    writer.write(orderSql);
                    writer.write(itemsSql.toString());
                    orderCounter++;
                }
            }

            System.out.println("SQL-файл сгенерирован: generated_orders.sql");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
