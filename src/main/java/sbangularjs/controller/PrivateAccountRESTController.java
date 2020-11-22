package sbangularjs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import sbangularjs.model.Role;
import sbangularjs.model.User;
import sbangularjs.repository.UserRepository;

import java.util.Set;

@RestController
public class PrivateAccountRESTController {
//    @Autowired
//    private UserRepository userRepository;

/*//    @RequestMapping("/privateAccount")
    @RequestMapping(value = "/privateAccount", //
            method = RequestMethod.GET, //
            produces = { MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public String privateAccount(@AuthenticationPrincipal User user,
            @RequestParam(name = "message", required = false) String message_privateAccount,
                                 Model model) {
        model.addAttribute("message_privateAccount", user.getUsername());
        return "privateAccount";
    }*/

    /*@RequestMapping(value = "/getDataUser", //
            method = RequestMethod.GET, //
            produces = { MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
//    @GetMapping("/getDataUser")
    public Set<Role> getDataUser(@AuthenticationPrincipal User user) {
        return userRepository.findByUsername(user.getUsername()).getRoles();
    }*/

    /*@RequestMapping(value = "/getDataClient",method = RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public Client getDataClient(@AuthenticationPrincipal User user) {
        Client client = clientRepository.findByUsername(user.getUsername());
        if(client == null) {
            Client newClient = new Client();
            newClient.setUsername(user.getUsername());
            clientRepository.save(newClient);
            return newClient;
        }
        return client;
    }

    @RequestMapping(value = "/getDataEmployee",method = RequestMethod.GET,produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public Employee getDataEmployee(@AuthenticationPrincipal User user) {
        Employee employee = employeeRepository.findByUsername(user.getUsername());
        if(employee == null) {
            Employee newEmployee = new Employee();
            newEmployee.setUsername(user.getUsername());
            employeeRepository.save(newEmployee);
            return newEmployee;
        }
        return employee;
    }

    @RequestMapping(value = "/updateClient", //
            method = RequestMethod.PUT, //
            produces = { MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public Client updateClient(@AuthenticationPrincipal User user, @RequestBody Client updateClient) {
        Client client = clientRepository.findByUsername(user.getUsername());
        client.setSurname(updateClient.getSurname());
        client.setName(updateClient.getName());
        client.setEmail(updateClient.getEmail());
        client.setPhone(updateClient.getPhone());
        *//*if(!user.getUsername().equals(updateClient.getUsername())) {
            client.setUsername(updateClient.getUsername());
            user.setUsername(updateClient.getUsername());
            userRepository.save(user);
        }*//*

        clientRepository.save(client);
        // LOGIN
//        System.out.println("(Service Side) Editing employee with Id: " + empForm.getEmpId());

        return client;
    }


    @RequestMapping(value = "/updateEmployee",method = RequestMethod.PUT,produces = { MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public Employee updateEmployee(@AuthenticationPrincipal User user, @RequestBody Employee updateEmployee) {
        Employee employee = employeeRepository.findByUsername(user.getUsername());
        employee.setSurname(updateEmployee.getSurname());
        employee.setName(updateEmployee.getName());
        employee.setPhone(updateEmployee.getPhone());
        *//*if(!user.getUsername().equals(updateClient.getUsername())) {
            client.setUsername(updateClient.getUsername());
            user.setUsername(updateClient.getUsername());
            userRepository.save(user);
        }*//*

        employeeRepository.save(employee);
        return employee;
    }

    @RequestMapping(value = "/updateUser", //
            method = RequestMethod.PUT, //
            produces = { MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public User updateUser(@AuthenticationPrincipal User user, @RequestBody User updateUser) {
        if(updateUser.getUsername() == null)
            return null; // Введите корректные данные в пол логин и/или пароль

        if(!user.getUsername().equals(updateUser.getUsername())) {
            if(userRepository.findByUsername(updateUser.getUsername()) == null) {
                //   return null; // Пользователь с таким логином уже существует
                Client client = clientRepository.findByUsername(user.getUsername());
                client.setUsername(updateUser.getUsername());
                clientRepository.save(client);
                user.setUsername(updateUser.getUsername());
                userRepository.save(user);
            }
        }

        if(updateUser.getPassword() != null && !updateUser.getPassword().equals(user.getPassword())) {
            user.setPassword(updateUser.getPassword());
            userRepository.save(user);
        }

        return user;
    }*/

    /*
    @RequestMapping(value = "/editClient",
            method = RequestMethod.POST,
            produces = { MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
//    @PostMapping("/addClient")
    public Client addClient(@RequestBody Client clientForm) {
        Client client = new Client(clientForm.getSurname(), clientForm.getName(),
                                   clientForm.getPhone(), clientForm.getEmail(),
                                   clientForm.getLogin(), clientForm.getPassword());
        return clientRepository.save(client);
    }
    */
}
