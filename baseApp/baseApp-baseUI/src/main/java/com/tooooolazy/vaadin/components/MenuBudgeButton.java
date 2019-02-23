package com.tooooolazy.vaadin.components;

import com.tooooolazy.util.Messages;
import com.tooooolazy.util.TLZUtils;
import com.vaadin.ui.Button;

public class MenuBudgeButton extends Button {
	protected String badge;

	public MenuBudgeButton(Class c, ClickListener listener) {
		this(c, listener, null);
	}

	public MenuBudgeButton(Class c, ClickListener listener, String badge) {
		super(Messages.getString(c, "page.title"), listener);
		this.badge = badge;
		setCaptionAsHtml(true);
		if ( !TLZUtils.isEmpty( badge ) )
			setCaption( getCaption() + getBadgeHtml() );

		setDescription( Messages.getString(c, "page.tooltip") );
	}
	protected String getBadgeHtml() {
		return " <span class=\"valo-menu-badge\">" + badge + "</span>";
	}
	public String getBadge() {
		return this.badge;
	}
	public void setBadge(String badge) {
		if ( this.badge == null ) {
			this.badge = badge;
			setCaption( getCaption() + getBadgeHtml() );
		} else {
			String oldBadge = getBadgeHtml();
			this.badge = badge;
			setCaption( getCaption().replace(oldBadge, getBadgeHtml() ) );
		}
	}
}
