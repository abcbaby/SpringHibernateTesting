package com.dragonzone.domain;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dragonzone.db.InMemoryDbTestBase;

/**
 * Save an item and list Person's with no criteria, twice. Should both see 1
 * item because DB is scrubbed each time.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class PersonListOfOneTest extends InMemoryDbTestBase {
	@Test
	public void list1() {
		Person person = new Person();
		person.setFirstName("Dragon");
		person.setLastName("Do");
		getDaoFactoy().write(person);
		List<Person> personList = getDaoFactoy().list(Person.class, null, 8);
		Assert.assertTrue(!personList.isEmpty());
	}

	@Test
	public void list2() {
		Person person = new Person();
		person.setFirstName("Zone");
		person.setLastName("Out");
		getDaoFactoy().write(person);
		List<Person> personList = getDaoFactoy().list(Person.class, null, 8);
		Assert.assertTrue(!personList.isEmpty());
	}
}