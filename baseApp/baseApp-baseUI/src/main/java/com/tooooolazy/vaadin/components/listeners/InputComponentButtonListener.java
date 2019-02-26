package com.tooooolazy.vaadin.components.listeners;

import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.components.InputComponent;
import com.tooooolazy.vaadin.exceptions.DataRequiredException;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ErrorLevel;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

/**
 * Base class for handling the submit button in an input component
 * @author gpatoulas
 *
 */
public class InputComponentButtonListener implements Button.ClickListener {
	protected InputComponent ic;
	protected BinderValidationStatus bvs;

	protected void init() {
		
	}
	protected void getInputComponent(ClickEvent event) {
		Component c = event.getComponent();
		while (!(c instanceof InputComponent)) {
			c = c.getParent();
		}
		ic = (InputComponent) c;
	}
	public boolean isValid() {
		bvs = ic.getBinder().validate();

		return bvs.isOk();
	}
	protected Object submit() throws Exception {
		return null;
	}
	protected void onError(Exception e) {
		ic.setComponentError(createError(ic.getClass(), e));
		ic.enableSubmit();
	}
	protected void onSuccess(Object res) {
		
	}
	@Override
	public void buttonClick(ClickEvent event) {
		init();

		getInputComponent(event);

		Exception le = null;
		Object res = null;

		try {
			if (isValid()) {
				try {
					ic.setComponentError(null);

					res = submit();
				} catch (Exception e) {
					e.printStackTrace();
					if (e instanceof RuntimeException && e.getCause() != null) {
						e = (Exception)e.getCause();
					}
						
					le = e;
				}
	
				if (le != null) {
					onError(le);
					return;
				} else {
					onSuccess(res);
				}
			} else {
				le = new DataRequiredException();
				onError(le);
			}
		} catch (Exception e) {
			onError(e);
		}
	}

	public static ErrorMessage createError(final Class c, final Exception e) {
		ErrorMessage er = new ErrorMessage() {
			@Override
			public String getFormattedHtmlMessage() {
				Messages.setLang( BaseUI.get().getLocale().getLanguage() );
				if (e == null)
					return Messages.getString(c, "error");
				else {
					return Messages.getString(e.getClass(), "msg");
				}
			}
			
			@Override
			public ErrorLevel getErrorLevel() {
				return ErrorLevel.ERROR;
			}
		};
		return er;
	}
}
