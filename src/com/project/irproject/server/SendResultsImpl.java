package com.project.irproject.server;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.project.irproject.client.SendResultsService;
import com.project.irproject.shared.SearchDoc;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendResultsImpl extends RemoteServiceServlet implements SendResultsService{

	@Override
	public Boolean sendResults(String query, List<SearchDoc> docs) {
		Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        String msgBody = "Query : " + query;
        int i=1;
        for(SearchDoc doc : docs){
        	msgBody += "\n" + i++ + " : " + doc.getRelevant();
        }
        

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("thibault.roucou@gmail.com", "IRPROJECT"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress("thibault.roucou+irproject@gmail.com", "Thibault"));
            msg.setSubject("A new user has tested the project");
            msg.setText(msgBody);
            Transport.send(msg);
    
        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

}
