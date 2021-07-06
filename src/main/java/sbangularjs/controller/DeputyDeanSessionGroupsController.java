package sbangularjs.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sbangularjs.model.DeputyDean;
import sbangularjs.model.User;
import sbangularjs.repository.DeputyDeanRepository;
import sbangularjs.repository.GroupRepository;

@Controller
@PreAuthorize("hasAuthority('DEPUTY_DEAN')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class DeputyDeanSessionGroupsController {
    private DeputyDeanRepository deputyDeanRepository;
    private GroupRepository groupRepository;

    @PatchMapping("/getSessionGroupsByDeputyDean")
    public ResponseEntity getSessionGroupsByDeanery(@AuthenticationPrincipal User user, @RequestParam Long groupId) { // groupId == null - все группы, иначе одна выбранная пользователем группа
        if (groupId != null)
            return new ResponseEntity<>(groupRepository.findGroupById(groupId), HttpStatus.OK);

        DeputyDean deputyDean = deputyDeanRepository.findByUsername(user.getUsername());
        if (deputyDean == null) return new ResponseEntity<>(0, HttpStatus.CONFLICT);

        return new ResponseEntity<>(groupRepository.findGroupsByDeputyDeanIdOrderByActiveDescCurSemesterAsc(deputyDean.getId()), HttpStatus.OK);
    }

}
