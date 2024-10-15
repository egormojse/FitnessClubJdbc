package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.models.SpaEmployee;
import ega.spring.fitnessClubJdbc.rowmappers.SpaEmployeeRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpaEmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public SpaEmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(SpaEmployee spaEmployee) {
        String sql = "INSERT INTO spa_employees (name, username, email, specialization, experience, bio, password, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, spaEmployee.getName(), spaEmployee.getUsername(), spaEmployee.getEmail(),
                spaEmployee.getSpecialization(), spaEmployee.getExperience(), spaEmployee.getBio(),
                spaEmployee.getPassword(), spaEmployee.getRole());
    }

    public List<SpaEmployee> findAll() {
        String sql = "SELECT * FROM spa_employees";
        return jdbcTemplate.query(sql, new SpaEmployeeRowMapper());
    }

    public SpaEmployee findById(int id) {
        String sql = "SELECT * FROM spa_employees WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new SpaEmployeeRowMapper(), id);
    }

    public List<SpaEmployee> findBySpecialization(String specialization) {
        String sql = "SELECT * FROM spa_employees WHERE specialization = ?";
        return jdbcTemplate.query(sql, new SpaEmployeeRowMapper(), specialization);
    }
}
