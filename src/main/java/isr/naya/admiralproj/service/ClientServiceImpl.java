package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Client;
import isr.naya.admiralproj.repo.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    @Override
    public Client save(@NonNull Client client) {
        return clientRepository.save(client);
    }
}
