package com.project.irproject.server;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
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
	public Boolean sendResults(String query, HashMap<String, List<SearchDoc>> docs) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		String msgBody = "Query : " + query;
		
		msgBody += "\n*---------------------------------*\n";
		msgBody += "*           BASELINE              *\n";
		msgBody += "*---------------------------------*\n";
		
		msgBody += getDisplayedResults(docs.get("baseline"));
		

		msgBody += "\n*---------------------------------*\n";
		msgBody += "*            RANKED               *\n";
		msgBody += "*---------------------------------*\n";
		
		msgBody += getDisplayedResults(docs.get("ranked"));

//		System.out.println(msgBody);
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("thibault.roucou@gmail.com", "IRPROJECT"));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress("thibault.roucou+irproject@gmail.com", "Thibault"));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress("rdoria1+irproject@gmail.com", "Rodrigo"));
			msg.setSubject("Results for : " + query);
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
	
	private String getDisplayedResults(List<SearchDoc> docs){
		double pAtFive = 0;
		double pAtTen = 0;
		double pAtTwenty = 0;
		int nbNone = 0;
		int nbNotRelevant = 0;
		String msgBody = "";
		int i=1;
		for(SearchDoc doc : docs){
			if(doc.getRelevant() == 1){
				if(i <= 5){
					pAtFive++;
				}
				if(i <= 10){
					pAtTen++;
				}
				pAtTwenty++;
			}
			else if(doc.getRelevant() == -1)
				nbNotRelevant++;
			else nbNone++;
			msgBody += "\n" + i++ + " : " + doc.getRelevant();
		}
		
		msgBody += "\n Nb relevant : " + pAtTwenty;
		msgBody += "\n Nb not relevant : " + nbNotRelevant;
		msgBody += "\n Nb don't know : " + nbNone;
		
		pAtFive = pAtFive/5d;
		pAtTen = pAtTen/10d;
		pAtTwenty = pAtTwenty/20d;
		
		msgBody += "\n P@5 : " + pAtFive;
		msgBody += "\n P@10 : " + pAtTen;
		msgBody += "\n P@20 : " + pAtTwenty;
		
		return msgBody;
	}

}
