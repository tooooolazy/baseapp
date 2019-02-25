package com.tooooolazy.vaadin.components;

import com.tooooolazy.util.Credentials;
import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.views.BaseView;
import com.vaadin.data.Binder;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;

/**
 * Creates the 'I forgot my password' form.
 * Logic behind the whole process it is not yet implemented.
 * @author tooooolazy
 *
 */
public class ForgotPasswordComponent extends InputComponent<Credentials> {
	protected TextField user;

	public ForgotPasswordComponent(Button.ClickListener listener) {
		super(listener, (Class)null);
	}
	public ForgotPasswordComponent(Button.ClickListener listener, BaseView baseView) {
		super(listener, baseView);
	}
	public ForgotPasswordComponent(Button.ClickListener listener, Class _class) {
		super(listener, _class);
	}
	protected void createBinder() {
		super.createBinder();

		binder.bind( user, Credentials::getUsername, Credentials::setUsername );
	}
	protected Credentials setBinderBean() {
		bean = new Credentials();
		binder.setBean( bean );

		return bean;
	}

	protected int getMaxColumns() {
		return 1;
	}
	@Override
	public  Component[] getFieldsArray() {
		return new Component[] {user};
	}
	@Override
	protected void createInputFields() {
		// Create the user input field
		user = new TextField(Messages.getString(getClass(), "username"));
		user.setWidth(getFieldWidth());
		user.setPlaceholder( Messages.getString(getClass(), "username.prompt") );

//		user.setRequired(true); // not inVaadin 8
//		user.addValidator(new EmailValidator(Messages.getString(EmailValidator.class, "errorMsg")));
//		user.setInvalidAllowed(false); // not inVaadin 8
//		user.setNullRepresentation(""); // not inVaadin 8

		
	}

	@Override
	public void focus() {
		super.focus();
		user.focus();
	}
	public TextField getUserField() {
		return user;
	}
	public String getUsername() {
		return user.getValue();
	}
//	public Credentials getCredentials() {
//		return new Credentials(getUsername(), null);
//	}
}
