package ega.spring.fitnessClubJdbc.generators;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class TrainingGenerator {
    public static void main(String[] args) {
        int numberOfUsers = 13; // Количество пользователей
        int maxSessionsPerUser = 5; // Максимальное количество тренировок на пользователя

        List<String> allTimes = List.of("10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");

        try (FileWriter writer = new FileWriter("generated_training_records.sql")) {
            Random random = new Random();
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusMonths(1);

            int sessionCounter = 1;  // Счетчик тренировок

            for (int userId = 1; userId <= numberOfUsers; userId++) {
                int sessionsPerUser = random.nextInt(maxSessionsPerUser - 3 + 1) + 3; // От 3 до maxSessionsPerUser

                for (int j = 0; j < sessionsPerUser; j++) {
                    // Случайная дата тренировки
                    LocalDate trainingDate = startDate.plusDays(random.nextInt((int) (endDate.toEpochDay() - startDate.toEpochDay())));

                    // Случайное время тренировки
                    String trainingTime = allTimes.get(random.nextInt(allTimes.size()));

                    // Случайный тренер
                    int trainerId = random.nextInt(4) + 1; // Например, тренеры с ID от 1 до 5

                    // Генерация записи на тренировку
                    String trainingSql = String.format(
                            "INSERT INTO workout_booking (user_id, date, status, deleted, time, trainer_id) " +
                                    "VALUES (%d, '%s', 'Зарегистрирован(а)', false, '%s', %d);\n",
                            userId, trainingDate, trainingTime, trainerId
                    );

                    // Запись в файл
                    writer.write(trainingSql);
                    sessionCounter++;
                }
            }

            System.out.println("SQL-файл для тренировок сгенерирован: generated_training_records.sql");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
