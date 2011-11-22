package com.project.irproject.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.Arrays;
import java.util.List;

/**
 * Example of creating a custom {@link Cell}.
 */
public class YoutubeCell implements EntryPoint {

  /**
   * A custom {@link Cell} used to render a string that contains the name of a
   * color.
   */
  static class ColorCell extends AbstractCell<String> {

    /**
     * The HTML templates used to render the cell.
     */
    interface Templates extends SafeHtmlTemplates {
      /**
       * The template for this Cell, which includes styles and a value.
       * 
       * @param styles the styles to include in the style attribute of the div
       * @param value the safe value. Since the value type is {@link SafeHtml},
       *          it will not be escaped before including it in the template.
       *          Alternatively, you could make the value type String, in which
       *          case the value would be escaped.
       * @return a {@link SafeHtml} instance
       */
      @SafeHtmlTemplates.Template("<div class=\"video\"><h2>{0}</h2>{1}</div>")
      SafeHtml cell(SafeStyles styles, SafeHtml value);
    }

    /**
     * Create a singleton instance of the templates used to render the cell.
     */
    private static Templates templates = GWT.create(Templates.class);

    @Override
    public void render(Context context, String videoId, SafeHtmlBuilder sb) {
      /*
       * Always do a null check on the value. Cell widgets can pass null to
       * cells if the underlying data contains a null, or if the data arrives
       * out of order.
       */
      if (videoId == null) {
        return;
      }

      // If the value comes from the user, we escape it to avoid XSS attacks.
      SafeHtml safeValue = SafeHtmlUtils.fromString(videoId);

      // Use the template to create the Cell's html.
      SafeStyles styles = SafeStylesUtils.fromTrustedString("color:blue");
      SafeHtml rendered = templates.cell(styles, safeValue);
      sb.append(rendered);
    }
  }

  /**
   * The list of data to display.
   */
  private static final List<String> COLORS = Arrays.asList("red", "green", "blue", "violet",
      "black", "gray");

  @Override
  public void onModuleLoad() {
    // Create a cell to render each value.
    ColorCell cell = new ColorCell();

    // Use the cell in a CellList.
    CellList<String> cellList = new CellList<String>(cell);

    // Push the data into the widget.
    cellList.setRowData(0, COLORS);

    // Add it to the root panel.
    RootPanel.get().add(cellList);
  }
}