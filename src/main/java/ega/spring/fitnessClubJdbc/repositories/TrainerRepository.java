package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.models.Trainer;
import ega.spring.fitnessClubJdbc.rowmappers.TrainerRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainerRepository {

    private final JdbcTemplate jdbcTemplate;

    public TrainerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Trainer> findAll() {
        String sql = "SELECT * FROM trainers";
        return jdbcTemplate.query(sql, new TrainerRowMapper());
    }

    public Trainer findById(int id) {
        String sql = "SELECT * FROM trainers WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new TrainerRowMapper(), id);
    }

    public void save(Trainer trainer) {
        String sql = "INSERT INTO trainers (name, username, email, specialization, experience, bio, password, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, trainer.getName(), trainer.getUsername(), trainer.getEmail(),
                trainer.getSpecialization(), trainer.getExperience(), trainer.getBio(),
                trainer.getPassword(), trainer.getRole());
    }
}
