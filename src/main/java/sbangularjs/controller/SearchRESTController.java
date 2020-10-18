/*
package sbangularjs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import sbangularjs.controller.MainController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sbangularjs.DTO.GroupDto;
import sbangularjs.model.CurrentOrder;
import sbangularjs.model.Medicine;
import sbangularjs.model.User;
import sbangularjs.repository.CurrentOrderRepository;
import sbangularjs.repository.GroupRepository;
import sbangularjs.repository.MedicineRepository;
import sbangularjs.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class SearchRESTController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private CurrentOrderRepository currentOrderRepository;

    */
/*@RequestMapping(value = "/getAllGroups", //
            method = RequestMethod.GET, //
            produces = { MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
//    @GetMapping("/getDataUser")
    public List<GroupDto> getAllGroups() {
        return groupRepository.findAllDTO();
    }*//*


    @RequestMapping(value = "/getMedicines", //
            method = RequestMethod.GET, //
            produces = { MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public List<Medicine> getMedicines() {
        return medicineRepository.findAll();
    }

    @RequestMapping(value = "/findMedicines", //
            method = RequestMethod.PUT, //
            produces = { MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public List<Medicine> findMedicines(@RequestBody Medicine medicine) {
        List<Medicine> medicines = new ArrayList<>();
        if(medicine.getGroupId() != null && medicine.getName() != null)
            medicines = medicineRepository.findAllByNameContainingIgnoreCaseAndGroupId(medicine.getName(), medicine.getGroupId());
        else
            if(medicine.getGroupId() != null)
                medicines = medicineRepository.findAllByGroupId(medicine.getGroupId());
              else
                if(medicine.getName() != null)
                    medicines = medicineRepository.findAllByNameContainingIgnoreCase(medicine.getName());
                else
                    medicines = medicineRepository.findAll();
        return medicines;
    }



    @RequestMapping(value = "/addToBasket", //
            method = RequestMethod.PUT, //
            produces = { MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public List<Medicine> addToBasket(@AuthenticationPrincipal User user, @RequestBody Integer medicineId) {
        CurrentOrder currentOrder = currentOrderRepository.findByUserIdAndMedicineId(user.getId(), medicineId);
        if(currentOrder == null) {
            CurrentOrder tmp = new CurrentOrder(medicineId, 1, user.getId());
            currentOrderRepository.save(tmp);
        }
        else {
            currentOrder.setCount(currentOrder.getCount() + 1);
            currentOrderRepository.save(currentOrder);
        }
        Optional<Medicine> optionalMedicine = medicineRepository.findById(medicineId);
        if(!optionalMedicine.isPresent())
            return null;
        Medicine medicine = optionalMedicine.get();
        medicine.setQuantity(medicine.getQuantity()-1);
        medicineRepository.save(medicine);

        return medicineRepository.findAll();
    }

    */
/*@RequestMapping(value = "/transitionMedicineId", //
            method = RequestMethod.PUT, //
            produces = { MediaType.APPLICATION_JSON_VALUE, //
                    MediaType.APPLICATION_XML_VALUE })
    @ResponseBody
    public String transitionMedicineId(@RequestBody Integer medicineId, Model model) {
        model.addAttribute("med", medicineId);
        return "redirect:/medicine";
//        return "redirect:/medicine";
    }*//*

    */
/*@RequestMapping(value = "/updateUser", //
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
    }*//*

}
*/
