package lv.lumii.tda.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicCast {

	private static class MyInvocationHandler implements InvocationHandler
	{
		private Object delegate;
		public MyInvocationHandler(Object _delegate)
		{
			delegate = _delegate;
		}
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			Method delegateMethod = delegate.getClass().getMethod(method.getName(), method.getParameterTypes());
			if (delegateMethod == null)
				delegateMethod = delegate.getClass().getMethod("_"+method.getName(), method.getParameterTypes());
			return delegateMethod.invoke(delegate, args);
		}
	}

	
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object delegate, Class<T> type)
	{		
		if (type.isInstance(delegate))
			return (T)delegate;
		else {
			return (T) java.lang.reflect.Proxy.newProxyInstance(type.getClassLoader(),
					new Class[] { type },
                    new MyInvocationHandler(delegate));				
		}
	}
}
