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
        Client privateClient = new Client("test", exampleClientMails[0], ClientType.PRIVATE);
        Client companyClient = new Client("test", exampleClientMails[0], ClientType.COMPANY);
        Product product = new Product("football", 10);
        Purchase nationalPaypalPayment = new Purchase(privateClient, product, PaymentType.PAYPAL, Destination.PL);
        Purchase internationalBankPayment = new Purchase(privateClient, product, PaymentType.BANKACCOUNT, Destination.DE);
        Purchase internationalBankPaymentByCompany = new Purchase(companyClient, product, PaymentType.CREDITCARD, Destination.US);
        session.insert(nationalPaypalPayment);
        session.insert(internationalBankPayment);
        session.insert(internationalBankPaymentByCompany);
        session.fireAllRules();
        assertEquals(9, nationalPaypalPayment.toPay(), 0);
        assertEquals(25, internationalBankPayment.toPay(), 0);
        assertEquals(29, internationalBankPaymentByCompany.toPay(), 0);
        session.dispose();
        MimeMessage[] emails = greenMail.getReceivedMessages();
        assertEquals(3, emails.length);
        assertEquals(exampleClientMails[0], emails[0].getAllRecipients()[0].toString());
    }


    @Test
    public void testMailContent() throws IOException, MessagingException {
        Client client = new Client("test", exampleClientMails[1], ClientType.PRIVATE);
        Product product = new Product("football", 10);
        Purchase nationalBankPayment = new Purchase(client, product, PaymentType.BANKACCOUNT, Destination.PL);
        Mailer.sendMessage(nationalBankPayment, "Test");
        MimeMessage[] emails = greenMail.getReceivedMessages();
        assertEquals("Purchase confirmation", emails[0].getSubject());
        assertEquals(exampleClientMails[1], emails[0].getAllRecipients()[0].toString());
    }

}
