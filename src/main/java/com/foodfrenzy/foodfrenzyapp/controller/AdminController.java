package com.foodfrenzy.foodfrenzyapp.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.foodfrenzy.foodfrenzyapp.count.Logic;
import com.foodfrenzy.foodfrenzyapp.entities.Admin;
import com.foodfrenzy.foodfrenzyapp.entities.Orders;
import com.foodfrenzy.foodfrenzyapp.entities.Product;
import com.foodfrenzy.foodfrenzyapp.entities.User;
import com.foodfrenzy.foodfrenzyapp.loginCredentials.AdminLogin;
import com.foodfrenzy.foodfrenzyapp.loginCredentials.UserLogin;
import com.foodfrenzy.foodfrenzyapp.services.AdminServices;
import com.foodfrenzy.foodfrenzyapp.services.OrderServices;
import com.foodfrenzy.foodfrenzyapp.services.ProductServices;
import com.foodfrenzy.foodfrenzyapp.services.UserServices;

@Controller
public class AdminController {

    @Autowired
    private UserServices services;

    @Autowired
    private AdminServices adminServices;

    @Autowired
    private ProductServices productServices;

    @Autowired
    private OrderServices orderServices;


    // =========================================================
    // ADMIN LOGIN
    // =========================================================

    @PostMapping("/adminLogin")
    public String adminLogin(
            @ModelAttribute("adminLogin") AdminLogin login,
            Model model,
            HttpSession session) {

        boolean valid =
                adminServices.validateAdminCredentials(
                        login.getEmail(),
                        login.getPassword()
                );

        if (valid) {

            session.setAttribute(
                    "loggedInAdmin",
                    login.getEmail()
            );

            return "redirect:/admin/services";
        }

        model.addAttribute(
                "error",
                "Invalid email or password"
        );

        return "Login";
    }


    // =========================================================
    // USER LOGIN
    // =========================================================

    @PostMapping("/userLogin")
    public String userLogin(
            @ModelAttribute("userLogin") UserLogin login,
            Model model,
            HttpSession session) {

        if (!services.validateLoginCredentials(
                login.getUserEmail(),
                login.getUserPassword())) {

            model.addAttribute(
                    "error2",
                    "Invalid email or password"
            );

            return "Login";
        }

        User user =
                services.getUserByEmail(
                        login.getUserEmail()
                );

        session.setAttribute(
                "loggedInUser",
                user
        );

        List<Orders> orders =
                orderServices.getOrdersForUser(user);

        model.addAttribute(
                "orders",
                orders
        );

        model.addAttribute(
                "name",
                user.getUname()
        );

        return "redirect:/products";
    }


