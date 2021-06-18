package cowin.api;

import org.json.simple.JSONObject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class SendEmail {
    public static void sendMail(String currentDateWithTime, ArrayList centerName,ArrayList centerAddress,ArrayList capacity,ArrayList vaccineName) throws IOException {
        String to = "22494.ankit15@gmail.com,arnavkumar2995@gmail.com ";
        String from = "22494.ankit@gmail.com";
        final String username = "22494.ankit@gmail.com";
        final String password = "bpwoikwhoeivirun";
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                }
        );

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("COWIN Slot Notification "+currentDateWithTime);
            StringBuilder stringBuilder=new StringBuilder();
            for(int i=0;i< centerAddress.size();i++) {
                stringBuilder.append("Center Name:\t\t\t" + centerName.get(i)+"\n");
                stringBuilder.append("Center Address:\t\t\t" + centerAddress.get(i)+"\n");
                stringBuilder.append("No of slots Left:\t\t\t"+capacity.get(i)+"\n");
                stringBuilder.append("Vaccine Name:\t\t\t"+vaccineName.get(i)+"\n");
                stringBuilder.append("\n\n\n");
            }
            System.out.println(stringBuilder);
            message.setText(stringBuilder.toString());

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}

