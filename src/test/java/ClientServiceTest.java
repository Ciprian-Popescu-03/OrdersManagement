import com.ordersmanagement.Business_Logic.ClientService;
import com.ordersmanagement.Model.Client;
import java.util.List;

public class ClientServiceTest {

    public static void main(String[] args) {
        ClientService clientService = new ClientService();

        System.out.println("---- View All Clients ----");
        List<Client> clients = clientService.viewAllClients();
        if (clients != null && !clients.isEmpty()) {
            clients.forEach(client -> System.out.println("Client ID: " + client.getId() + ", Name: " + client.getName() + ", Address: " + client.getAddress() + ", Age: " + client.getAge()));
        } else {
            System.out.println("No clients found in the database.");
        }

        System.out.println("\n---- Add New Client ----");
        Client newClient = new Client();
        if (clientService.addClient(newClient)) {
            System.out.println("New client added successfully!");
        } else {
            System.out.println("Failed to add new client.");
        }

        System.out.println("\n---- Update Client ----");
        Client clientToUpdate = clients.get(0);
        clientToUpdate.setName("Johnathan Doe");
        if (clientService.editClient(clientToUpdate)) {
            System.out.println("Client updated successfully!");
        } else {
            System.out.println("Failed to update client.");
        }

        System.out.println("\n---- Delete Client ----");
        Client clientToDelete = clients.get(clients.size() - 1);
        if (clientService.deleteClient(clientToDelete)) {
            System.out.println("Client deleted successfully!");
        } else {
            System.out.println("Failed to delete client.");
        }
    }
}