    // =========================================================
    // LOGOUT
    // =========================================================

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }


    // =========================================================
    // ADMIN DASHBOARD
    // =========================================================

    @GetMapping("/admin/services")
    public String adminDashboard(
            Model model,
            HttpSession session) {

        Object loggedInAdmin =
                session.getAttribute(
                        "loggedInAdmin"
                );

        if (loggedInAdmin == null) {

            return "redirect:/login";
        }


        // -----------------------------------------------------
        // GET USERS
        // -----------------------------------------------------

        List<User> users =
                services.getAllUser();


        // -----------------------------------------------------
        // GET ADMINS
        // -----------------------------------------------------

        List<Admin> admins =
                adminServices.getAll();


        // -----------------------------------------------------
        // GET PRODUCTS
        // -----------------------------------------------------

        List<Product> products =
                productServices.getAllProducts();


        // -----------------------------------------------------
        // GET ORDERS
        // -----------------------------------------------------

        List<Orders> orders =
                orderServices.getOrders();


        // -----------------------------------------------------
        // TOTAL REVENUE
        // -----------------------------------------------------

        double totalRevenue = 0;

        for (Orders order : orders) {

            if (order != null) {

                totalRevenue +=
                        order.getTotalAmount();
            }
        }


        // -----------------------------------------------------
        // DASHBOARD STATISTICS
        // -----------------------------------------------------

        model.addAttribute(
                "totalUsers",
                users.size()
        );

        model.addAttribute(
                "totalAdmins",
                admins.size()
        );

        model.addAttribute(
                "totalProducts",
                products.size()
        );

        model.addAttribute(
                "totalOrders",
                orders.size()
        );

        model.addAttribute(
                "totalRevenue",
                totalRevenue
        );


        // -----------------------------------------------------
        // TABLE DATA
        // -----------------------------------------------------

        model.addAttribute(
                "users",
                users
        );

        model.addAttribute(
                "admins",
                admins
        );

        model.addAttribute(
                "products",
                products
        );

        model.addAttribute(
                "orders",
                orders
        );


        return "Admin_Page";
    }


    // =========================================================
    // ADD ADMIN
    // =========================================================

    @GetMapping("/addAdmin")
    public String addAdminPage() {

        return "Add_Admin";
    }


    @PostMapping("/addingAdmin")
    public String addAdmin(
            @ModelAttribute Admin admin) {

        adminServices.addAdmin(admin);

        return "redirect:/admin/services";
    }


    // =========================================================
    // UPDATE ADMIN
    // =========================================================

    @GetMapping("/updateAdmin/{id}")
    public String updateAdminPage(
            @PathVariable int id,
            Model model) {

        Admin admin =
                adminServices.getAdmin(id);

        model.addAttribute(
                "admin",
                admin
        );

        return "Update_Admin";
    }


    @PostMapping("/updateAdmin/{id}")
    public String updateAdmin(
            @ModelAttribute Admin admin,
            @PathVariable int id) {

        adminServices.update(
                admin,
                id
        );

        return "redirect:/admin/services";
    }


    // =========================================================
    // DELETE ADMIN
    // =========================================================

    @GetMapping("/deleteAdmin/{id}")
    public String deleteAdmin(
            @PathVariable int id) {

        adminServices.delete(id);

        return "redirect:/admin/services";
    }


    // =========================================================
    // ORDER PRODUCT
    // =========================================================

    @PostMapping("/product/order")
    public String orderProduct(
            @Valid @ModelAttribute Orders order,
            BindingResult result,
            Model model,
            HttpSession session) {

        if (result.hasErrors()) {

            return "BuyProduct";
        }


        // -----------------------------------------------------
        // GET LOGGED-IN USER
        // -----------------------------------------------------

        User loggedInUser =
                (User) session.getAttribute(
                        "loggedInUser"
                );

        if (loggedInUser == null) {

            return "redirect:/login";
        }


        // -----------------------------------------------------
        // ATTACH USER
        // -----------------------------------------------------

        order.setUser(
                loggedInUser
        );


        // -----------------------------------------------------
        // CALCULATE TOTAL
        // -----------------------------------------------------

        double totalAmount =
                Logic.countTotal(
                        order.getOrderPrice(),
                        order.getOrderQuantity()
                );

        order.setTotalAmount(
                totalAmount
        );


        // -----------------------------------------------------
        // ORDER DATE
        // -----------------------------------------------------

        order.setOrderDate(
                new Date()
        );


        // -----------------------------------------------------
        // INITIAL STATUS
        // -----------------------------------------------------

        order.setOrderStatus(
                "Pending"
        );


        // -----------------------------------------------------
        // SAVE ORDER
        // -----------------------------------------------------

        orderServices.saveOrder(
                order
        );


        model.addAttribute(
                "amount",
                totalAmount
        );


        return "Order_success";
    }


    // =========================================================
    // UPDATE ORDER STATUS
    // =========================================================

    @GetMapping("/admin/orderStatus/{id}")
    public String updateOrderStatus(
            @PathVariable int id,
            HttpSession session) {


        Object loggedInAdmin =
                session.getAttribute(
                        "loggedInAdmin"
                );

        if (loggedInAdmin == null) {

            return "redirect:/login";
        }


        Orders order =
                orderServices.getOrderById(id);


        if (order != null) {

            String status =
                    order.getOrderStatus();


            // -------------------------------------------------
            // Pending → Preparing
            // -------------------------------------------------

            if (status == null
                    || status.equals("Pending")) {

                order.setOrderStatus(
                        "Preparing"
                );
            }


            // -------------------------------------------------
            // Preparing → Out For Delivery
            // -------------------------------------------------

            else if (status.equals("Preparing")) {

                order.setOrderStatus(
                        "Out For Delivery"
                );
            }


            // -------------------------------------------------
            // Out For Delivery → Delivered
            // -------------------------------------------------

            else if (status.equals("Out For Delivery")) {

                order.setOrderStatus(
                        "Delivered"
                );
            }


            // -------------------------------------------------
            // SAVE
            // -------------------------------------------------

            orderServices.updateOrder(
                    order
            );
        }


        return "redirect:/admin/services";
    }


    // =========================================================
    // ADD PRODUCT PAGE
    // =========================================================

    @GetMapping("/addProduct")
    public String addProductPage() {

        return "Add_Product";
    }


    // =========================================================
    // SAVE PRODUCT
    // =========================================================

    @PostMapping("/addingProduct")
    public String addProduct(
            @RequestParam("pname") String name,
            @RequestParam("pprice") double price,
            @RequestParam("pimage") String image,
            @RequestParam("pcategory") String category) {

        Product product = new Product();

        product.setName(name);

        product.setPrice(price);

        product.setImage(image);

        product.setCategory(category);

        productServices.addProduct(product);

        return "redirect:/admin/services";
    }

// =========================================================
// UPDATE PRODUCT PAGE
// =========================================================

    @GetMapping("/updateProduct/{id}")
    public String updateProductPage(
            @PathVariable int id,
            Model model,
            HttpSession session) {

        // Check admin login
        Object loggedInAdmin =
                session.getAttribute("loggedInAdmin");

        if (loggedInAdmin == null) {
            return "redirect:/login";
        }


        // Find product
        Product product =
                productServices.getProductById(id);

        if (product == null) {
            return "redirect:/admin/services";
        }


        model.addAttribute(
                "product",
                product
        );

        return "Update_Product";
    }


// =========================================================
// UPDATE PRODUCT
// =========================================================

    @PostMapping("/updateProduct/{id}")
    public String updateProduct(
            @PathVariable int id,
            @RequestParam("pname") String name,
            @RequestParam("pprice") double price,
            @RequestParam("pimage") String image,
            @RequestParam("pcategory") String category,
            HttpSession session) {

        // Check admin login
        Object loggedInAdmin =
                session.getAttribute("loggedInAdmin");

        if (loggedInAdmin == null) {
            return "redirect:/login";
        }


        // Find existing product
        Product product =
                productServices.getProductById(id);

        if (product != null) {

            product.setName(name);

            product.setPrice(price);

            product.setImage(image);

            product.setCategory(category);


            // Save changes
            productServices.updateProduct(
                    product
            );
        }

        return "redirect:/admin/services";
    }


// =========================================================
// DELETE PRODUCT
// =========================================================

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(
            @PathVariable int id,
            HttpSession session) {

        // Check admin login
        Object loggedInAdmin =
                session.getAttribute("loggedInAdmin");

        if (loggedInAdmin == null) {
            return "redirect:/login";
        }


        // Delete product
        productServices.deleteProduct(id);

        return "redirect:/admin/services";
    }

}