package com.project.irproject.client;

import java.util.HashMap;
import java.util.List;
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
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.project.irproject.shared.FieldVerifier;
import com.project.irproject.shared.SearchDoc;
import com.google.gwt.user.client.ui.FlowPanel;


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
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final SendResultsServiceAsync sendResultsService = GWT
			.create(SendResultsService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Send");
		sendButton.setText("Search");
		final TextBox nameField = new TextBox();
		nameField.setText("Your search");
		final Label errorLabel = new Label();
		
		final Image loaderImg = new Image("ajax-loader.gif");
		final RootPanel loader;
		



		final Button endButton = new Button("Send Results");
		final Button nextButton = new Button("Next");

		// We can add style names to widgets

		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel rootPanel = RootPanel.get("nameFieldContainer");
		rootPanel.add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		sendButton.setSize("105px", "33px");
		RootPanel.get("errorLabelContainer").add(errorLabel);
		loader = RootPanel.get("loaderContainer");
		loader.setVisible(false);
		loader.add(loaderImg);
		loader.add(new Label("Sorry, the search takes a while..."));


		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);

		final FlowPanel content = new FlowPanel();
		RootPanel.get("contentContainer").add(content);
		//		rootPanel.add(content);
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
				loader.setVisible(true);
				// First, we validate the input.
				errorLabel.setText("");
				final String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer,
						new AsyncCallback<HashMap<String, List<SearchDoc>>>() {
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
					public void onSuccess(final HashMap<String, List<SearchDoc>> result) {
						content.clear();
						dialogBox.setText("Remote Procedure Call");
						serverResponseLabel.removeStyleName("serverResponseLabelError");

						if((result.get("baseline") != null) && (result.get("baseline").size() > 0)){
							for(SearchDoc doc:result.get("baseline")){

								System.out.println(doc.getTitle() + " : " + doc.getScore());
								Result res = new Result(doc);
								loader.setVisible(false);
								content.add(res);
							}

							class NextHandler implements ClickHandler {
								public void onClick(ClickEvent event) {
									nextButton.setVisible(false);
									nextButton.setEnabled(false);
									content.clear();
									loader.setVisible(true);
									if((result.get("ranked") != null) && (result.get("ranked").size() > 0)){
										for(SearchDoc doc:result.get("ranked")){

											System.out.println(doc.getTitle() + " : " + doc.getScore());
											Result res = new Result(doc);
											loader.setVisible(false);
											content.add(res);
										}

										class EndHandler implements ClickHandler {
											/**
											 * Fired when the user clicks on the sendButton.
											 */
											public void onClick(ClickEvent event) {
												sendClickToServer();
											}


											/**
											 * Send the name from the nameField to the server and wait for a response.
											 */
											private void sendClickToServer() {
												// Then, we send the input to the server.
												endButton.setEnabled(false);
												sendResultsService.sendResults(textToServer, result, 
														new AsyncCallback<Boolean>() {
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
													public void onSuccess(Boolean result) {
														// Show the RPC error message to the user
														dialogBox
														.setText("Email sent !!!");
														dialogBox.center();
														closeButton.setFocus(true);
													}

												});
											}
										}

										RootPanel.get("endButtonContainer").add(endButton);
										EndHandler handler = new EndHandler();
										endButton.addClickHandler(handler);
									}
								}

							}

							RootPanel.get("endButtonContainer").add(nextButton);
							NextHandler handler = new NextHandler();
							nextButton.addClickHandler(handler);



						}
						else
							content.add(new HTML("Oups... No results found..."));

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
