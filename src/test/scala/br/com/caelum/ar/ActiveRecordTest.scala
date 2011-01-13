package br.com.caelum.ar
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
		Sessions remove session
	}	
	
	@Test
	def shouldSaveObject {
		var user = new User("teste",15).save
		assertEquals(1,session.all[User].size)
		assertEquals(1,user.getId)
	}
	
	@Test
	def shouldUpdateObject {	
		val user = new User("teste",15).save
		user setName "Alberto"
		user.update
		assertEquals("Alberto",session.all[User].get(0).getName)
	}
	
	@Test
	def shouldDeleteObject {	
		val user = new User("teste",15).save
		user.delete
		assertEquals(0,session.all[User].size)
	}
}