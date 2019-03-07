package com.tooooolazy.ws;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.json.JSONObject;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tooooolazy.data.interfaces.DataHandlerClient;
import com.tooooolazy.data.services.beans.JobFailureCode;
import com.tooooolazy.data.services.beans.OnlineBaseResult;
import com.tooooolazy.data.services.beans.OnlineParams;
import com.tooooolazy.domain.DataBaseRepository;
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
public abstract class WsBaseDataHandler<OR extends OnlineBaseResult> implements DataHandlerClient<OR> {
	private static org.apache.logging.log4j.Logger logger = LogManager.getLogger();

	protected final DataBaseRepository dataRepository;

	public WsBaseDataHandler( final DataBaseRepository dataRepository ) {
		this.dataRepository = dataRepository;
	}
    
	@Transactional(propagation=Propagation.NOT_SUPPORTED)
	public OR execute(OnlineParams params) {
		return _execute(params);
	}
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public OR executeUpdate(OnlineParams params) {
		return _execute(params);
	}
	/**
	 * Uses reflection to execute a method of {@link DataBaseRepository}.
	 * Depending of the method that called it, either {@link #execute(OnlineParams)} or {@link #executeUpdate(OnlineParams)},
	 * the method called by reflection will NOT <b>or</b> will be wrapped in a Transaction. 
	 * @param params
	 * @return
	 */
	protected OR _execute(OnlineParams params) {
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
//			UserAccount ua = null;
//			if ( params.getUserCode() != null)
//				ua = userAccountRepository.find( params.getUserCode() );
			methodName = params.getMethod();
			Method m = DataBaseRepository.class.getDeclaredMethod(methodName, Map.class);
			tor.setResultObject(
					m.invoke(
							dataRepository, params.getMethodParams()
							)
					);

		} catch (AccessDeniedException e) {
			LogManager.getLogger().error("WS error: ", e);
			jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			tor.setFailCode(JobFailureCode.GENERIC);
			tor.setResultObject( jo.toString() );
		} catch (SecurityException e) {
			e.printStackTrace();
			jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			tor.setFailCode(JobFailureCode.GENERIC);
			tor.setResultObject( jo.toString() );
		} catch (NoSuchMethodException e) {
			LogManager.getLogger().error("WS error: ", e);
			jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			tor.setFailCode(JobFailureCode.GENERIC);
			tor.setResultObject( jo.toString() );
		} catch (IllegalArgumentException e) {
			LogManager.getLogger().error("WS error: ", e);
			jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			tor.setFailCode(JobFailureCode.GENERIC);
			tor.setResultObject( jo.toString() );
		} catch (IllegalAccessException e) {
			LogManager.getLogger().error("WS error: ", e);
			jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			tor.setFailCode(JobFailureCode.GENERIC);
			tor.setResultObject( jo.toString() );
		} catch (InvocationTargetException e) {
			LogManager.getLogger().error("WS error: ", e);
			if (e.getTargetException() != null) {
				if ( e.getTargetException() instanceof ItemLockedException) {
					jo.put("lockedBy", ((ItemLockedException)e.getTargetException()).getLockedBy());
					tor.setFailCode(JobFailureCode.ITEM_LOCKED);
					tor.setResultObject( jo.toString() );
				}
				else if ( e.getTargetException() instanceof RuntimeException 
						&& e.getTargetException().getMessage() != null) // without this Custom RuntimeExceptions (like PendingEvaluationExistsException) were not handled correctly
					jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getTargetException().getMessage() );
				else
					jo.put("failMsg", e.getTargetException().getClass().getSimpleName() );
			} else {
				jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			}
			tor.setResultObject( jo.toString() );
			if (tor.getFailCode() == null)
				tor.setFailCode(JobFailureCode.GENERIC);
		} catch (Exception e) {
			LogManager.getLogger().error("WS error: ", e);
			jo.put("failMsg", e.getClass().getSimpleName() + ": " + e.getMessage());
			tor.setFailCode(JobFailureCode.GENERIC);
			tor.setResultObject( jo.toString() );
		}
		finally {
		}
		long lf = System.currentTimeMillis();
		LogManager.getLogger().info(methodName + " WS duration: " +  (lf - ls));
		return tor;
	}
	protected abstract OR createResultObject();

//	@Transactional(propagation=Propagation.REQUIRES_NEW)
//	public User login(Credentials credentials) throws InvalidCredentialsException, MultipleLoginException, UserInactiveException {
//		User u = dataRepository.login( credentials );
//		return u;
//	}

}
