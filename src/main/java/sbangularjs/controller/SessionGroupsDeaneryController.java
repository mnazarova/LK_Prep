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
import sbangularjs.model.Deanery;
import sbangularjs.model.User;
import sbangularjs.repository.DeaneryRepository;
import sbangularjs.repository.GroupRepository;

@Controller
@PreAuthorize("hasAuthority('DEANERY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class SessionGroupsDeaneryController {
    private DeaneryRepository deaneryRepository;
    private GroupRepository groupRepository;

    @PatchMapping("/getSessionGroupsByDeanery")
    public ResponseEntity getSessionGroupsByDeanery(@AuthenticationPrincipal User user, @RequestParam Long groupId) { // groupId == null - все группы, иначе одна выбранная пользователем группа
        if (groupId != null)
            return new ResponseEntity<>(groupRepository.findGroupById(groupId), HttpStatus.OK);

        Deanery curDeanery = deaneryRepository.findByUsername(user.getUsername());
        if (curDeanery == null) return new ResponseEntity<>(0, HttpStatus.CONFLICT);

        return new ResponseEntity<>(groupRepository.findGroupsByDeaneryIdOrderByActiveDescCurSemesterAsc(curDeanery.getId()/*, Sort.by(Sort.Direction.DESC, "active")*/), HttpStatus.OK);
    }

}
