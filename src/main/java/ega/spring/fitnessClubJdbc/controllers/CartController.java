package ega.spring.fitnessClubJdbc.controllers;

import ega.spring.fitnessClubJdbc.models.Order;
import ega.spring.fitnessClubJdbc.models.Person;
import ega.spring.fitnessClubJdbc.models.Product;
import ega.spring.fitnessClubJdbc.security.PersonDetails;
import ega.spring.fitnessClubJdbc.services.OrderService;
import ega.spring.fitnessClubJdbc.services.PersonDetailsService;
import ega.spring.fitnessClubJdbc.services.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class CartController {

    private final ProductService productService;
    private final OrderService orderService;
    private final PersonDetailsService personDetailsService;

    public CartController(ProductService productService, OrderService orderService, PersonDetailsService personDetailsService) {
        this.productService = productService;
        this.orderService = orderService;
        this.personDetailsService = personDetailsService;
    }

    // Главная страница магазина с товарами
    @GetMapping
    public String viewShop(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "shop/shop";  // Страница с товарами
    }

    // Просмотр корзины
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model, Principal principal) {
        // Попробуем получить объект заказа из сессии
        Order order = (Order) session.getAttribute("order");

        // Если заказа нет, создаем новый
        if (order == null) {
            order = new Order();
            session.setAttribute("order", order);
        }

        model.addAttribute("order", order);

        if (principal != null) {
            UserDetails userDetails = personDetailsService.loadUserByUsername(principal.getName());
            Person currentUser = ((PersonDetails) userDetails).getPerson();
            model.addAttribute("currentUser", currentUser);
        }

        return "shop/cart"; // Возвращаем страницу корзины
    }


    // Добавление товара в корзину
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam int productId, @RequestParam int quantity, HttpSession session) {
        Product product = productService.findById(productId);

        Order order = (Order) session.getAttribute("order");
        if (order == null) {
            order = new Order();
            session.setAttribute("order", order);
        }

        orderService.addProductToOrder(order, product, quantity);
        session.setAttribute("order", order);
        return "redirect:/shop/cart";  // Возврат к странице корзины
    }

    // Удаление товара из корзины
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam int productId, HttpSession session) {
        Order order = (Order) session.getAttribute("order");  // Получаем заказ из сессии
        if (order != null) {
            // Ищем и удаляем товар из списка orderItems в заказе
            order.getOrderItems().removeIf(item -> item.getProduct().getId() == productId);

            // Обновляем итоговую цену после удаления товара
            order.updateTotalPrice();

            // Сохраняем обновленный заказ в сессии
            session.setAttribute("order", order);
        }
        return "redirect:/shop/cart";   // Возврат к странице корзины
    }

    // Оформление заказа
    @PostMapping("/cart/checkout")
    public String checkout(@RequestParam int userId, HttpSession session, RedirectAttributes redirectAttributes) {
        Person user = personDetailsService.getUserById(userId);

        Order order = (Order) session.getAttribute("order");
        if (order == null || order.getOrderItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Корзина пуста. Добавьте товары в заказ.");
            return "redirect:/shop/cart";
        }

        order.setUser(user);
        orderService.processOrder(order);
        session.removeAttribute("order");  // Очистка корзины после оформления заказа

        redirectAttributes.addFlashAttribute("success", "Заказ успешно оформлен!");
        return "redirect:/shop";  // Возврат к главной странице магазина
    }
}
