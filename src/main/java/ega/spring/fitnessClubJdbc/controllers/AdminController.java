package ega.spring.fitnessClubJdbc.controllers;

import ega.spring.fitnessClubJdbc.dto.EmployeeRegistrationDto;
import ega.spring.fitnessClubJdbc.models.*;
import ega.spring.fitnessClubJdbc.services.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;
    private final PersonDetailsService personService;
    private final TrainerService trainerService;
    private final BookingService bookingService;
    private final SpaBookingService spaBookingService;
    private final OrderService orderService;
    private final SpaEmployeeService spaEmployeeService;
    private final AnalysisService analysisService;

    public AdminController(PersonDetailsService personService, TrainerService trainerService, BookingService bookingService, SpaBookingService spaBookingService, OrderService orderService, SpaEmployeeService spaEmployeeService, AnalysisService analysisService) {
        this.personService = personService;
        this.trainerService = trainerService;
        this.bookingService = bookingService;
        this.spaBookingService = spaBookingService;
        this.orderService = orderService;
        this.spaEmployeeService = spaEmployeeService;
        this.analysisService = analysisService;
    }

    @GetMapping("/admin")
    public String showAdminDashboard(Model model) {
        model.addAttribute("users", personService.findAll());
        model.addAttribute("trainers", trainerService.getAllTrainers());
        model.addAttribute("spaEmployees", spaEmployeeService.getAllEmployees());
        model.addAttribute("bookings", bookingService.getAllBookings());
        model.addAttribute("spaBookings", spaBookingService.getAllBookings());
        model.addAttribute("orders", orderService.getAll());
        model.addAttribute("employee", new EmployeeRegistrationDto());

        model.addAttribute("popularTimes", analysisService.getPopularTimes());
        model.addAttribute("popularSpaTimes", analysisService.getPopularSpaTimes());
        model.addAttribute("popularTrainers", analysisService.getPopularTrainers());
        model.addAttribute("popularSpaEmployees", analysisService.getPopularSpaEmployees());
        model.addAttribute("popularProducts", analysisService.getPopularProducts());

        return "admin";
    }

    @PostMapping("/user/delete")
    public String deleteUser(@RequestParam int userId) {
        personService.deleteUserById(userId);
        return "redirect:/admin";
    }

    @PostMapping("/user/edit")
    public String editUser(@RequestParam int userId, @RequestParam String firstName, Model model) {
        if (firstName == null || firstName.trim().isEmpty()) {
            model.addAttribute("error", "Имя не должно быть пустым.");
            return "admin";
        }

        Person user = personService.getUserById(userId);
        if (user != null) {
            user.setFirst_name(firstName);
            personService.updateUser(userId, user);
        } else {
            model.addAttribute("error", "Пользователь не найден.");
        }

        return "redirect:/admin";
    }

    @PostMapping("/spaEmployee/delete")
    public String deleteSpaEmployee(@RequestParam int spaEmployeeId) {
        spaEmployeeService.deleteSpaEmployeeById(spaEmployeeId);
        return "redirect:/admin";
    }

    @PostMapping("/spaEmployee/edit")
    public String editSpaEmployee(@RequestParam int spaEmployeeId,
                                  @RequestParam String name,
                                  Model model) {
        if (name == null || name.trim().isEmpty()) {
            model.addAttribute("error", "Имя не должно быть пустым.");
            return "admin";
        }

        SpaEmployee existingEmployee = spaEmployeeService.getEmployeeById(spaEmployeeId);
        if (existingEmployee == null) {
            model.addAttribute("error", "Сотрудник СПА не найден.");
            return "admin";
        }

        existingEmployee.setName(name);
        spaEmployeeService.updateEmployee(spaEmployeeId, existingEmployee);

        return "redirect:/admin";
    }

    @PostMapping("/trainer/delete")
    public String deleteTrainer(@RequestParam int trainerId) {
        trainerService.deleteTrainerById(trainerId);
        return "redirect:/admin";
    }

    @PostMapping("/trainer/edit")
    public String editTrainer(@RequestParam int trainerId, @RequestParam String firstName, Model model) {
        if (firstName == null || firstName.trim().isEmpty()) {
            model.addAttribute("error", "Имя не должно быть пустым.");
            return "admin";
        }

        Trainer trainer = trainerService.getTrainerById(trainerId);
        if (trainer != null) {
            trainer.setName(firstName);
            trainerService.updateTrainer(trainerId, trainer);
        } else {
            model.addAttribute("error", "Тренер не найден.");
        }

        return "redirect:/admin";
    }

    @PostMapping("/booking/delete")
    public String deleteBooking(@RequestParam int bookingId) {
        bookingService.deleteBookingById(bookingId);
        return "redirect:/admin";
    }

    @PostMapping("/booking/edit")
    public String editBooking(@RequestParam int bookingId,
                              @RequestParam String details,
                              Model model) {
        if (details == null || details.trim().isEmpty()) {
            model.addAttribute("error", "Детали не должны быть пустыми.");
            return "admin";
        }

        GymBooking booking = bookingService.getBookingById(bookingId);
        if (booking != null) {
            booking.setStatus(details);
            bookingService.updateBooking(bookingId, booking);
        } else {
            model.addAttribute("error", "Бронирование не найдено.");
        }

        return "redirect:/admin";
    }

    @PostMapping("/spaBooking/delete")
    public String deleteSpaBooking(@RequestParam int bookingId) {
        spaBookingService.deleteById(bookingId);
        return "redirect:/admin";
    }

    @PostMapping("/spaBooking/edit")
    public String editSpaBooking(@RequestParam int bookingId,
                              @RequestParam String details,
                              Model model) {
        if (details == null || details.trim().isEmpty()) {
            model.addAttribute("error", "Детали не должны быть пустыми.");
            return "admin";
        }

        SpaBooking booking = spaBookingService.getBookingById(bookingId);
        if (booking != null) {
            booking.setStatus(details);
            spaBookingService.updateBooking(bookingId, booking);
        } else {
            model.addAttribute("error", "Бронирование не найдено.");
        }

        return "redirect:/admin";
    }

    @PostMapping("/order/delete")
    public String deleteOrder(@RequestParam int orderId) {
        orderService.deleteOrderById(orderId);
        return "redirect:/admin";
    }

    @PostMapping("/order/edit")
    public String editOrder(@RequestParam int orderId, @RequestParam String newDetails, Model model) {
        if (newDetails == null || newDetails.trim().isEmpty()) {
            model.addAttribute("error", "Статус не должен быть пустым.");
            return "admin";
        }

        Order order = orderService.getOrderById(orderId);
        if (order != null) {
            order.setStatus(newDetails);
            orderService.updateOrder(orderId, order);
        } else {
            model.addAttribute("error", "Заказ не найден.");
        }

        return "redirect:/admin";
    }

    @PostMapping("/admin/register-employee")
    public String registerEmployee(
            @Valid EmployeeRegistrationDto employeeDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Пожалуйста, исправьте ошибки в форме.");
            return "admin";
        }

        try {
            if ("SPA".equalsIgnoreCase(employeeDto.getRole())) {
                SpaEmployee spaEmployee = new SpaEmployee();
                spaEmployee.setName(employeeDto.getName());
                spaEmployee.setUsername(employeeDto.getEmail());
                spaEmployee.setEmail(employeeDto.getEmail());
                spaEmployee.setSpecialization(employeeDto.getSpecialization());
                spaEmployee.setExperience(employeeDto.getExperience());
                spaEmployee.setBio(employeeDto.getBio());
                adminService.register(spaEmployee);
            } else if ("TRAINER".equalsIgnoreCase(employeeDto.getRole())) {
                Trainer trainer = new Trainer();
                trainer.setName(employeeDto.getName());
                trainer.setUsername(employeeDto.getEmail());
                trainer.setEmail(employeeDto.getEmail());
                trainer.setSpecialization(employeeDto.getSpecialization());
                trainer.setExperience(employeeDto.getExperience());
                trainer.setBio(employeeDto.getBio());
                adminService.register(trainer);
            } else {
                model.addAttribute("error", "Неверная роль выбранного сотрудника.");
                return "admin";
            }

            model.addAttribute("success", "Сотрудник успешно зарегистрирован!");
            model.addAttribute("employee", new EmployeeRegistrationDto());
        } catch (Exception e) {
            model.addAttribute("error", "Произошла ошибка при регистрации сотрудника.");
        }

        return "admin";
    }

    @GetMapping("/analysis/export")
    public void exportAnalysisData(
            @RequestParam String exportTypes, // Список через запятую: users,orders,bookings
            HttpServletResponse response) throws IOException {


        // Создаем Excel Workbook
        Workbook workbook = new XSSFWorkbook();

        // Разбираем, какие данные экспортировать
        List<String> exportTypeList = Arrays.asList(exportTypes.split(","));

        // Экспорт пользователей
        if (exportTypeList.contains("users-export")) {
            Sheet userSheet = workbook.createSheet("Пользователи");
            List<Person> users = personService.findAll();
            fillUserSheet(userSheet, users);
        }

        // Экспорт заказов
        if (exportTypeList.contains("orders-export")) {
            Sheet orderSheet = workbook.createSheet("Заказы");
            List<Order> orders = orderService.getAllOrders();
            fillOrderSheet(orderSheet, orders);
        }

        // Экспорт бронирований
        if (exportTypeList.contains("bookings-export")) {
            Sheet bookingSheet = workbook.createSheet("Бронирования тренировок");
            List<GymBooking> bookings = bookingService.getAllBookings();
            fillBookingSheet(bookingSheet, bookings);
        }

        if (exportTypeList.contains("spa-bookings-export")) {
            Sheet bookingSheet = workbook.createSheet("Бронирования Спа");
            List<SpaBooking> bookings = spaBookingService.getAllBookings();
            fillSpaBookingSheet(bookingSheet, bookings);
        }

        // Устанавливаем заголовки ответа
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=export_data.xlsx");

        // Записываем Excel-файл в поток
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private void fillUserSheet(Sheet sheet, List<Person> users) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Имя");
        header.createCell(2).setCellValue("Email");
        header.createCell(3).setCellValue("Роль");

        int rowIndex = 1;
        for (Person user : users) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(user.getId());
            row.createCell(1).setCellValue(user.getFirst_name());
            row.createCell(2).setCellValue(user.getEmail());
            row.createCell(3).setCellValue(user.getRole());
        }
    }

    private void fillBookingSheet(Sheet sheet, List<GymBooking> bookings) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Дата");
        header.createCell(2).setCellValue("Время");
        header.createCell(3).setCellValue("Статус");

        int rowIndex = 1;
        for (GymBooking booking : bookings) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(booking.getId());
            row.createCell(1).setCellValue(booking.getDate().toString());
            row.createCell(2).setCellValue(booking.getTime().toString());
            row.createCell(3).setCellValue(booking.getStatus());
        }
    }

    private void fillSpaBookingSheet(Sheet sheet, List<SpaBooking> bookings) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Дата");
        header.createCell(2).setCellValue("Время");
        header.createCell(3).setCellValue("Статус");

        int rowIndex = 1;
        for (SpaBooking booking : bookings) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(booking.getId());
            row.createCell(1).setCellValue(booking.getDate().toString());
            row.createCell(2).setCellValue(booking.getTime().toString());
            row.createCell(3).setCellValue(booking.getStatus());
        }
    }

    private void fillOrderSheet(Sheet sheet, List<Order> orders) {
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Дата");
        header.createCell(2).setCellValue("Время");
        header.createCell(3).setCellValue("Сумма");
        header.createCell((4)).setCellValue("Статус");

        int rowIndex = 1;
        for (Order order : orders) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(order.getId());
            row.createCell(1).setCellValue(order.getDate().toString());
            row.createCell(2).setCellValue(order.getTime().toString());
            row.createCell(3).setCellValue(order.getTotal_price());
            row.createCell(4).setCellValue(order.getStatus());
        }
    }


}