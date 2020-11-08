package sbangularjs.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
public class MainController {

    @RequestMapping("/")
    public String welcome() {
        return "index";
    }

    @RequestMapping("/home")
    public String home() {
        return "home";
    }

    /*server.servlet.session.timeout=60m*/
    @RequestMapping("/login")
    public String login(@AuthenticationPrincipal User user,
                        @RequestParam(name = "message", required = false) String message, Model model,
                        HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("message", null);
        System.out.println(request.getSession().getId());
        Principal getUser = request.getUserPrincipal();
        /*System.out.println(123);
//        HttpServletResponse res = (HttpServletResponse) response;
        try {
            response.sendRedirect(request.getContextPath() + "/privateAccount");
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/
//        return "redirect:/privateAccount";
        return "login";
//        return "localhost:8082";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "login";
    }

    @RequestMapping("/privateAccount")
    public String privateAccount(@AuthenticationPrincipal User user,
                                 @RequestParam(name = "message", required = false) String message_privateAccount,
                                 Model model) {
        model.addAttribute("message_privateAccount", user.getUsername());
        return "privateAccount";
    }

    @RequestMapping("/{medicineId}")
    public String transitionMedicineId(@PathVariable Integer medicineId, Model model) {
        model.addAttribute("med", medicineId);
//        medicine();
        return "medicine";
//        return "redirect:/medicine";
    }

    @RequestMapping("/search")
    public String search() {
        return "search";
    }

    /*@RequestMapping("/help")
    public String help() {
        return "help";
    }*/


}