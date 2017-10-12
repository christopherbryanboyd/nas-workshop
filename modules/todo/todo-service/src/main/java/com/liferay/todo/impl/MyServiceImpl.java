package com.liferay.todo.impl;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.todo.api.MyService;
import com.liferay.todo.model.Todo;
import com.liferay.todo.service.TodoLocalService;

@Component
public class MyServiceImpl implements MyService {

	@Override
	public void complete(Todo todo) {
		todo.setCompleted(true);
		todoService.updateTodo(todo);
	}

	@Override
	public void stillNeedsWork(Todo todo) {
		todo.setCompleted(false);
		todoService.updateTodo(todo);
	}

	@Reference
	TodoLocalService todoService;
}
