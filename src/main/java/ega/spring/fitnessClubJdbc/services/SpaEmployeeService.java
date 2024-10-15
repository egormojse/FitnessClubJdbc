package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.SpaEmployee;
import ega.spring.fitnessClubJdbc.repositories.SpaEmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpaEmployeeService {

    private final ega.spring.fitnessClubJdbc.repositories.SpaEmployeeRepository spaEmployeeRepository;

    public SpaEmployeeService(SpaEmployeeRepository spaEmployeeRepository) {
        this.spaEmployeeRepository = spaEmployeeRepository;
    }

    public List<SpaEmployee> getAllEmployees() {
        return spaEmployeeRepository.findAll();
    }

    public Optional<SpaEmployee> getEmployeeById(int id) {
        return Optional.ofNullable(spaEmployeeRepository.findById(id));
    }

    public List<SpaEmployee> getEmployeesBySpecialization(String specialization) {
        return spaEmployeeRepository.findBySpecialization(specialization);
    }
}
