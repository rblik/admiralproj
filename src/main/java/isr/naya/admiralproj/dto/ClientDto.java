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
        this.id = client.getId();
        this.companyNumber = client.getCompanyNumber();
        this.clientNumber = client.getClientNumber();
        this.isEnabled= client.isEnabled();
        this.name = client.getName();
        this.phones = client.getPhones();
        this.addresses = client.getAddresses();
        this.projects = projects;
    }

    private Integer id;
    private String companyNumber;
    private String clientNumber;
    private boolean isEnabled;
    private String name;
    private Set<String> phones;
    private Set<Address> addresses;
    private List<Project> projects;
}
