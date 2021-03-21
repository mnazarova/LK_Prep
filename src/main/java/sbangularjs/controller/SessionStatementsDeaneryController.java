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
import sbangularjs.DTO.SessionDTO;
import sbangularjs.model.Deanery;
import sbangularjs.model.User;
import sbangularjs.repository.DeaneryRepository;
import sbangularjs.repository.GroupRepository;
import sbangularjs.repository.SessionSheetRepository;

import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('DEANERY')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class SessionStatementsDeaneryController {
    private DeaneryRepository deaneryRepository;
    private GroupRepository groupRepository;
    private SessionSheetRepository sessionSheetRepository;

    @PatchMapping("/getSessionStatementsByDeanery")
    public ResponseEntity getSessionStatementsByDeanery(@AuthenticationPrincipal User user, @RequestParam List<Long> groupIds) { // groupId == null - все группы, иначе одна выбранная пользователем группа
        Deanery curDeanery = deaneryRepository.findByUsername(user.getUsername());
        if (curDeanery == null) return new ResponseEntity<>(0, HttpStatus.CONFLICT);
        if (groupIds.size() == 0) groupIds = groupRepository.findGroupIdsByDeaneryId(curDeanery.getId());

        List<Long> sessionSheetIds = sessionSheetRepository.findSessionSheetIdsByDeadlineAndGroupIds(new Date(), groupIds);
        if(sessionSheetIds.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<SessionDTO> sessionDTOList = sessionSheetRepository.findAllSessionDTOBySessionSheetIds(sessionSheetIds);
        if (sessionDTOList.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(sessionDTOList, HttpStatus.OK);
    }

}
