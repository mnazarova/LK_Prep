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
import sbangularjs.model.DeputyDean;
import sbangularjs.model.User;
import sbangularjs.repository.DeputyDeanRepository;
import sbangularjs.repository.GroupRepository;
import sbangularjs.repository.SessionSheetRepository;

import java.util.Date;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('DEPUTY_DEAN')")
@AllArgsConstructor(onConstructor = @_(@Autowired))
public class DeputyDeanSessionStatementsController {
    private DeputyDeanRepository deputyDeanRepository;
    private GroupRepository groupRepository;
    private SessionSheetRepository sessionSheetRepository;

    @PatchMapping("/getSessionStatementsByDeputyDean")
    public ResponseEntity getSessionStatementsByDeputyDean(@AuthenticationPrincipal User user, @RequestParam List<Long> groupIds) { // groupId == null - все группы, иначе одна выбранная пользователем группа
        DeputyDean deputyDean = deputyDeanRepository.findByUsername(user.getUsername());
        if (deputyDean == null) return new ResponseEntity<>(0, HttpStatus.CONFLICT);
        if (groupIds.size() == 0) groupIds = groupRepository.findGroupIdsByDeputyDeanId(deputyDean.getId());

        List<Long> sessionSheetIds = sessionSheetRepository.findSessionSheetIdsByDeadlineAndGroupIds(new Date(), groupIds);
        if(sessionSheetIds.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        List<SessionDTO> sessionDTOList = sessionSheetRepository.findAllSessionDTOBySessionSheetIds(sessionSheetIds);
        if (sessionDTOList.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(sessionDTOList, HttpStatus.OK);
    }

}
