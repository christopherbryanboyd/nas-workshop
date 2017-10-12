package com.liferay.todo.api;

import com.liferay.todo.model.Todo;

public interface MyService {

	public void complete(Todo todo);

	public void stillNeedsWork(Todo todo);

}
