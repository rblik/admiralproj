package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Client;

import java.util.List;

public interface ClientService {
    Client save(Client client);

    List<Client> getAll();

    List<Client> getAllWithProjects();
}
