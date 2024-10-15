package ega.spring.fitnessClubJdbc.rowmappers;

import ega.spring.fitnessClubJdbc.models.Trainer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrainerRowMapper implements RowMapper<Trainer> {
    @Override
    public Trainer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Trainer trainer = new Trainer();
        trainer.setId(rs.getInt("id"));
        trainer.setName(rs.getString("name"));
        return trainer;
    }
}
