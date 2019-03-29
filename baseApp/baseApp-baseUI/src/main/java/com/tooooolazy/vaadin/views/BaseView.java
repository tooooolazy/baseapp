package com.tooooolazy.vaadin.views;

import org.apache.wink.client.ClientRuntimeException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tooooolazy.data.services.beans.OnlineBaseResult;
import com.tooooolazy.data.services.beans.UserBean;
import com.tooooolazy.util.Messages;
import com.tooooolazy.util.SearchCriteria;
import com.tooooolazy.util.TLZUtils;
import com.tooooolazy.vaadin.ui.AppLayout;
import com.tooooolazy.vaadin.ui.BaseUI;
import com.vaadin.external.org.slf4j.Logger;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewBeforeLeaveEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public abstract class BaseView<C extends SearchCriteria, E, UB extends UserBean, OR extends OnlineBaseResult, JFC> extends CustomComponent implements View {
	protected final Logger logger = LoggerFactory.getLogger( getClass() );

	protected String fragmentAndParameters;

	/**
	 * This is the root layout of the content and where everything is added.
	 */
	protected VerticalLayout vl;
	/**
	 * a progress bar to be displayed while data is been loaded!
	 */
	protected ProgressBar pb;

	/**
	 * Content as retrieved from WS - so we do not have to call ws again for export!
	 */
	protected JSONObject jo;

	public BaseUI getUI() {
		return (BaseUI) super.getUI();
	}

	/**
	 * Overridden in order to perform initialization that may require access to UI
	 * 
	 * @see com.vaadin.ui.AbstractComponent#attach()
	 */
	@Override
	public void attach() {
		super.attach();
		setBrowserTitle();

		init();

		addJavascriptFunctions();
	}

	protected void setBrowserTitle() {
		Messages.setLang(getUI().getLocale().getLanguage());
		Page.getCurrent().setTitle( getViewTitle() );
	}

	protected boolean showTitleInContent() {
		return true;
	}
	protected String getViewTitle() {
		return Messages.getString(getClass(), "application.title");
	}
	public String getThisView() {
		return getClass().getSimpleName();
	}

	/**
	 * called inside {@link #attach()} method and is where main View initialization
	 * takes place.
	 */
	protected void init() {
		vl = new VerticalLayout();
		setCompositionRoot( vl );

		if ( showTitleInContent() ) {
			vl.addComponent( createTitleComponent() );
		}
		addContent();
	}

	/**
	 * Checks if there is any static content defined and adds it before adding dynamic content.
	 */
	protected void addContent() {
		if ( hasStaticContent() ) {
			String lang = getUI().getLocale().getLanguage();

			StringBuffer sb = new StringBuffer( getClass().getSimpleName() );
			sb.append("_").append(lang);

			CustomLayout cl = new CustomLayout(sb.toString());
			vl.addComponent( cl );
		}

		addDynamicContent();
	}

	/**
	 * Override if View has static content defined by a custom layout in Theme/layouts folder. 
	 * <p>The name of the file should be: [SimpleName of View's class]_[en|el|xx.html]</p>
	 * @return
	 */
	protected boolean hasStaticContent() {
		return false;
	}

	/**
	 * Handles Search criteria (if any), retrieves WS data in background and updates UI when done
	 * <p>Always called by {@link #addContent()}</p>
	 * Override if there is no dynamic Content (see {@link MainView} as an example)
	 */
	protected void addDynamicContent() {
		try {
			boolean showContent = true;

			if (getCriteriaClass() != null) {
				AbstractComponent ac = createSearchCriteria();
				if (ac != null && !hideCriteria()) {
					addSearchCriteria( ac );
				}

				showContent = showContent();
			}
			if (showContent) {
				pb = new ProgressBar();
				pb.setIndeterminate(true);

//				VerticalLayout vl = new VerticalLayout(pb);
				vl.setSizeFull();
				vl.addComponent( pb );
				vl.setComponentAlignment(pb, Alignment.MIDDLE_CENTER);

//				if (MAIN_CONTENT.equals( getAlternateMainContentLocation() ) )
//					contentLayout.addComponent(vl, getAlternateMainContentLocation() );
//				else {
//					CustomLayout _cl = (CustomLayout)contentLayout.getComponent( MAIN_CONTENT );
//					_cl.addComponent(vl, getAlternateMainContentLocation() );
//				}

				// needed because 'BaseUI.get()' will not work as expected inside the thread below and the methods called within it
				BaseUI ui = BaseUI.get();
				ui.setPollInterval(200); 

				Thread t = new Thread() {
					volatile Component c;
					@Override
				    public void run() {
						try {
							c = generateContent( ui );
							ui.access(new Runnable() {
				                @Override
				                public void run() {
				            		vl.removeComponent( pb );

									handleGeneratedContent(c, getGeneratedContentContainer(), ui );
				                }
				            });
						} catch (NullPointerException e) { // TEP-1164
							// seems likes we should ignore this exception as it is thrown when we click fast enough on another page link (before current one is loaded)
							// this lead to setting PollInterval to -1 causing the view to never be updated...
							e.printStackTrace();
							handleGenricException(e, ui);
						} catch (Exception e) {
							vl.removeComponent( pb );

							handleGenricException(e, ui);
							
							c = createErrorContent( ui, (JFC) ui.getServiceFailureCode() );
							handleGeneratedContent(c, getGeneratedContentContainer(), ui);
						}
					}
				};
				t.start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * Implement to add search criteria Component to View.
	 * <p>Called by {@link #addDynamicContent} if {@link #getCriteriaClass} returns a valid value and {@link createSearchCriteria} actually created a Component!</p>
	 * @param ac
	 */
	protected abstract void addSearchCriteria(AbstractComponent ac);
	/**
	 * Works ONLY if {@link #getCriteriaClass} is defined. Used to specify the required condition to show content (ie user must at least select any of the available search criteria) 
	 * @return
	 */
	protected boolean showContent() {
//		return !getCriteria().hasNone();
		return true;
	}
	/**
	 * Called by {@link #addDynamicContent} in order to create the content from data retrieved from a WS
	 * @param ui 
	 * @return
	 * @throws Exception
	 */
	protected Component generateContent(BaseUI ui) throws Exception {
		jo = new JSONObject();
		boolean allOk = true;
		boolean noneOk = true;

		long ls = System.currentTimeMillis();
		OR[] tors = getWSContents();
		long lf = System.currentTimeMillis();
		logger.info("getWSContents duration: " +  (lf - ls));

		if ( tors == null ) {
			return null;
		}
		JSONArray ja = new JSONArray();
		for (OR tor : tors) {
			boolean ok = handleOR(ja, tor);
			if (!ok) {
//				msg += tor.getAsJSON().optString("failMsg");
				allOk = false;
			} else
				noneOk = false;
		}
		this.jo.put("content", ja);

		return createContent( ui );
	}
	/**
	 * Called by {@link #generateContent()} once all WS data are retrieved. This is where the UI elements are actually created.
	 * <p><b>If {@link #getWSContents} returns null this method is NOT called!</b></p>
	 * @param ui 
	 * @return
	 */
	protected abstract Component createContent(BaseUI ui);

	protected boolean handleOR(JSONArray ja, OR tor) throws JSONException {
		if (tor != null) {
			JFC jfc = (JFC)tor.getFailCode();
			if (jfc == null) {
				JSONObject jo = tor.getAsJSON();
				ja.put(jo);
				return true;
			} else {
				logger.warn("WS call problem: " + jfc + " - " + tor.getResultObject());
				ja.put(new JSONObject("{'jfc':1}")); // dummy object so the ordering remains the same
				return false;
			}
		}
		return true;
	}

	/**
	 * Called by {@link #generateContent()}. if it returns null {@link #createContent()} will not be called
	 * @return
	 */
	protected abstract OR[] getWSContents();

	/**
	 * Should be implemented/overridden by every View in order to create the array with result objects. Can return null if no WS calls are needed by the View. 
	 * @param i
	 * @return
	 */
	protected abstract OR[] create_OR_array(int i);

	protected Component createErrorContent(BaseUI ui, JFC jfc) {
		int jfcv = ui.getFailureCodeValue(jfc);
		String errorDesc = Messages.getString(getClass(), "ws.error." + ui.getFailureCode(jfc));
		if (errorDesc.startsWith("!"))
			errorDesc = Messages.getString(getClass(), "ws.error");
		Label l = new Label(errorDesc);
		l.setContentMode(ContentMode.HTML);
		l.setCaption(Messages.getString(getClass(), "error.code") + ": " + ui.getFailureCodeValue(jfc));
		return l;
	}
	/**
	 * Should return the container (eg could be main VerticalLayout {@link #vl} or a child component of it)
	 * @return
	 */
	protected AbstractComponentContainer getGeneratedContentContainer() {
		return vl;
	}

	/**
	 * Hides progress bar and adds new dynamic content to the View!
	 * <p>Called by {@link #addDynamicContent}'s background process </p>
	 * @param c
	 * @param generatedContentContainer
	 * @param ui 
	 */
	protected void handleGeneratedContent(Component c, AbstractComponentContainer generatedContentContainer, BaseUI ui) {
		if (c != null) {
			generatedContentContainer.addComponent( c );
		}		
		ui.setPollInterval(-1);
	}
	protected void handleGenricException(Exception e, BaseUI ui) {
		String errMsg = Messages.getString("ExternalServicesException.msg");
		if (e instanceof ClientRuntimeException) {
			errMsg = Messages.getString(ClientRuntimeException.class, "msg");
		} else
		if (e.getCause() instanceof RuntimeException) {
			errMsg = e.getCause().getMessage();
		} else
		if (e instanceof RuntimeException) {
			errMsg = e.getMessage();
		}
		logger.error(errMsg, e);
		if ( TLZUtils.isEmpty( errMsg ) )
			errMsg = Messages.getString(ClientRuntimeException.class, "msg");
		if ( !errMsg.startsWith("A connector") ) {// lets hide this... should actually user Exception type instead
//			Notification.show(null, errMsg, Type.ERROR_MESSAGE);
			Notification not = new Notification(null, errMsg, Type.ERROR_MESSAGE);
			not.show( ui.getPage() );
		}
	}

	/**
	 * returns all locally stored data.
	 * @param bu
	 * @return
	 */
	protected JSONArray get_Data() {
		JSONArray ja_bu = jo.optJSONArray("content");
		if (ja_bu == null) {
			ja_bu = new JSONArray();
			try {
				jo.put("content", ja_bu);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return ja_bu;
	}
	/**
	 * Since all Data is stored locally in a JSONArray (for each BU), this function returns the <b>i</b> th element of the Data of the given BU. 
	 * @param bu
	 * @param i
	 * @return
	 */
	protected JSONObject get_DataElement(int i) {
		JSONArray ja_bu = get_Data();

		JSONObject bu_jo = ja_bu.optJSONObject(i);
		if (bu_jo == null) {
			bu_jo = new JSONObject();
			try {
				ja_bu.put(i, bu_jo);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return bu_jo;
	}
	protected void set_DataElement(int i, JSONObject jo) {
		JSONArray ja_bu = get_Data();
		try {
			ja_bu.put(i, jo);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * if true, the Title of parent view will be displayed with current view title.
	 * Refers ONLY to Views in left menu.
	 * 
	 * @return
	 */
	protected boolean showParentTitle() {
		return true;
	}
	protected Component createTitleComponent() {
		String _title = Messages.getString(getClass(), "page.title");
		if ( _title.indexOf("!") > -1 )
			_title = Messages.getString(getClass(), "page.menu.title");

		// also add Parent View name (if any AND if enabled)
		if ( showParentTitle() ) {
			AppLayout al = (AppLayout) getUI().getContent();
			Class pClass = al.getParentViewClass(getClass());

			if ( pClass != null ) {
				String __title = Messages.getString(pClass, "page.title");
				if ( __title.indexOf("!") > -1 )
					__title = Messages.getString(pClass, "page.menu.title");
				_title = __title + " - " + _title;
			}
		}

		Label title = new Label( _title );
		title.setWidth("100%");
		title.addStyleName( ValoTheme.LABEL_H1 );
		return title;
	}

	protected abstract void addJavascriptFunctions();

	public void enter(ViewChangeEvent event) {
		getUI().getAppLayout().setActiveView(event.getNewView().getClass(), true);
		logger.info("Entering " + getClass().getSimpleName() + " from ip: " + BaseUI.get().getClientIp() + " =? " + BaseUI.get().getUserRemoteAddress());
		logger.info("URI params: " + event.getParameters());
		logger.info("URI params: " + event.getParameterMap("/"));

		if ( BaseUI.get().hasSecureContent() ) {
			BaseUI.get().getAppLayout().getLogoutItem().setVisible(BaseUI.get().getCurrentUser() != null);
			BaseUI.get().getAppLayout().getLoginItem().setVisible(BaseUI.get().hasSecureContent() && BaseUI.get().getCurrentUser() == null);
		}

		AppLayout al = (AppLayout) getUI().getContent();
		al.toggleChildMenuItems(getClass(), true);

		handleCriteria( event.getOldView() );
	}

	public void beforeLeave(ViewBeforeLeaveEvent event) {
		if (verifyExit(event)) {
			event.navigate();

		} else
			logger.info("Cannot leave from " + getClass().getSimpleName());
	}

	/**
	 * Automatically called when User navigates out of current View
	 * 
	 * @param event
	 * @return - false if Navigation should NOT take place!! (ie user needs to save
	 *         data first)
	 */
	public boolean verifyExit(ViewBeforeLeaveEvent event) {
		boolean canExit = true;
		return canExit;
	}

	protected abstract Class<C> getCriteriaClass();
	protected C getCriteria() {
		if (getCriteriaClass() == null)
			return null;

		C sc = (C) getSession().getAttribute(getCriteriaClass());
		if (sc == null) {
			try {
				sc = (C) TLZUtils.loadObject(getCriteriaClass().getName());
				getSession().setAttribute(getCriteriaClass(), sc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sc;
	}
	/**
	 * Default functionality that clears search criteria when entering a page from a different one
	 */
	protected void handleCriteria(View fromView) {
		if (getCriteriaClass() != null) {
			// clear criteria when entering the page from another page (if criteria exist)
			// could do it depending on a parameter!!
			if (fromView != null && fromView.getClass().equals(getClass())) {
				// retain criteria
			} else {
				getSession().setAttribute(getCriteriaClass(), null);
			}
		}
	}
	/**
	 * Override to create a custom Search criteria component
	 * <p> called by {@link #addDynamicContent()}</p>
	 * @return
	 */
	protected AbstractComponent createSearchCriteria() {
		return null;
	}
	protected boolean hideCriteria() {
		return false;
	}

	public void onInactivity() {

	}

	public void performLogout() {
		if (!verifyExit(null))
			return;

		onInactivity(); // should also trigger auto save!

		try {
			Class[] types = new Class[] {Object.class};
			UB user = (UB) BaseUI.get().getCurrentUser();

			cleanupUser( user );
		} catch (Exception e) {
			e.printStackTrace();
		}

		// "Logout" the user
		getSession().setAttribute(BaseUI.SESSION_USER_KEY, null);
		getUI().setCredentials(null);
		logger.info("log out clicked...");

		// Refresh this view, should redirect to login view
		getUI().refreshLayout();
		getUI().getNavigator().navigateTo("");
	}
	protected void cleanupUser(UB user) {
		BaseUI.get().cleanupUser(user);
	}
}
