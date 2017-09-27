package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.model.FrontalMessage;
import isr.naya.admiralproj.service.FrontalMessageService;
import isr.naya.admiralproj.web.controllers.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CorsRestController
@RequestMapping("/backend/admin/frontalmessages")
@AllArgsConstructor
public class AdminFrontalMessagesController {

    @Autowired
    private FrontalMessageService frontalMessageService;

    @GetMapping
    public List<FrontalMessage> getAllFrontalMessages(@AuthenticationPrincipal AuthorizedUser admin) {
        List<FrontalMessage> frontalMessages = frontalMessageService.getAll();
        log.info("Admin {} is retrieving all frontal messages", admin.getFullName());
        return frontalMessages;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FrontalMessage> saveFrontalMessage(@AuthenticationPrincipal AuthorizedUser admin,
                                                             @Valid @RequestBody FrontalMessage frontalMessage) {
        FrontalMessage saved = frontalMessageService.save(frontalMessage);
        log.info("Admin {} saved a new frontal message {} with id = {})", admin.getFullName(), saved.getHeader(), saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{frontalMessageId}")
    public void deleteProject(@AuthenticationPrincipal AuthorizedUser admin,
                              @PathVariable("frontalMessageId") Integer frontalMessageId) {
        frontalMessageService.delete(frontalMessageId);
        log.info("Admin {} is removing frontalMessage with id = {}", admin.getFullName(), frontalMessageId);
    }
}
