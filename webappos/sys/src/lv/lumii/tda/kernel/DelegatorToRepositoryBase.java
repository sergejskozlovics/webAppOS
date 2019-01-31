package lv.lumii.tda.kernel;

import lv.lumii.tda.raapi.*;

public class DelegatorToRepositoryBase extends DelegatorBase<IRepository> implements IRepository {
	
	IRepository delegate = null;
	
	public DelegatorToRepositoryBase()
	{
	}
	
	public void setDelegate(IRepository _delegate)
	{
		delegate = _delegate;
	}	

	
	@Override
	public IRepository getDelegate() {
		return delegate;
	}
	
	@Override
	public boolean exists(String location) {
		return getDelegate().exists(location);
	}

	@Override
	public boolean open(String location) {
		return getDelegate().open(location);
	}

	@Override
	public void close() {
		getDelegate().close();
	}

	@Override
	public boolean startSave() {
		return getDelegate().startSave();
	}

	@Override
	public boolean finishSave() {
		return getDelegate().finishSave();
	}

	@Override
	public boolean cancelSave() {
		return getDelegate().cancelSave();
	}

	@Override
	public boolean drop(String location) {
		return getDelegate().drop(location);
	}

}
