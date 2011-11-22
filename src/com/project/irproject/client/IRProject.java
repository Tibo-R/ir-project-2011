package com.project.irproject.client;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.youtube.client.YouTubePlayer;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.project.irproject.shared.FieldVerifier;
import com.project.irproject.shared.SearchDoc;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.youtube.client.YouTubeEmbeddedPlayer;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IRProject implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Send");
		sendButton.setText("Search");
		final TextBox nameField = new TextBox();
		nameField.setText("Your search");
		final Label errorLabel = new Label();

		// We can add style names to widgets

		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel rootPanel = RootPanel.get("nameFieldContainer");
		rootPanel.add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		sendButton.setSize("105px", "33px");
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);

		final FlowPanel content = new FlowPanel();
		rootPanel.add(content);
		//		content.setSize("430px", "187px");
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);




		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer,
						new AsyncCallback<List<SearchDoc>>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						dialogBox
						.setText("Remote Procedure Call - Failure");
						serverResponseLabel
						.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(SERVER_ERROR);
						dialogBox.center();
						closeButton.setFocus(true);
					}
					public void onSuccess(List<SearchDoc> result) {
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel
						.removeStyleName("serverResponseLabelError");
						if((result != null) && (result.size() > 0)){
							for(SearchDoc doc:result){
								if(doc.getType().equals("youtube")){
									AbstractMediaPlayer player = null;
									try {
									     // create the player, specifing URL of media
									     player = new YouTubePlayer("http://www.youtube.com/v/" + doc.getUrl(), "560px", "315px");
									     content.add(player); // add player to panel.
									} catch(PluginVersionException e) {
									     // required Flash plugin version is not available,
									     // alert user possibly providing a link to the plugin download page.
									     content.add(new HTML(".. some nice message telling the " +
									           "user to download plugin first .."));
									} catch(PluginNotFoundException e) {
									     // required Flash plugin not found, display a friendly notice.
										content.add(PlayerUtil.getMissingPluginNotice(e.getPlugin()));
									}
////									YouTubeEmbeddedPlayer youTubeEmbeddedPlayer = new YouTubeEmbeddedPlayer(doc.getUrl());
////									youTubeEmbeddedPlayer.setWidth("427px");
////									youTubeEmbeddedPlayer.setHeight("320px");
////									content.add(youTubeEmbeddedPlayer);
//									content.insert(new HTML("<div class='result'>" + doc.getTitle() + " : <iframe width=\"560\" height=\"315\" src=\"http://www.youtube.com/v/" + doc.getUrl() + "\" frameborder=\"0\" allowfullscreen></iframe></div>"), 0);
								}
									else
										content.insert(new HTML("<div class='result'>" + doc.getTitle() + " : " + doc.getUrl() + "</div>"), 0);
							}
						}

						//								dialogBox.center();
						//								closeButton.setFocus(true);
					}

				});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
}
