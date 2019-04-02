package com.tooooolazy.ws;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tooooolazy.data.services.beans.OnlineBaseParams;
import com.tooooolazy.data.services.beans.OnlineBaseResult;
import com.tooooolazy.domain.DataBaseRepository;
import com.tooooolazy.domain.UserAccountRepository;
import com.tooooolazy.domain.objects.UserAccount;
import com.tooooolazy.util.TLZUtils;
import com.tooooolazy.util.exceptions.AccessDeniedException;
import com.tooooolazy.util.exceptions.ItemLockedException;

/**
 * Helper component to wrap a method within a Transaction, if required. Basically, acts as a delegator using reflection to call the selected method. 
 * <p>Should be overridden. Also '@Component' must be used on derived class.</p>
 * 
 * @see https://stackoverflow.com/questions/9400160/autowire-depending-upon-the-subclass
 * @author gpatoulas
 */
//@Component
public abstract class WsBaseDataHandler<DR extends DataBaseRepository, OR extends OnlineBaseResult, OP extends OnlineBaseParams> /*implements DataHandlerClient<OR, OP>*/ {
	private static org.apache.logging.log4j.Logger logger = LogManager.getLogger();

	@Autowired
	private UserAccountRepository userAccountRepository;

	protected abstract DR getDataRepository();
    
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public OR execute(OP params) {
		return _execute(params);
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public OR executeUpdate(OP params) {
		return _execute(params);
	}
	/**
	 * Uses reflection to execute a method of {@link DataBaseRepository}.
	 * Depending of the method that called it, either {@link #execute(OnlineBaseParams)} or {@link #executeUpdate(OnlineBaseParams)},
	 * the method called by reflection will NOT <b>or</b> will be wrapped in a Transaction. 
	 * @param params
	 * @return
	 */
	protected OR _execute(OP params) {
		OR tor = createResultObject(); //new OnlineResult();
		JSONObject jo = new JSONObject();

		long ls = System.currentTimeMillis();
		String methodName = "-";
		try {
			// block WS call only if we really want to!!
//			boolean isUpdating = dataRepository.isUpdating();
			
//			if (isUpdating && params.getMethodParams() != null && ((Boolean)params.getMethodParams().get("blockIfUpdating"))) {
//				tor.setFailCode(JobFailureCode.UPDATE_IN_PROGRESS);
//				return tor;
//			}
			// Only a logged in user can use this <-- Don't check that yet
			UserAccount ua = null;
			if ( params.isUserRequired() && !userVerified( params.getUserCode() ) ) {
				throw new AccessDeniedException(); 
			}
			if ( params.getUserCode() != null && params.getUserCode() > 0 )
				ua = userAccountRepository.find( params.getUserCode() );
			if ( !TLZUtils.isEmpty( params.getUsername() ) )
				ua = userAccountRepository.findByUsername( params.getUsername() );
			methodName = params.getMethod();
			Method m = null;
			
			try {
				if ( ua != null )
					m = getDataRepository().getClass().getDeclaredMethod(methodName, UserAccount.class, Map.class);
				else
					m = getDataRepository().getClass().getDeclaredMethod(methodName, Map.class);
			} catch (Exception e) {
				LogManager.getLogger().error("methodName: " + methodName + " not found. Looking in Base class");
				if ( ua != null )
					m = getDataRepository().getClass().getSuperclass().getDeclaredMethod(methodName, UserAccount.class, Map.class);
				else
					m = getDataRepository().getClass().getSuperclass().getDeclaredMethod(methodName, Map.class);
			}
			if ( ua != null ) {
				tor.setResultObject(
						m.invoke(
								getDataRepository(), ua, params.getMethodParams()
								)
						);
			} else 
				tor.setResultObject(
						m.invoke(
								getDataRepository(), params.getMethodParams()
								)
						);

		} catch (AccessDeniedException e) {
			LogManager.getLogger().error("WS error: ", e);
			jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			tor.setFailCode( getGenericFailCode() );
			tor.setResultObject( jo.toString() );
		} catch (SecurityException e) {
			e.printStackTrace();
			jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			tor.setFailCode( getGenericFailCode() );
			tor.setResultObject( jo.toString() );
		} catch (NoSuchMethodException e) {
			LogManager.getLogger().error("WS error: ", e);
			jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			tor.setFailCode( getGenericFailCode() );
			tor.setResultObject( jo.toString() );
		} catch (IllegalArgumentException e) {
			LogManager.getLogger().error("WS error: ", e);
			jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			tor.setFailCode( getGenericFailCode() );
			tor.setResultObject( jo.toString() );
		} catch (IllegalAccessException e) {
			LogManager.getLogger().error("WS error: ", e);
			jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			tor.setFailCode( getGenericFailCode() );
			tor.setResultObject( jo.toString() );
		} catch (InvocationTargetException e) {
			LogManager.getLogger().error("WS error: ", e);
			if (e.getTargetException() != null) {
				if ( e.getTargetException() instanceof ItemLockedException) {
					jo.put("lockedBy", ((ItemLockedException)e.getTargetException()).getLockedBy());
					tor.setFailCode( getItemLockedFailCode() );
					tor.setResultObject( jo.toString() );
				}
				else if ( e.getTargetException() instanceof RuntimeException 
						&& e.getTargetException().getMessage() != null) // without this Custom RuntimeExceptions (like PendingEvaluationExistsException) were not handled correctly
					jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getTargetException().getMessage() );
				else
					jo.put("failMsg", e.getTargetException().getClass().getSimpleName() + ": " + e.getTargetException().getMessage()  );
			} else {
				jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			}
			tor.setResultObject( jo.toString() );
			if (tor.getFailCode() == null)
				tor.setFailCode( getGenericFailCode() );
		} catch (Exception e) {
			LogManager.getLogger().error("WS error: ", e);
			jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			tor.setFailCode( getGenericFailCode() );
			tor.setResultObject( jo.toString() );
		}
		finally {
		}
		long lf = System.currentTimeMillis();
		LogManager.getLogger().info(methodName + " WS duration: " +  (lf - ls));
		return tor;
	}

	protected abstract Object getGenericFailCode();
	protected abstract Object getItemLockedFailCode();

	/**
	 * @param userCode
	 * @return true if user is verified in backend, thus the request can be processed!
	 */
	protected abstract boolean userVerified(Integer userCode);

	protected abstract OR createResultObject();

//	@Transactional(propagation=Propagation.REQUIRES_NEW)
//	public User login(Credentials credentials) throws InvalidCredentialsException, MultipleLoginException, UserInactiveException {
//		User u = dataRepository.login( credentials );
//		return u;
//	}

}
