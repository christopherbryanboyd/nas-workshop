package com.liferay.todo.impl.test;

import org.junit.Before;
import org.junit.Test;
import org.powermock.api.easymock.PowerMock;
import org.powermock.reflect.Whitebox;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicBoolean;

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
		
		/**
		 * Used as a placeholder for the completed state,
		 * since {@link Todo} is an interface and holds no state.
		 */
		AtomicBoolean completed = new AtomicBoolean(Boolean.FALSE);
		
		todo.setCompleted(anyBoolean());
		
		expectLastCall().andAnswer(() ->
			{
				boolean parm = (boolean) getCurrentArguments()[0];
				// Store the state for later.
				completed.set(parm);
				
				/**
				 * Return null because 
				 * {@link Todo#setCompleted(boolean}
				 * does not return anything.
				 */
				return null;
			})
			.atLeastOnce();
		
		expect(todo.getCompleted())
			.andAnswer(() -> completed.get())
			.atLeastOnce();
		
		replay(todo);
		
		/*	
		 	// MORE STRICT VERSION
		 
			todo = strictMock(Todo.class);
			
			todo.setCompleted(Boolean.TRUE);
			
			expectLastCall()
				.andVoid()
				.once();
			
			expect(todo.getCompleted())
				.andReturn(Boolean.TRUE)
				.once();
			
			replay(todo);
		*/
	}
	
	private void mockTodoLocalService()
	{
		todoLocalService = mock(TodoLocalService.class);
		
		expect(todoLocalService.updateTodo(isA(Todo.class)))
			.andAnswer(() ->
			{
				/**
				 * Normally this is used to save the 
				 * {@link Todo} object.
				 * For our test, we'll just return
				 * what we received.
				 */
				Todo todo = (Todo) getCurrentArguments()[0];
				
				return todo;
	
			})
			.once();
		
		replay(todoLocalService);
	}
	
	private void mockMyServiceImpl() 
	{
		myServiceImpl = partialMockBuilder(MyServiceImpl.class)
			.createMock();
		
		/**
		 * Set the field in {@link MyServiceImpl}
		 * because OSGi isn't available to do it for us.
		 */
		Whitebox.setInternalState(myServiceImpl, "todoService", todoLocalService);
		
		/*
		 	// Commented out because this is the code we're testing.
		 	 
		 	myServiceImpl.stillNeedsWork(isA(Todo.class));
		
			expectLastCall().andVoid().once();
			
		*/
		
		replay(myServiceImpl);
	}

	@Test
	public void testCompletion() throws Exception {

		myServiceImpl.complete(todo);
		
		assertTrue(todo.getCompleted());
		
		/**
		 * This verifies all the expectations we built
		 * in our {@link #setUp()} method.
		 */
		PowerMock.verifyAll();
	}
	
	@Test
	public void testNeedsWork() throws Exception {

		myServiceImpl.stillNeedsWork(todo);
		
		assertFalse(todo.getCompleted());
		
		/**
		 * This verifies all the expectations we built
		 * in our {@link #setUp()} method.
		 */
		PowerMock.verifyAll();
	}
	
	private TodoLocalService todoLocalService = null;
	
	private MyServiceImpl myServiceImpl = null;

	private Todo todo = null;

}
