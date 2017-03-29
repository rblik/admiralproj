package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Client;
import isr.naya.admiralproj.repo.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static isr.naya.admiralproj.exception.ValidationUtil.checkNotFound;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    @Override
    public Client save(@NonNull Client client) {
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public List<Client> getAllWithProjects() {
        return clientRepository.getAllWithProjects();
    }

    @Override
    public Client get(@NonNull Integer id) {
        return checkNotFound(clientRepository.findOne(id), id, Client.class);
    }
}
