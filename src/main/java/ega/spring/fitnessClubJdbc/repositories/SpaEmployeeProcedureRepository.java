package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.models.SpaEmployeeProcedure;
import ega.spring.fitnessClubJdbc.rowmappers.SpaEmployeeProcedureRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpaEmployeeProcedureRepository {

    private final JdbcTemplate jdbcTemplate;

    public SpaEmployeeProcedureRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(SpaEmployeeProcedure spaEmployeeProcedure) {
        String sql = "INSERT INTO spa_employee_procedure (id) VALUES (?)";
        jdbcTemplate.update(sql, spaEmployeeProcedure.getId());
    }

    public List<SpaEmployeeProcedure> findAll() {
        String sql = "SELECT * FROM spa_employee_procedure";
        return jdbcTemplate.query(sql, new SpaEmployeeProcedureRowMapper());
    }

    public SpaEmployeeProcedure findById(int id) {
        String sql = "SELECT * FROM spa_employee_procedure WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new SpaEmployeeProcedureRowMapper(), id);
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM spa_employee_procedure WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
