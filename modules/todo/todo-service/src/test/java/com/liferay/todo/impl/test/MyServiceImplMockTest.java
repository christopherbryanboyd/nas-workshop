package com.liferay.todo.impl.test;

import org.junit.Assert;
import org.junit.Test;

import com.liferay.todo.impl.MyServiceImpl;
import com.liferay.todo.model.Todo;

public class MyServiceImplMockTest {


//	@Test
	public void myServiceImplTest() throws Exception {
		MyServiceImpl myServiceImpl = new MyServiceImpl();

		myServiceImpl.stillNeedsWork(todo);

		Assert.assertFalse(todo.getCompleted());
	}


	Todo todo = null;

}
