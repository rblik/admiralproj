package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.dto.ClientDto;
import isr.naya.admiralproj.model.Client;
import isr.naya.admiralproj.service.ClientService;
import isr.naya.admiralproj.web.controllers.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CorsRestController
@RequestMapping("/backend/admin/clients")
@AllArgsConstructor
public class AdminClientsController {

    private ClientService clientService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> saveClient(@AuthenticationPrincipal AuthorizedUser admin,
                                             @Valid @RequestBody Client client) {
        Client saved = clientService.save(client);
        log.info("Admin {} saved a new client {} with id = {}", admin.getFullName(), saved.getName(), saved.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Client> getAllClients(@AuthenticationPrincipal AuthorizedUser admin) {
        List<Client> clients = clientService.getAll();
        log.info("Admin {} is retrieving all clients", admin.getFullName());
        return clients;
    }

    @GetMapping("/{clientId}")
    public ClientDto getClient(@AuthenticationPrincipal AuthorizedUser admin,
                               @PathVariable("clientId") Integer clientId) {
        ClientDto client = clientService.get(clientId);
        log.info("Admin {} is retrieving client with id = {}", admin.getFullName(), clientId);
        return client;
    }

    @DeleteMapping("/{clientId}")
    public void deleteProject(@AuthenticationPrincipal AuthorizedUser admin,
                              @PathVariable("clientId") Integer clientId) {
        clientService.delete(clientId);
        log.info("Admin {} is removing client with id = {}", admin.getFullName(), clientId);
    }
}
