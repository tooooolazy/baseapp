package com.tooooolazy.vaadin.components;

import com.tooooolazy.util.Messages;
import com.vaadin.ui.Grid;
import com.vaadin.ui.components.grid.HeaderRow;

/**
 * Provides helper methods for all grids that extend this class
 * @author tooooolazy
 *
 * @param <T>
 */
public class BaseGrid<T> extends Grid<T> {

	/**
	 * Makes a Grid header fit two lines of text by adjusting css and assuming that only 1 line break (<br>) exist. In order to work the following style must exist in theme:
	 * <p><pre>#gridId .v-grid-cell.two-lines .v-grid-column-default-header-content {
	 * 	line-height: 16px;
	 * }
	 * where gridId is the id of the grid component (set it using setId(xx) method)</pre></p>
	 * 
	 * @param h - the HeaderRow to affect
	 * @param colId - the column Id
	 * @param captionKey - the key to look for in bundles (the value must have maximum 1 <br> defined in it otherwise UI will not look so good)
	 */
	protected void makeTwoLineHeader(HeaderRow h, String colId, String captionKey) {
		h.getCell(colId).setHtml( Messages.getString( getClass(), captionKey ) );
		h.getCell(colId).setStyleName("two-lines");
	}

}
