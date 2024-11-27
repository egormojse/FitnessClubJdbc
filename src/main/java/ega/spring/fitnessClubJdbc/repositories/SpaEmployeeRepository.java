package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.dto.PopularSpaEmployee;
import ega.spring.fitnessClubJdbc.models.SpaEmployee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpaEmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
   public SpaEmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SpaEmployee> findAllByDeletedFalse() {
        String sql = "SELECT * FROM spa_employees WHERE deleted = false";
        return jdbcTemplate.query(sql, spaEmployeeRowMapper());
    }

    public SpaEmployee findById(int id) {
        String sql = "SELECT * FROM spa_employees WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, spaEmployeeRowMapper());
    }

    public List<SpaEmployee> findBySpecialization(String specialization) {
        String sql = "SELECT * FROM spa_employees WHERE specialization = ?";
        return jdbcTemplate.query(sql, new Object[]{specialization}, spaEmployeeRowMapper());
    }

    public void save(SpaEmployee spaEmployee) {
        if (spaEmployee == null) {
            String sql = "INSERT INTO spa_employees (name, specialization, deleted) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, spaEmployee.getName(), spaEmployee.getSpecialization(), spaEmployee.isDeleted());
        } else {
            String sql = "UPDATE spa_employees SET name = ?, specialization = ?, deleted = ? WHERE id = ?";
            jdbcTemplate.update(sql, spaEmployee.getName(), spaEmployee.getSpecialization(), spaEmployee.isDeleted(), spaEmployee.getId());
        }
    }

    private RowMapper<SpaEmployee> spaEmployeeRowMapper() {
        return (rs, rowNum) -> {
            SpaEmployee spaEmployee = new SpaEmployee();
            spaEmployee.setId(rs.getInt("id"));
            spaEmployee.setName(rs.getString("name"));
            spaEmployee.setSpecialization(rs.getString("specialization"));
            spaEmployee.setDeleted(rs.getBoolean("deleted"));
            return spaEmployee;
        };
    }

    public List<PopularSpaEmployee> getPopularSpaEmployees() {
        String sql = "SELECT s.name AS name, COUNT(*) AS count " +
                "FROM spa_booking sb " +
                "JOIN spa_employees s ON sb.employee_id = s.id " +
                "GROUP BY s.name " +
                "ORDER BY count DESC LIMIT 3";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new PopularSpaEmployee(
                rs.getString("name"),
                rs.getInt("count")
        ));
    }

}
