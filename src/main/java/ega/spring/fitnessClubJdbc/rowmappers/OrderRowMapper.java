package ega.spring.fitnessClubJdbc.rowmappers;

import ega.spring.fitnessClubJdbc.dto.OrderDTO;
import ega.spring.fitnessClubJdbc.models.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderRowMapper implements RowMapper<OrderDTO> {

    @Override
    public OrderDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        // Создаем новый заказ
        OrderDTO order = new OrderDTO();
        order.setOrderId(rs.getInt("order_id"));
        order.setOrderDate(rs.getTimestamp("order_date"));
        order.setTime(rs.getTime("time").toLocalTime());
        order.setStatus(rs.getString("status"));
        order.setTotalPrice(rs.getDouble("total_price"));
        order.setUserName(rs.getString("username"));

        // Список продуктов для текущего заказа
        List<Product> products = new ArrayList<>();

        // Добавляем первый продукт
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getDouble("price"));
        products.add(product);

        // Сохраняем продукты в список, пока не встретится новый заказ
        while (rs.next() && rs.getInt("order_id") == order.getOrderId()) {
            product = new Product();
            product.setId(rs.getInt("product_id"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getDouble("price"));
            products.add(product);
        }

        // Устанавливаем список продуктов в заказ
        order.setProducts(products);

        return order;
    }
}
