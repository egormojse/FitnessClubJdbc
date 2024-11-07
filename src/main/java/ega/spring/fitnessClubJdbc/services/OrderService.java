package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.Order;
import ega.spring.fitnessClubJdbc.models.OrderItem;
import ega.spring.fitnessClubJdbc.models.Product;
import ega.spring.fitnessClubJdbc.repositories.OrderRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    private final JdbcTemplate jdbcTemplate;
    private final OrderRepository orderRepository;

    public OrderService(JdbcTemplate jdbcTemplate, OrderRepository orderRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderRepository = orderRepository;
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

    public List<Order> getUserOrders(int userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? AND deleted = false";
        return jdbcTemplate.query(sql, new Object[]{userId}, orderRowMapper());
    }

    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM orders WHERE deleted = false";
        return jdbcTemplate.query(sql, orderRowMapper());
    }

    public void deleteOrderById(int id) {
        String sql = "UPDATE orders SET deleted = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateOrder(int id, Order updatedOrder) {
        String sql = "UPDATE orders SET status = ?, total_price = ? WHERE id = ?";
        jdbcTemplate.update(sql, updatedOrder.getStatus(), updatedOrder.getTotal_price(), id);
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{orderId}, orderRowMapper());
    }

    private void saveOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, total_price, status, order_date) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, order.getUser().getId(), order.getTotal_price(), order.getStatus(), order.getDate());
        saveOrderItems(order);
    }

    private void saveOrderItems(Order order) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        for (OrderItem item : order.getOrderItems()) {
            jdbcTemplate.update(sql, order.getId(), item.getProduct().getId(), item.getQuantity(), item.getPrice());
        }
    }

    private void updateProductStock(Product product, int quantity) {
        String sql = "UPDATE product SET stock = stock - ? WHERE id = ?";
        jdbcTemplate.update(sql, quantity, product.getId());
    }

    private RowMapper<Order> orderRowMapper() {
        return (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setTotal_price(rs.getDouble("total_price"));
            order.setStatus(rs.getString("status"));
            order.setDate(rs.getDate("order_date"));  // замените на "order_date"
            return order;
        };
    }




}
