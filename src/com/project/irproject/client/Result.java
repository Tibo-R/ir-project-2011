package com.project.irproject.client;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.youtube.client.YouTubePlayer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.project.irproject.shared.SearchDoc;

public class Result extends Composite {

	public Result(SearchDoc doc) {
		VerticalPanel widget = new VerticalPanel();
        initWidget(widget);
        widget.setStylePrimaryName("result");
        if(doc.getType().equals("youtube")){
			AbstractMediaPlayer player = null;
			try {
				// create the player, specifing URL of media
				player = new YouTubePlayer("http://www.youtube.com/v/" + doc.getUrl(), "560px", "315px");
				widget.add(player); // add player to panel.
			} catch(PluginVersionException e) {
				// required Flash plugin version is not available,
				// alert user possibly providing a link to the plugin download page.
				widget.add(new HTML(".. some nice message telling the " +
						"user to download plugin first .."));
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
			widget.add(img);
		}
		else{
			HTML html = new HTML("<h2>" + doc.getTitle() + " : </h2>" + (doc.hasSummary() ? doc .getSummary() : doc.getUrl()));
			widget.add(html);

		}
        
	}

}
