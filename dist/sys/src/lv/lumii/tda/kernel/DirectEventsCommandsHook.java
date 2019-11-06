package lv.lumii.tda.kernel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.util.StackTrace;

import lv.lumii.tda.raapi.IEngineAdapter;
import lv.lumii.tda.raapi.ITransformationAdapter;
import lv.lumii.tda.raapi.RAAPI;
import lv.lumii.tda.raapi.RAAPIHelper;

public class DirectEventsCommandsHook implements IEventsCommandsHook {

	private static Logger logger =  LoggerFactory.getLogger(DirectEventsCommandsHook.class);
	
	@Override
	public boolean handleEvent(TDAKernel raapi, long rEvent) {
		IEventsCommandsHelper adapters = raapi;
		
		String[] handlers = adapters.getEventHandlers(rEvent);
		if (handlers==null)
			return true;
		
		boolean retVal = true;
		for (String transformationName : handlers) {
			String type = TDAKernel.getAdapterTypeFromURI(transformationName);
			String location = TDAKernel.getLocationFromURI(transformationName);
			
			if (type == null)
				return false;
			
			final long transformationArgument;
			int i = type.indexOf('(');
			if ((i>=0) && type.endsWith(")")) {
				long parsed;
				try {
					parsed = Long.parseLong(type.substring(i+1, type.length()-1));
				}
				catch (Throwable t) {
					parsed = 0;
				}
				transformationArgument = parsed;
				type = type.substring(0, i);
			}
			else
				transformationArgument = rEvent;			
			
			ITransformationAdapter adapter = adapters.getTransformationAdapter(type);
			if (adapter == null) {
				retVal = false;
			}
			
			try {
				if (!adapter.launchTransformation(location, transformationArgument))
					retVal = false;
			}
			catch(Throwable t) {
				StackTrace.log(t, logger);
				retVal = false;
			}
			

		}
		return retVal;
	}

	@Override
	public boolean executeCommand(TDAKernel raapi, long rCommand) {
		IEventsCommandsHelper adapters = raapi;
		String className = RAAPIHelper.getObjectClassName(raapi, rCommand);
		
		if (className == null)
			return false;
		
		String engineName = adapters.getEngineForEventOrCommand(className);
		if (engineName == null)
			return false;
		
		IEngineAdapter adapter = adapters.getEngineAdapter(engineName);
		if (adapter == null)
			return false;
		return adapter.executeCommand(rCommand);
	}

}
