package org.webappos.webcaller;

import lv.lumii.tda.raapi.RAAPI;

public interface ITdaWebCallsAdapter {
	

	public void tdacall(String resolvedLocation, long rObject, RAAPI raapi, String project_id, String appFullName, String login);

}
