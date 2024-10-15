package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.repositories.SpaEmployeeProcedureRepository;
import ega.spring.fitnessClubJdbc.models.SpaEmployeeProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpaEmployeeProcedureService {

    private final SpaEmployeeProcedureRepository spaEmployeeProcedureRepository;

    @Autowired
    public SpaEmployeeProcedureService(SpaEmployeeProcedureRepository spaEmployeeProcedureRepository) {
        this.spaEmployeeProcedureRepository = spaEmployeeProcedureRepository;
    }

    public void save(SpaEmployeeProcedure spaEmployeeProcedure) {
        spaEmployeeProcedureRepository.save(spaEmployeeProcedure);
    }

    public List<SpaEmployeeProcedure> getAll() {
        return spaEmployeeProcedureRepository.findAll();
    }

}
