package br.com.caelum.ar

import java.util.Arrays
import br.com.caelum.ar.Sessions._
import br.com.caelum.ar.ActiveRecord._
import br.com.caelum.hibernatequerydsl.PimpedSession._
import org.hibernate.Session
import org.hibernate.cfg.Configuration
import org.junit.{Before, After, Test}
import org.junit.Assert._

class ActiveRecordTest {

	private var session:Session = _
	
	@Before
	def setUp {
		val cfg = new Configuration();
		cfg.configure().setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:arDB");
		session = cfg.buildSessionFactory().openSession();
		session.beginTransaction();
		Sessions put session
	}

	@After
	def tearDown{
		if (session != null && session.getTransaction().isActive()) {
			session.getTransaction().rollback();
		}
		session.close
		Sessions remove
	}	
	
	@Test
	def shouldSaveObject {
		var user = new User("teste",15).save
		assertEquals(1,session.count[User])
		assertEquals(1,user.id)
	}
	
	@Test
	def shouldUpdateObject {	
		val user = new User("teste",15).save
		user.name = "Alberto"
		user.update
		assertEquals("Alberto",session.all[User].get(0).name)
	}
	
	@Test
	def shouldDeleteObject {	
		val user = new User("teste",15).save
		user.delete
		assertEquals(0,session.count[User])
	}
	
	@Test
	def shouldListAllObjects {			
		val user = new User("teste",15).save
		var users = User.all
		assertEquals(1,User.all.size)
	}
	
	@Test
	def shouldLoadById {	
		val user = new User("teste",15).save
		assertEquals(user,User.find(user.id))
	}
	
	@Test
	def shouldCount {	
		new User("teste",15).save
		new User("teste",15).save
		new User("teste",15).save
		assertEquals(3,User.count)
	}	
	
	@Test
	def shouldGetNoneForNoLast {
		assertEquals(None,User.last)
	}
	
	@Test
	def shouldGetLast {	
		val user = new User("teste",15).save
		val user2 = new User("teste",15).save
		assertEquals(Some(user),User.last)
	}
	
	@Test
	def shouldGetFirst {	
		val user = new User("teste",15).save
		val user2 = new User("teste",15).save
		assertEquals(user,User.first)
	}
	
	@Test
	def shouldDeleteAllInsideTheScalaIterable {	
		val user = new User("teste",15).save
		val user2 = new User("teste",15).save
		val users = List(user,user2)		
		users.delete
		assertEquals(0,session.count[User])
	}
	
	@Test
	def shouldDeleteAllInsideTheJavaIterable {
		val user = new User("teste",15).save
		val user2 = new User("teste",15).save
		val users = Arrays.asList(user,user2)
		users.delete
		assertEquals(0,session.count[User])
	}		
	
	
	@Test(expected = classOf[IllegalStateException])
	def shouldThrowIAEWhenInstanceIsNotAEntity {
		"dahfdjskfh".save
	}
	
	
}