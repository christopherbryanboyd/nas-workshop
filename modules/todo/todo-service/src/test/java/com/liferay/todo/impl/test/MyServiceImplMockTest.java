package com.liferay.todo.impl.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.powermock.reflect.Whitebox;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import org.easymock.IAnswer;

import com.liferay.todo.impl.MyServiceImpl;
import com.liferay.todo.model.Todo;
import com.liferay.todo.service.TodoLocalService;

public class MyServiceImplMockTest {


	@Before
	public void setUp() {
		mockTodo();
		
		mockTodoLocalService();
		
		mockMyServiceImpl();
	}
	
	private void mockTodo() 
	{
		todo = mock(Todo.class);
		
		todo.setCompleted(Boolean.TRUE);
		
		expectLastCall().andVoid().once();
		
		expect(todo.getCompleted()).andReturn(Boolean.TRUE).once();
		
		replay(todo);
	}
	
	private void mockTodoLocalService()
	{
		todoLocalService = mock(TodoLocalService.class);
		
		expect(todoLocalService.updateTodo(isA(Todo.class))).andAnswer(new IAnswer<Todo>() {

			@Override
			public Todo answer() throws Throwable {
				Todo todo = (Todo) getCurrentArguments()[0];
				
				assertTrue(todo.getCompleted());
				
				return todo;

			}
		}).once();
		
		replay(todoLocalService);
	}
	
	private void mockMyServiceImpl() 
	{
		myServiceImpl = mock(MyServiceImpl.class);
		
		Whitebox.setInternalState(myServiceImpl, "todoService", todoLocalService);

		myServiceImpl.stillNeedsWork(isA(Todo.class));
		
		expectLastCall().andVoid().once();
		
		replay(myServiceImpl);
	}
	
	@Test
	public void myServiceImplTest() throws Exception {

		myServiceImpl.stillNeedsWork(todo);
		
		PowerMock.verifyAll();
	}
	
	private TodoLocalService todoLocalService = null;
	
	private MyServiceImpl myServiceImpl = null;

	private Todo todo = null;

}
