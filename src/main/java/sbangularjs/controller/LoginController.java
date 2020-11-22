package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sbangularjs.model.Role;
import sbangularjs.model.User;
import sbangularjs.repository.UserRepository;

import java.util.Set;

@Controller
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class LoginController {
    private UserRepository userRepository;

    /*@GetMapping("/getDataUser")
    public Set<Role> getDataUser(@AuthenticationPrincipal User user) {
        return userRepository.findByUsername(user.getUsername()).getRoles();
    }*/

    @RequestMapping(value = "/getDataUser", //
            method = RequestMethod.GET, //
            produces = { MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
//    @GetMapping("/getDataUser") // не будет работать из-за возвращаемого Set<Role>
    public Set<Role> getDataUser(@AuthenticationPrincipal User user) {
        return userRepository.findByUsername(user.getUsername(), Sort.by(Sort.Direction.ASC, "roles")).getRoles();
    }

}
