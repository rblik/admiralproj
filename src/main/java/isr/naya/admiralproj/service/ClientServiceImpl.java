package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Client;
import isr.naya.admiralproj.repository.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static isr.naya.admiralproj.util.ValidationUtil.checkNotFound;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

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
    public Client get(@NonNull Integer id) {
        return checkNotFound(clientRepository.getWithAddressesAndPhones(id), id, Client.class);
    }
}
