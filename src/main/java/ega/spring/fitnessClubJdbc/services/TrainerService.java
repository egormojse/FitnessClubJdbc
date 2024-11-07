package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.repositories.TrainerRepository;
import ega.spring.fitnessClubJdbc.models.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, JdbcTemplate jdbcTemplate) {
        this.trainerRepository = trainerRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Trainer> getAllTrainers() {
        String sql = "SELECT * FROM trainers WHERE deleted = false";
        return jdbcTemplate.query(sql, trainerRowMapper());
    }

    public Trainer getTrainerById(int trainerId) {
        String sql = "SELECT * FROM trainers WHERE id = ? AND deleted = false";
        return jdbcTemplate.queryForObject(sql, new Object[]{trainerId}, (rs, rowNum) -> {
            Trainer trainer = new Trainer();
            trainer.setId(rs.getInt("id"));
            trainer.setName(rs.getString("name"));
            return trainer;
        });
    }

    public void deleteTrainerById(int id) {
        String sql = "UPDATE trainers SET deleted = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateTrainer(int id, Trainer updatedTrainer) {
        String sql = "UPDATE trainers SET name = ?, specialization = ? WHERE id = ? AND deleted = false";
        jdbcTemplate.update(sql, updatedTrainer.getName(), updatedTrainer.getSpecialization(), id);
    }

    public void save(Trainer trainer) {
        if (trainer.getId() == 0) {
            String sql = "INSERT INTO trainers (name, specialization, deleted) VALUES (?, ?, false)";
            jdbcTemplate.update(sql, trainer.getName(), trainer.getSpecialization());
        } else {
            String sql = "UPDATE trainers SET name = ?, specialization = ? WHERE id = ?";
            jdbcTemplate.update(sql, trainer.getName(), trainer.getSpecialization(), trainer.getId());
        }
    }

    private RowMapper<Trainer> trainerRowMapper() {
        return (rs, rowNum) -> {
            Trainer trainer = new Trainer();
            trainer.setId(rs.getInt("id"));
            trainer.setName(rs.getString("name"));
            trainer.setSpecialization(rs.getString("specialization"));
            trainer.setDeleted(rs.getBoolean("deleted"));
            return trainer;
        };
    }
}
