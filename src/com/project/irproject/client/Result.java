package com.project.irproject.client;

import therandomhomepage.widgets.client.LightboxImage;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.ConfigParameter;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.TransparencyMode;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer.UIMode;
import com.bramosystems.oss.player.youtube.client.YouTubePlayer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.project.irproject.shared.SearchDoc;

public class Result extends Composite {
	private SearchDoc document;
	private FlowPanel relevanceButtonPanel;

	public Result(SearchDoc doc, int rank) {
		this.document = doc;
		VerticalPanel widget = new VerticalPanel();
		sinkEvents(Event.ONMOUSEOVER);
		sinkEvents(Event.ONMOUSEOUT);
		initWidget(widget);
		widget.setStylePrimaryName("result");
		if(doc.getType().equals("youtube")){
			AbstractMediaPlayer player = null;
			try {
				// create the player, specifying URL of media
				player = new YouTubePlayer("http://www.youtube.com/v/" + doc.getUrl(), "100%", "100%");
				player.setHeight("100%");
				player.setControllerVisible(false);
				player.setConfigParameter(ConfigParameter.TransparencyMode, TransparencyMode.TRANSPARENT);
				player.setConfigParameter(ConfigParameter.WMPUIMode, UIMode.INVISIBLE);
				player.setConfigParameter(ConfigParameter.BackgroundColor, "#ffffff");
				widget.add(player); // add player to panel.
			} catch(PluginVersionException e) {
				// required Flash plugin version is not available,
				// alert user possibly providing a link to the plugin download page.
				widget.add(new HTML("You need to download the flash plugin to be able to see this video"));
			} catch(PluginNotFoundException e) {
				// required Flash plugin not found, display a friendly notice.
				widget.add(PlayerUtil.getMissingPluginNotice(e.getPlugin()));
			}
			////									YouTubeEmbeddedPlayer youTubeEmbeddedPlayer = new YouTubeEmbeddedPlayer(doc.getUrl());
			////									youTubeEmbeddedPlayer.setWidth("427px");
			////									youTubeEmbeddedPlayer.setHeight("320px");
			////									content.add(youTubeEmbeddedPlayer);
			//									content.insert(new HTML("<div class='result'>" + doc.getTitle() + " : <iframe width=\"560\" height=\"315\" src=\"http://www.youtube.com/v/" + doc.getUrl() + "\" frameborder=\"0\" allowfullscreen></iframe></div>"), 0);
		}
		else if(doc.getType().equals("flickr")){
			Image img = new Image(doc.getUrl());
			img.setAltText(doc.getTitle());
			img.setTitle(doc.getTitle());
			LightboxImage lightboxImage = new LightboxImage(img);
			
			Anchor link = new Anchor("", doc.getUrl());
			link.setTarget("_blank");
			link.getElement().appendChild(img.getElement());



//			DOM.setElementAttribute(img.getElement(), "style", "max-height: 100%; max-width: 100%;");
			widget.add(lightboxImage);
//			img.setWidth("100%");
			
		}
		else{
			HTML html;
			if(rank <= 6)
			{
				html = new HTML("<h2><a href=\"" + doc.getUrl() + "\" target=\"_blank\" >"+ doc.getTitle() + "</a></h2>" + (doc.hasSummary() ? doc .getSummary() : doc.getUrl()));
			}
			else{
				html = new HTML("<h2><a href=\"" + doc.getUrl() + "\" target=\"_blank\" >"+ doc.getTitle() + "</a></h2>");
			}
			widget.add(html);

		}


//		// Make some radio buttons, all in one group.
//		RadioButton relevant = new RadioButton("relevanceButton" + document.getId(), "Relevant");
//		RadioButton notRelevant = new RadioButton("relevanceButton" + document.getId(), "Not Relevant");
//		RadioButton dontKnow = new RadioButton("relevanceButton" + document.getId(), "Don't know");
//
//		relevant.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				if(event.getValue() == true){
//					document.setRelevant(1);
//					addStyleName("relevant");
//					removeStyleName("notRelevant");
//				}
//
//			}
//		});
//
//		notRelevant.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				if(event.getValue() == true){
//					document.setRelevant(-1);
//					addStyleName("notRelevant");
//					removeStyleName("relevant");
//				}
//
//			}
//		});
//
//		dontKnow.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				if(event.getValue() == true){
//					document.setRelevant(0);
//					removeStyleName("notRelevant");
//					removeStyleName("relevant");
//				}
//
//			}
//		});
//
//		switch(document.getRelevant()){
//		case 1 : 
//			relevant.setChecked(true);
//			addStyleName("relevant");
//		break;
//		case -1 : 
//			notRelevant.setChecked(true);
//			addStyleName("notRelevant");
//		break;
//		default : dontKnow.setChecked(true);
//		}
//
//		// Add them to the root panel.
//		relevanceButtonPanel = new FlowPanel();
//		relevanceButtonPanel.add(relevant);
//		relevanceButtonPanel.add(notRelevant);
//		relevanceButtonPanel.add(dontKnow);
//		widget.add(relevanceButtonPanel);
//
//		relevanceButtonPanel.setVisible(false);
	}

//	@Override
//	public void onBrowserEvent(Event event)
//	{
//		switch(DOM.eventGetType(event)) {
//		case Event.ONMOUSEOVER:
//			relevanceButtonPanel.setVisible(true);
//			this.addStyleName("hover");
//			break;
//
//		case Event.ONMOUSEOUT:
//			relevanceButtonPanel.setVisible(false);
//			this.removeStyleName("hover");
//			break;
//		default:
//			break;
//		}
//	}

}


