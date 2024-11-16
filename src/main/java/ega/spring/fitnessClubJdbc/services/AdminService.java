package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.SpaEmployee;
import ega.spring.fitnessClubJdbc.models.Trainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;


    public AdminService(PasswordEncoder passwordEncoder, JdbcTemplate jdbcTemplate) {
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void doAdminStaff() {
        System.out.println("Admin staff");
    }

    @Value("${employee.default.password}")
    private String password;


    public void register(Trainer trainer) {
        String encodedPassword = passwordEncoder.encode(password);
        String sql = "INSERT INTO trainers (name, email, password, role) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, trainer.getName(), trainer.getEmail(), encodedPassword, "TRAINER");
    }

    public void register(SpaEmployee spaEmployee) {
        String encodedPassword = passwordEncoder.encode(password);
        String sql = "INSERT INTO spa_employees (name, email, password, role) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql, spaEmployee.getName(), spaEmployee.getEmail(), encodedPassword, "SPA");
    }
}
