package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.models.SpaProcedure;
import org.springframework.stereotype.Service;
import ega.spring.fitnessClubJdbc.repositories.SpaProcedureRepository;

import java.util.List;

@Service
public class SpaProcedureService {

    private final SpaProcedureRepository spaProcedureRepository;

    public SpaProcedureService(SpaProcedureRepository spaProcedureRepository) {
        this.spaProcedureRepository = spaProcedureRepository;
    }

    public List<SpaProcedure> getAllProcedures() {
        return spaProcedureRepository.findAll();
    }

    public List<SpaProcedure> getProceduresByType(String type) {
        return spaProcedureRepository.findByType(type);
    }

    public SpaProcedure getProcedureById(int procedureId) {
        SpaProcedure procedure = spaProcedureRepository.findById(procedureId);
        if (procedure == null) {
            throw new RuntimeException("Procedure not found");
        }
        return procedure;
    }
}
