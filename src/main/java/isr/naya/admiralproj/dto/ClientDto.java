package isr.naya.admiralproj.dto;

import isr.naya.admiralproj.model.Address;
import isr.naya.admiralproj.model.Client;
import isr.naya.admiralproj.model.Project;
import lombok.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Value
public class ClientDto implements Serializable {

    public ClientDto(Client client, List<Project> projects) {
        this.companyNumber = client.getCompanyNumber();
        this.name = client.getName();
        this.phones = client.getPhones();
        this.addresses = client.getAddresses();
        this.projects = projects;
    }

    private Integer companyNumber;
    private String name;
    private Set<String> phones;
    private Set<Address> addresses;
    private List<Project> projects;
}
