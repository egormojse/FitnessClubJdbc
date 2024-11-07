package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.SpaEmployee;
import ega.spring.fitnessClubJdbc.repositories.SpaEmployeeRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaEmployeeService {

    private final ega.spring.fitnessClubJdbc.repositories.SpaEmployeeRepository spaEmployeeRepository;
    private final JdbcTemplate jdbcTemplate;

    public SpaEmployeeService(SpaEmployeeRepository spaEmployeeRepository, JdbcTemplate jdbcTemplate) {
        this.spaEmployeeRepository = spaEmployeeRepository;
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<SpaEmployee> getAllEmployees() {
        String sql = "SELECT * FROM spa_employees WHERE deleted = false";
        return jdbcTemplate.query(sql, spaEmployeeRowMapper());
    }

    public SpaEmployee getEmployeeById(int id) {
        String sql = "SELECT * FROM spa_employees WHERE id = ? AND deleted = false";
        List<SpaEmployee> employees = jdbcTemplate.query(sql, new Object[]{id}, spaEmployeeRowMapper());
        return employees.isEmpty() ? null : employees.get(0);
    }

    public List<SpaEmployee> getEmployeesBySpecialization(String specialization) {
        String sql = "SELECT * FROM spa_employees WHERE specialization = ? AND deleted = false";
        return jdbcTemplate.query(sql, new Object[]{specialization}, spaEmployeeRowMapper());
    }

    public void save(SpaEmployee spaEmployee) {
        if (spaEmployee.getId() == 0) {
            String sql = "INSERT INTO spa_employees (name, specialization, deleted) VALUES (?, ?, false)";
            jdbcTemplate.update(sql, spaEmployee.getName(), spaEmployee.getSpecialization());
        } else {
            String sql = "UPDATE spa_employees SET name = ?, specialization = ? WHERE id = ?";
            jdbcTemplate.update(sql, spaEmployee.getName(), spaEmployee.getSpecialization(), spaEmployee.getId());
        }
    }

    public void updateEmployee(int id, SpaEmployee updatedSpaEmployee) {
        String sql = "UPDATE spa_employees SET name = ?, specialization = ? WHERE id = ? AND deleted = false";
        jdbcTemplate.update(sql, updatedSpaEmployee.getName(), updatedSpaEmployee.getSpecialization(), id);
    }

    public void deleteSpaEmployeeById(int spaEmployeeId) {
        String sql = "UPDATE spa_employees SET deleted = true WHERE id = ?";
        jdbcTemplate.update(sql, spaEmployeeId);
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
}
