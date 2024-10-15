package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.Order;
import ega.spring.fitnessClubJdbc.models.OrderItem;
import ega.spring.fitnessClubJdbc.models.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {
    private final JdbcTemplate jdbcTemplate;

    public OrderService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addProductToOrder(Order order, Product product, int quantity) {
        OrderItem item = new OrderItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setPrice(product.getPrice() * quantity);
        order.getOrderItems().add(item);
        order.setTotal_price(order.getTotal_price() + item.getPrice());
    }

    @Transactional
    public void processOrder(Order order) {
        if (order.getOrderItems().isEmpty()) {
            throw new IllegalStateException("Корзина пуста. Добавьте товары в заказ.");
        }

        order.setDate(new Date());

        String sql = "INSERT INTO orders (user_id, order_date, status, total_price) VALUES (?, ?, ?, ?) RETURNING id";

        int orderId = jdbcTemplate.queryForObject(sql, new Object[]{
                order.getUser().getId(),
                order.getDate(), // Сохраняем дату
                order.getStatus(),
                order.getTotal_price(),
        }, Integer.class);

        order.setId(orderId);

        for (OrderItem item : order.getOrderItems()) {
            String itemSql = "INSERT INTO order_item (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(itemSql, orderId, item.getProduct().getId(), item.getQuantity(), item.getPrice());

            String stockCheckSql = "SELECT stock FROM product WHERE id = ?";
            Integer currentStock = jdbcTemplate.queryForObject(stockCheckSql, new Object[]{item.getProduct().getId()}, Integer.class);

            if (currentStock == null || currentStock < item.getQuantity()) {
                throw new IllegalStateException("Недостаточное количество товара на складе: " + item.getProduct().getName());
            }

            String updateStockSql = "UPDATE product SET stock = stock - ? WHERE id = ?";
            jdbcTemplate.update(updateStockSql, item.getQuantity(), item.getProduct().getId());
        }
    }

}
