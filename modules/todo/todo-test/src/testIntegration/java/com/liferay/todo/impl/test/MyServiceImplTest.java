package com.liferay.todo.impl.test;

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
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PersistenceTestRule;
import com.liferay.portal.test.rule.TransactionalTestRule;
import com.liferay.todo.api.MyService;
import com.liferay.todo.model.Todo;
import com.liferay.todo.service.persistence.TodoPersistence;
import com.liferay.todo.service.persistence.TodoUtil;

@RunWith(Arquillian.class)
public class MyServiceImplTest {

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
	public void testComplete() throws Exception {
		BundleContext context = FrameworkUtil.getBundle(MyServiceImplTest.class).getBundleContext();

		ServiceTracker<MyService, MyService> st = new ServiceTracker<MyService,MyService>(context, MyService.class, null);
		st.open();

		MyService myService = st.getService();

		Todo todo = _persistence.findAll(0, 1).get(0);

		myService.complete(todo);

		Assert.assertTrue(todo.getCompleted());
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

		todo.setCompleted(false);

		_todos.add(_persistence.update(todo));

		return todo;
	}

	private List<Todo> _todos = new ArrayList<Todo>();
	private TodoPersistence _persistence;
}
