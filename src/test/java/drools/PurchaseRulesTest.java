package drools;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import utils.DroolsSessionUtils;
import utils.Mailer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class PurchaseRulesTest {

    private static final String DRL_PATH = "rules.drl";
    private String[] exampleClientMails = { "test@gmail.com", "test2@gmail.com"};
    public GreenMail greenMail;

    @Before
    public void setUp() throws IOException {
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/properties/mail.properties"));
        greenMail.setUser(properties.getProperty("mail.username"),
                properties.getProperty("mail.username"),
                properties.getProperty("mail.password"));
    }

    @After
    public void cleanUp() {
        greenMail.stop();
    }


    @Test
    public void testRules() throws MessagingException {
        KieSession session = DroolsSessionUtils.createKieSession(DRL_PATH);
        Client client = new Client("test", exampleClientMails[0], ClientType.PRIVATE);
        Purchase nationalPaypalPayment = new Purchase(client, 10, PaymentType.CREDITCARD, Destination.PL);
        Purchase internationalBankPayment = new Purchase(client, 100, PaymentType.BANKACCOUNT, Destination.DE);
        session.insert(nationalPaypalPayment);
        session.insert(internationalBankPayment);
        session.fireAllRules();
        assertEquals(9, nationalPaypalPayment.toPay(), 0);
        assertEquals(115, internationalBankPayment.toPay(), 0);
        session.dispose();
        MimeMessage[] emails = greenMail.getReceivedMessages();
        assertEquals(1, emails.length);
        assertEquals(exampleClientMails[0], emails[0].getAllRecipients()[0].toString());
    }


    @Test
    public void testMailContent() throws IOException, MessagingException {
        Client client = new Client("test", exampleClientMails[1], ClientType.PRIVATE);
        Purchase nationalBankPayment = new Purchase(client, 10, PaymentType.BANKACCOUNT, Destination.PL);
        Mailer.sendMessage(nationalBankPayment);
        MimeMessage[] emails = greenMail.getReceivedMessages();
        assertEquals("Purchase confirmation", emails[0].getSubject());
        assertEquals(exampleClientMails[1], emails[0].getAllRecipients()[0].toString());
    }

}
