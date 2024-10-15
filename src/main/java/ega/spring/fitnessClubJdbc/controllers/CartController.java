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

    @GetMapping
    public String viewShop(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "shop/shop";
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam int productId, @RequestParam int quantity, HttpSession session) {
        Product product = productService.findById(productId);

        Order order = (Order) session.getAttribute("order");
        if (order == null) {
            order = new Order();
            session.setAttribute("order", order);
        }

        orderService.addProductToOrder(order, product, quantity);
        return "redirect:/shop";
    }

    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        Order order = (Order) session.getAttribute("order");
        model.addAttribute("order", order);
        return "shop/cart";
    }

    @PostMapping("/checkout/submit")
    public String checkout(@RequestParam int userId, HttpSession session) {
        Person user = personDetailsService.getUserById(userId);
        System.out.println(user.getUsername() + " " + user.getId());

        Order order = (Order) session.getAttribute("order");
        if (order == null || order.getOrderItems().isEmpty()) {
            throw new IllegalStateException("Корзина пуста. Добавьте товары в заказ.");
        }

        order.setUser(user);
        System.out.println(order.getId() + " = " + "id");

        orderService.processOrder(order);

        session.removeAttribute("order");
        return "redirect:/shop";
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(Principal principal, Model model, HttpSession session) {
        if (principal != null) {
            UserDetails userDetails = personDetailsService.loadUserByUsername(principal.getName());
            Person currentUser = ((PersonDetails) userDetails).getPerson();

            model.addAttribute("currentUser", currentUser);
        }

        Order order = (Order) session.getAttribute("order");
        if (order != null) {
            model.addAttribute("order", order);
        } else {
            return "redirect:/shop?error=no_order";
        }

        return "shop/checkout";
    }
}
