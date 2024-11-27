package ega.spring.fitnessClubJdbc.repositories;

import ega.spring.fitnessClubJdbc.dto.PopularTrainer;
import ega.spring.fitnessClubJdbc.models.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainerRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TrainerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Trainer> findAllByDeletedFalse() {
        String sql = "SELECT * FROM trainers WHERE deleted = false";
        return jdbcTemplate.query(sql, trainerRowMapper());
    }

    public List<PopularTrainer> getPopularTrainers() {
        String sql = "SELECT t.name AS name, COUNT(*) AS count " +
                "FROM workout_booking wb " +
                "JOIN trainers t ON wb.trainer_id = t.id " +
                "GROUP BY t.name " +
                "ORDER BY count DESC LIMIT 3";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new PopularTrainer(rs.getString("name"), rs.getInt("count")));
    }


    public Trainer findById(int id) {
        String sql = "SELECT * FROM trainers WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, trainerRowMapper());
    }

    public void save(Trainer trainer) {
        if (trainer == null) {
            String sql = "INSERT INTO trainers (name, specialization, deleted) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, trainer.getName(), trainer.getSpecialization(), trainer.isDeleted());
        } else {
            String sql = "UPDATE trainers SET name = ?, specialization = ?, deleted = ? WHERE id = ?";
            jdbcTemplate.update(sql, trainer.getName(), trainer.getSpecialization(), trainer.isDeleted(), trainer.getId());
        }
    }

    public void deleteById(int id) {
        Trainer trainer = findById(id);
        if (trainer != null) {
            trainer.setDeleted(true);
            save(trainer);
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
