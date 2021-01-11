import client.ClientApp;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import serveur.ReceiverImpl;

import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ReceiverImplTest {

    private static Temporal startedAt;

    @InjectMocks
    private ReceiverImpl receiver;

    {
        try {
            receiver = new ReceiverImpl();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    @BeforeAll
    static public void initStartingTime() {
        startedAt = Instant.now();
    }

    @AfterAll
    static public void showTestDuration() {
        Instant endedAt = Instant.now();
        long duration = Duration.between(startedAt, endedAt).toMillis();
        System.out.println(MessageFormat.format("Durée des tests : {0} ms", duration));
    }

    @Test
    void addClient() throws RemoteException {

        String name = "user1";
        receiver.addClient(name);
        assertNotNull(receiver.getClients());
    }

    @Test
    void removeClient() throws RemoteException {

        String name ="user1";
        receiver.removeClient(name);
        assertFalse(receiver.getClients().contains(name));
    }


    @Test
    void receive() throws RemoteException {


        String name ="user1";
        String msg ="message1";
        receiver.receive(name,msg);

        String name2 ="user2";
        String msg2 ="message2";
        receiver.receive(name,msg2);

        // Si on récéptionne deux messages d'affilié, les deux sont bien présents dans la liste
        assertEquals(2,receiver.getMsg().size());
    }

    @Test
    void getClients() {
    }

    @Test
    void getMsg() {
    }

    @Test
    void clearMsg() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testHashCode() {
    }
}