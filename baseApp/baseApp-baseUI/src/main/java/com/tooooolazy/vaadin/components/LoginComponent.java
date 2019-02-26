package com.tooooolazy.vaadin.components;

import com.tooooolazy.util.Credentials;
import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.views.BaseView;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.AbstractValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Link;
import com.vaadin.ui.PasswordField;

public class LoginComponent extends ForgotPasswordComponent {

	protected PasswordField password;

//	protected RegistrationButton regBtn;
//	protected ForgotPasswordButton forgotBtn;
	protected Link forgotLink;
	
	public LoginComponent(Button.ClickListener listener) {
		super(listener, LoginComponent.class);

	}
	public LoginComponent(Button.ClickListener listener, BaseView baseView) {
		super(listener, baseView);
		_class = LoginComponent.class; // override clas to use for messages!!
	}
	@Override
	protected void createBinder() {
		super.createBinder();

		binder.forField( password )
			.asRequired( Messages.getString( _class, "Form.requiredMessage") )
			.withValidator( new PasswordValidator() )
			.bind( Credentials::getPassword, Credentials::setPassword );
	}

	@Override
	protected int getMaxColumns() {
		return 1;
	}
	@Override
	public  Component[] getFieldsArray() {
		return new Component[] {user, password};
	}
	/**
	 * 
	 */
	protected void createFields(Button.ClickListener listener) {
		super.createFields(listener);

		// Create the password input field
		password = new PasswordField(Messages.getString(PasswordField.class, "password"));
		password.setWidth(getFieldWidth());
		password.setValue("");

//		regBtn = new RegistrationButton();
//		forgotBtn = new ForgotPasswordButton(listener);
//		forgotLink = BaseUI.createMainMenuLink(this.getClass(), ForgotPasswordView.class);
		clearButton.setVisible( false );
	}

	public PasswordField getPasswordField() {
		return password;
	}
	public void clearPassword() {
		password.setValue(null);
		password.focus();
	}

	public String getPassword() {
		return password.getValue();
	}

	private static final class PasswordValidator extends AbstractValidator<String> {

		public PasswordValidator() {
			// make sure text in bundles match the validation process in 'isValid' method
			// currently the text is located in 'utils-->resources-->com.tooooolazy.util-->messages.*'
			super(Messages.getString(PasswordValidator.class, "errorMsg"));
		}

		@Override
		public ValidationResult apply(String value, ValueContext context) {
			return toResult(value, isValid(value));
		}
		
		protected boolean isValid(String value) {
			//
			// Password must be at least 6 characters long and contain at least one number
			//
			if (value != null && (value.length() < 6 || !value.matches(".*\\d.*"))) {
				return false;
			}
			return true;
		}
	}
}
