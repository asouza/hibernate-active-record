package br.com.caelum.ar

import org.hibernate.Session
object Sessions{
	val perThreadSessions = new ThreadLocal[Session]
	
	def put(session:Session) {
		perThreadSessions.set(session)
	}
	
	def remove(session:Session){
		perThreadSessions.remove()
	}
	
	def get = perThreadSessions.get
}
