package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.SpaEmployee;
import ega.spring.fitnessClubJdbc.models.Trainer;
import ega.spring.fitnessClubJdbc.repositories.SpaEmployeeRepository;
import ega.spring.fitnessClubJdbc.repositories.TrainerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final PasswordEncoder passwordEncoder;
    private final TrainerRepository trainerRepository;
    private final SpaEmployeeRepository spaEmployeeRepository;

    public AdminService(PasswordEncoder passwordEncoder, TrainerRepository trainerRepository, SpaEmployeeRepository spaEmployeeRepository) {
        this.passwordEncoder = passwordEncoder;
        this.trainerRepository = trainerRepository;
        this.spaEmployeeRepository = spaEmployeeRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void doAdminStaff() {
        System.out.println("Admin staff");
    }

    @Value("${employee.default.password}")
    private String password;

    public void register(Trainer trainer) {
        trainer.setPassword(passwordEncoder.encode(password));
        trainer.setRole("TRAINER");
        trainerRepository.save(trainer);
    }

    public void register(SpaEmployee spaEmployee) {
        spaEmployee.setPassword(passwordEncoder.encode(password));
        spaEmployee.setRole("SPA");
        spaEmployeeRepository.save(spaEmployee);
    }
}
