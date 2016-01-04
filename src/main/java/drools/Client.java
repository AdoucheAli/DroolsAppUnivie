package drools;

public class Client {
    private String name;
    private String email;
    private ClientType clientType;

    public Client(String name, String email, ClientType clientType) {
        this.name = name;
        this.email = email;
        this.clientType = clientType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    @Override
    public String toString() {
        return "Client named " + name + " is type " + clientType.toString() + ", email: "  + email;
    }
}
