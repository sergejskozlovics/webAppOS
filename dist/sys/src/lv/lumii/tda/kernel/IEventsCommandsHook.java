package lv.lumii.tda.kernel;


public interface IEventsCommandsHook {
	public boolean handleEvent(final TDAKernel tdaKernel, final long rEvent);
	public boolean executeCommand(final TDAKernel tdaKernel, final long rCommand);
}
