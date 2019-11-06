package lv.lumii.tda.raapi;

import lv.lumii.tda.raapi.RAAPI_Synchronizer;

public interface RAAPI_Synchronizable {
	public void syncAll(RAAPI_Synchronizer synchronizer, long bitsValues);
}
