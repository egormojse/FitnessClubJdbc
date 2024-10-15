package ega.spring.fitnessClubJdbc.services;

import ega.spring.fitnessClubJdbc.repositories.TrainerRepository;
import ega.spring.fitnessClubJdbc.models.Trainer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;

    public TrainerService(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    public Trainer getTrainerById(int id) {
        Trainer trainer = trainerRepository.findById(id);
        if (trainer == null) {
            throw new RuntimeException("Trainer not found");
        }
        return trainer;
    }
}
