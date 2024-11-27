package ega.spring.fitnessClubJdbc.generators;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class SpaGenerator {

    public static void main(String[] args) {
        int numberOfUsers = 13; // Количество пользователей
        int maxSessionsPerUser = 5; // Максимальное количество спа-процедур на пользователя

        List<String> allTimes = List.of("10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00");

        try (FileWriter writer = new FileWriter("generated_spa_records.sql")) {
            Random random = new Random();
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusMonths(1);

            int sessionCounter = 1;  // Счетчик спа-процедур

            for (int userId = 1; userId <= numberOfUsers; userId++) {
                int sessionsPerUser = random.nextInt(maxSessionsPerUser - 3 + 1) + 3; // От 3 до maxSessionsPerUser

                for (int j = 0; j < sessionsPerUser; j++) {
                    // Случайная дата спа-процедуры
                    LocalDate spaDate = startDate.plusDays(random.nextInt((int) (endDate.toEpochDay() - startDate.toEpochDay())));

                    // Случайное время спа-процедуры
                    String spaTime = allTimes.get(random.nextInt(allTimes.size()));

                    // Случайный сотрудник (например, от 1 до 5)
                    int employeeId = random.nextInt(4) + 1;

                    // Генерация записи на спа-процедуру
                    String spaSql = String.format(
                            "INSERT INTO spa_booking (user_id, procedure_id, date, status, employee_id, deleted, time) " +
                                    "VALUES (%d, %d, '%s', 'Зарегистрирован(а)', %d, false, '%s');\n",
                            userId, random.nextInt(3) + 1, spaDate, employeeId, spaTime
                    );

                    // Запись в файл
                    writer.write(spaSql);
                    sessionCounter++;
                }
            }

            System.out.println("SQL-файл для спа-процедур сгенерирован: generated_spa_records.sql");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
