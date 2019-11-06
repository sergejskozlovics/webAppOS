package lv.lumii.tda.raapi;

public interface IAdvancedTransformationAdapter extends ITransformationAdapter {

	/**
	 * Launches the transformation at the given location taking a string as argument and returning string.
	 * @param location an adapter-specific location of the transformation (e.g., a file name containing the transformation definition)
	 * @param argument specifies any string (e.g., a stringified JSON)
	 * @param login specifies user name, who launched this transformation (optional, can be null)
	 * @return a string that is a result of transformation.
	 */
	String launchStringTransformation (String location, String argument, String login);

}
