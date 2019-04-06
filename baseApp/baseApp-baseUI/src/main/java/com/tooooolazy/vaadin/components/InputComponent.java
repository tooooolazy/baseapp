package com.tooooolazy.vaadin.components;


import com.tooooolazy.util.Messages;
import com.tooooolazy.vaadin.views.BaseView;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.ErrorMessage;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;

public abstract class InputComponent<BEAN> extends CustomComponent {
	protected Button submitButton, clearButton;
	protected Label msg;
	protected Label subInfo, topInfo;
	protected Class _class;
	protected BaseView baseView;
	protected GridLayout fieldsLayout;
	protected boolean submitClicked;
	protected VerticalLayout fields;
	protected HorizontalLayout btns;

	protected Button.ClickListener listener;
	protected Binder<BEAN> binder;
	protected BEAN bean;

	public void setSubmitClicked(boolean submitClicked) {
		this.submitClicked = submitClicked;
	}
	public boolean isSubmitClicked() {
		return submitClicked;
	}


	public InputComponent(Button.ClickListener listener, BaseView baseView) {
		this( listener, baseView.getClass() );
		this.baseView = baseView;
	}
	public InputComponent(Button.ClickListener listener, Class _class) {
		this._class = _class;
		setSizeFull();

		this.listener = listener;
	}
	protected void createBinder() {
		binder = new Binder<BEAN>();
	}
	protected abstract BEAN setBinderBean();
	public Binder getBinder() {
		return binder;
	}

	public void attach() {
		super.attach();

		createFields(listener);

		createBinder();
		setBinderBean();

		setupLayout();
		setFieldValues();
	}

	protected void createFields(Button.ClickListener listener) {
		createInputFields();

		submitButton = new Button(Messages.getString(_class, "submitBtn"), listener);
		submitButton.setDisableOnClick( true );
		submitButton.setData(this);
		submitButton.setClickShortcut(KeyCode.ENTER);
		submitButton.addStyleName("tiny");
		submitButton.addStyleName(ValoTheme.BUTTON_PRIMARY);

		if (showClearButton()) {
			clearButton = new Button(Messages.getString(_class, "clearBtn"), new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					clearAll();
				}
			});
			clearButton.addStyleName("tiny");
		}

		msg = new Label("");
	}

	protected abstract void createInputFields();
	protected void setFieldValues() {
		
	}
	protected int getMaxColumns() {
		return 1;
	}
	protected boolean addExtraEmptyLabel() {
		return false;
	}

	protected void setupLayout() {
		if (showSubInfo()) {
			subInfo = new Label(Messages.getString(_class, "subInfo"));
			subInfo.setContentMode(ContentMode.HTML);
		}
		fieldsLayout = new GridLayout(getMaxColumns(),2);
		addFields();
		fieldsLayout.setSpacing(true);
		fieldsLayout.setWidth(getCriteriaWidth());
		fields = new VerticalLayout();
		if (topInfo != null)
			fields.addComponent(topInfo);
		fields.addComponent(fieldsLayout);
		addExtraComponents();
		fields.setWidth(getCriteriaWidth());
		btns = new HorizontalLayout(submitButton);
		if (clearButton != null)
			btns.addComponent(clearButton);
		btns.setSpacing(true);
		fields.addComponents(btns, msg);
		if (subInfo != null)
			fields.addComponent(subInfo);
		fields.setSpacing(true);
		fields.setMargin(new MarginInfo(false, true, false, true));

		setCompositionRoot(fields);
	}
	protected boolean showSubInfo() {
		return true;
	}
	protected boolean showClearButton() {
		return true;
	}
	/**
	 * called before Buttons, message and subInfo is added!
	 */
	protected void addExtraComponents() {
		// TODO Auto-generated method stub
		
	}
	protected void addFields() {
		fieldsLayout.addComponents( getFieldsArray() );
	}
	protected String getCriteriaWidth() {
		return "100%";
	}

	public abstract Component[] getFieldsArray();

	public void setComponentError(ErrorMessage em) {
		super.setComponentError(em);

		if (em != null) {
			msg.setValue(em.getFormattedHtmlMessage());
			msg.addStyleName("i-error-msg");
		} else {
			msg.setValue(null);
			msg.removeStyleName("i-error-msg");
		}
	}
	public void setSubInfo(String txt) {
		subInfo.setValue(txt);
	}

	public void enableSubmit() {
		submitButton.setEnabled(true);
	}
	public void setSubmitEnabled(boolean b) {
		submitButton.setEnabled( b );
	}
	public void setSubmitVisible(boolean b) {
		submitButton.setVisible(b);
	}
	public void clearAll() {
		setComponentError(null);
		binder.readBean( setBinderBean() );

		enableSubmit();
	}

	protected String getFieldWidth() {
		return "150px";
	}
	public BEAN getBean() {
		return bean;
	}
}
