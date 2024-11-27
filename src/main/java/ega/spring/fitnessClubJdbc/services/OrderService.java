package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.dto.OrderDTO;
import ega.spring.fitnessClubJdbc.models.Order;
import ega.spring.fitnessClubJdbc.models.OrderItem;
import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.models.Product;
import ega.spring.fitnessClubJdbc.repositories.OrderRepository;
import ega.spring.fitnessClubJdbc.rowmappers.OrderRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final JdbcTemplate jdbcTemplate;
    private final OrderRepository orderRepository;

    public OrderService(JdbcTemplate jdbcTemplate, OrderRepository orderRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderRepository = orderRepository;
    }

    public void addProductToOrder(Order order, Product product, int quantity) {
        // Проверяем, есть ли уже этот продукт в заказе
        Optional<OrderItem> existingItem = order.getOrderItems().stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst();

        if (existingItem.isPresent()) {
            // Если товар уже есть, увеличиваем количество и обновляем цену
            OrderItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setPrice(item.getPrice() + product.getPrice() * quantity);
        } else {
            // Если товара нет, создаём новый элемент заказа
            OrderItem newItem = new OrderItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice() * quantity);
            order.getOrderItems().add(newItem);
        }

        // Пересчитываем общую стоимость заказа
        double totalPrice = order.getOrderItems().stream()
                .mapToDouble(OrderItem::getPrice)
                .sum();
        order.setTotal_price(totalPrice);
    }


    @Transactional
    public void processOrder(Order order) {
        if (order.getOrderItems().isEmpty()) {
            throw new IllegalStateException("Корзина пуста. Добавьте товары в заказ.");
        }

        order.setDate(new Date()); // Устанавливаем дату
        order.setTime(LocalTime.now().withSecond(0).withNano(0)); // Устанавливаем текущее время

        // Сохраняем заказ и получаем его ID
        String sql = "INSERT INTO orders (user_id, order_date, time, status, total_price) VALUES (?, ?, ?, ?, ?) RETURNING id";

        int orderId = jdbcTemplate.queryForObject(sql, new Object[]{
                order.getUser().getId(),
                order.getDate(), // Дата
                order.getTime(), // Время
                order.getStatus(),
                order.getTotal_price(),
        }, Integer.class);

        order.setId(orderId);

        // Сохраняем товары из заказа
        for (OrderItem item : order.getOrderItems()) {
            String itemSql = "INSERT INTO order_item (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(itemSql, orderId, item.getProduct().getId(), item.getQuantity(), item.getPrice());

            // Проверка наличия товара на складе
            String stockCheckSql = "SELECT stock FROM product WHERE id = ?";
            Integer currentStock = jdbcTemplate.queryForObject(stockCheckSql, new Object[]{item.getProduct().getId()}, Integer.class);

            if (currentStock == null || currentStock < item.getQuantity()) {
                throw new IllegalStateException("Недостаточное количество товара на складе: " + item.getProduct().getName());
            }

            // Обновление количества товара на складе
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
    public List<OrderDTO> getAll() {
        String sql = "SELECT o.id AS order_id, o.order_date, o.time, o.total_price, o.status, u.username, " +
                "p.id AS product_id, p.name, p.price " +
                "FROM orders o " +
                "JOIN person u ON o.user_id = u.id " +
                "JOIN order_item op ON o.id = op.order_id " +
                "JOIN product p ON op.product_id = p.id " +
                "WHERE o.deleted = false";

        return jdbcTemplate.query(sql, new OrderRowMapper());
    }


    private RowMapper<Order> orderRowMapperForAll() {
        return (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setTotal_price(rs.getDouble("total_price"));
            order.setStatus(rs.getString("status"));
            order.setDate(rs.getDate("order_date"));
            order.setTime(rs.getTime("time").toLocalTime());
            Person person = new Person();
            person.setId(rs.getInt("user_id"));
            person.setUsername(rs.getString("user_name"));
            order.setUser(person);
            return order;
        };
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
            order.setDate(rs.getDate("order_date"));
            order.setTime(rs.getTime("time").toLocalTime());
            return order;
        };
    }


}
