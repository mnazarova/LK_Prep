package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sbangularjs.model.Role;
import sbangularjs.model.User;
import sbangularjs.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Set;

@Controller
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class MainController {
    private UserRepository userRepository;

    @RequestMapping("/")
    public String home(@AuthenticationPrincipal User user) {
        return user == null ? "login" : "index";
    }

    @RequestMapping("/home")
    public String home() {
        return "login";
    }

    /*@RequestMapping("/login")
    public String login() {
        return "login";
    }*/

    @RequestMapping("/login2")
    public String login(@AuthenticationPrincipal User user,
                        @RequestParam(name = "message", required = false) String message, Model model,
                        HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("message", null);
        //System.out.println(request.getSession().getId());
        Principal getUser = request.getUserPrincipal();
        //   if (getUser == null)
//            return "redirect:/login"; // рекурсия

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

    @RequestMapping(value = "/getDataUser", method = RequestMethod.GET,
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public Set<Role> getDataUser(@AuthenticationPrincipal User user) {
        return userRepository.findByUsername(user.getUsername(), Sort.by(Sort.Direction.ASC, "roles")).getRoles();
    }
}