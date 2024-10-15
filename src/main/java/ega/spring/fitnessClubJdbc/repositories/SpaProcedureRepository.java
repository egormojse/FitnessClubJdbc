package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.models.SpaProcedure;
import ega.spring.fitnessClubJdbc.rowmappers.SpaProcedureRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpaProcedureRepository {

    private final JdbcTemplate jdbcTemplate;

    public SpaProcedureRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SpaProcedure> findAll() {
        String sql = "SELECT * FROM spa_procedures";
        return jdbcTemplate.query(sql, new SpaProcedureRowMapper());
    }

    public SpaProcedure findById(int procedureId) {
        String sql = "SELECT * FROM spa_procedures WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new SpaProcedureRowMapper(), procedureId);
    }

    public List<SpaProcedure> findByType(String type) {
        String sql = "SELECT * FROM spa_procedures WHERE type = ?";
        return jdbcTemplate.query(sql, new SpaProcedureRowMapper(), type);
    }
}
