package isr.naya.admiralproj.service;

import isr.naya.admiralproj.dto.ClientDto;
import isr.naya.admiralproj.model.Client;
import isr.naya.admiralproj.model.Project;
import isr.naya.admiralproj.repository.ClientRepository;
import isr.naya.admiralproj.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static isr.naya.admiralproj.util.ValidationUtil.checkNotFound;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;
    private ProjectRepository projectRepository;

    @CacheEvict(value = {"clients", "projects"}, allEntries = true)
    @Override
    @Transactional
    public Client save(@NonNull Client client) {
        return clientRepository.save(client);
    }

    @Cacheable(value = "clients", key = "getMethodName()")
    @Override
    public List<Client> getAll() {
        return clientRepository.getAllWithAddressesAndPhones();
    }

    @Cacheable(value = "clients", key = "getMethodName() + #id")
    @Override
    public ClientDto get(@NonNull Integer id) {
        Client withAddressesAndPhones = clientRepository.getWithAddressesAndPhones(id);
        List<Project> allByClientId = projectRepository.findAllByClientId(id);
        allByClientId.forEach(project -> project.getWorkAgreements().forEach(workAgreement -> workAgreement.setWorkUnits(Collections.emptyList())));
        return new ClientDto(checkNotFound(withAddressesAndPhones, id, Client.class), allByClientId);
    }

    @CacheEvict(value = {"clients", "projects"}, allEntries = true)
    @Transactional
    @Override
    public void delete(@NonNull Integer id) {
        checkNotFound(clientRepository.remove(id), id, Client.class);
    }
}
