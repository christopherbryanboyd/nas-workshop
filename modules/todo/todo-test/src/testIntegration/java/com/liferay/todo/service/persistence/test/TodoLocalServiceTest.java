/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.todo.service.persistence.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;
import com.liferay.todo.model.Todo;
import com.liferay.todo.service.TodoLocalServiceUtil;
import com.liferay.todo.service.persistence.TodoPersistence;
import com.liferay.todo.service.persistence.TodoUtil;

@RunWith(Arquillian.class)
public class TodoLocalServiceTest {
	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule = new AggregateTestRule(new LiferayIntegrationTestRule(),
			PersistenceTestRule.INSTANCE,
			new TransactionalTestRule(Propagation.REQUIRED,
				"com.liferay.todo.service"));

	@Before
	public void setUp() throws Exception {
		_persistence = TodoUtil.getPersistence();

		for (int i=0; i<10; i++) {
			addTodo();
		}
	}

	@After
	public void tearDown() throws Exception {
		Iterator<Todo> iterator = _todos.iterator();

		while (iterator.hasNext()) {
			_persistence.remove(iterator.next());

			iterator.remove();
		}
	}

	@Test
	public void testWhatsBestNext() throws Exception {
		Todo whatIsBestNext = TodoLocalServiceUtil.whatIsBestNext();

		Assert.assertNotNull(whatIsBestNext);
	}


	protected Todo addTodo() throws Exception {
		long pk = RandomTestUtil.nextLong();

		Todo todo = _persistence.create(pk);

		todo.setUuid(RandomTestUtil.randomString());

		todo.setGroupId(RandomTestUtil.nextLong());

		todo.setCompanyId(RandomTestUtil.nextLong());

		todo.setUserId(RandomTestUtil.nextLong());

		todo.setUserName(RandomTestUtil.randomString());

		todo.setCreateDate(RandomTestUtil.nextDate());

		todo.setModifiedDate(RandomTestUtil.nextDate());

		todo.setTitle(RandomTestUtil.randomString());

		todo.setDescription(RandomTestUtil.randomString());

		todo.setCompleted(RandomTestUtil.randomBoolean());

		_todos.add(_persistence.update(todo));

		return todo;
	}

	private List<Todo> _todos = new ArrayList<Todo>();
	private TodoPersistence _persistence;
}