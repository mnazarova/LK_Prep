package sbangularjs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.model.Client;
import sbangularjs.model.Role;
import sbangularjs.model.User;
import sbangularjs.repository.ClientRepository;
import sbangularjs.repository.UserRepository;

import java.util.Collections;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    private ClientRepository clientRepository;

    @RequestMapping("/registration")
    public String registration(@RequestParam(name = "message", required = false) String message, Model model) {
        model.addAttribute("message", null);
        return "registration";
    }

    /*@RequestMapping(value = "/addUser",
            method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    @ResponseBody*/
    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        User userFromDB = userRepository.findByUsername(user.getUsername());
        if(userFromDB != null) {
            model.addAttribute("error", "Такой логин уже существует.");
            return "registration";
        }
//        Client client = new Client();
//        Client newClient = clientRepository.save(client);

//        User newUser = new User();
//        newUser.setUsername(user.getUsername());
//        newUser.setPassword(user.getPassword());

        user.setActive(true);
//        user.setClientId(newClient.getId());

        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);


        model.addAttribute("success", "Вы успешно зарегистрировались!");
        return "registration";

//        model.addAttribute("message", "Вы успешно зарегистрировались!");
//        return "redirect:/login";
    }
}
