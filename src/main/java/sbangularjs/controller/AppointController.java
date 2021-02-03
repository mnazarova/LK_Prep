package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.model.Group;
import sbangularjs.repository.GroupRepository;

import java.util.List;

@Controller
@PreAuthorize("hasAuthority('SECRETARY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class AppointController {

    private GroupRepository groupRepository;

    @PatchMapping("/getGroupListByActiveIsTrue")
    public ResponseEntity getGroupListByActiveIsTrue() {
        List<Group> groupList = groupRepository.getGroupListByActiveIsTrue();
        if (groupList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(groupList, HttpStatus.OK);
    }

    @PatchMapping("/selectedGroup")
    public ResponseEntity selectedGroup(@RequestParam Long groupId) {
        Group group = groupRepository.findGroupById(groupId);
        if (group == null)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You many decide to return HttpStatus.NOT_FOUND
        return new ResponseEntity<>(group, HttpStatus.OK);
    }
}
