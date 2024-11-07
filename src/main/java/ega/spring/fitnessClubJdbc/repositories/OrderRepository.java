package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.models.Order;
import ega.spring.fitnessClubJdbc.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Order> findByUserId(int userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? AND deleted = false";
        return jdbcTemplate.query(sql, new Object[]{userId}, orderRowMapper());
    }

    public Order findById(int orderId) {
        String sql = "SELECT * FROM orders WHERE id = ? AND deleted = false";
        List<Order> orders = jdbcTemplate.query(sql, new Object[]{orderId}, orderRowMapper());
        return orders.isEmpty() ? null : orders.get(0);
    }

    public List<Order> findAllByDeletedFalse() {
        String sql = "SELECT * FROM orders WHERE deleted = false";
        return jdbcTemplate.query(sql, orderRowMapper());
    }

    public void save(Order order) {
        if (order.getId() == 0) {
            // Предполагается, что у вас есть поле user_id в таблице orders
            String sql = "INSERT INTO orders (user_id, total_price, status, order_date, deleted) VALUES (?, ?, ?, ?, false)";
            jdbcTemplate.update(sql, order.getUser().getId(), order.getTotal_price(), order.getStatus(), order.getDate());
        } else {
            String sql = "UPDATE orders SET total_price = ?, status = ?, order_date = ? WHERE id = ? AND deleted = false";
            jdbcTemplate.update(sql, order.getTotal_price(), order.getStatus(), order.getDate(), order.getId());
        }
    }

    private RowMapper<Order> orderRowMapper() {
        return (rs, rowNum) -> {
            Order order = new Order();
            order.setId(rs.getInt("id"));
            order.setTotal_price(rs.getDouble("total_price"));
            order.setStatus(rs.getString("status"));
            order.setDate(rs.getDate("date"));
            Person user = new Person();
            user.setId(rs.getInt("user_id"));
            order.setUser(user);
            order.setDeleted(rs.getBoolean("deleted"));
            return order;
        };
    }

    public void deleteById(int id) {
        String sql = "UPDATE orders SET deleted = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
