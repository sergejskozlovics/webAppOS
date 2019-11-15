package lv.lumii.tda.kernel;

import org.webappos.webmem.IWebMemory;

public interface IEventsCommandsHook {
	public boolean handleEvent(final IWebMemory webmem, final long rEvent);
	public boolean executeCommand(final IWebMemory webmem, final long rCommand);
}
