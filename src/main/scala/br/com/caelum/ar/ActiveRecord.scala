package br.com.caelum.ar

import javax.persistence.Entity
import java.util.List
object ActiveRecord {
	implicit def objectToActiveRecord[T <: Object](obj:T) = new ActiveRecord[T](obj)
}

class ActiveRecord[T](obj:Object) {
	require(obj.getClass.isAnnotationPresent(classOf[Entity]))
	
	def save = {		
		
		Sessions.get.save(obj)
		obj.asInstanceOf[T]
	}
	
	def update = {
			Sessions.get.update(obj)
			obj.asInstanceOf[T]
	}
	
	def delete = Sessions.get.delete(obj)
	
	def all = Sessions.get.createCriteria(obj.getClass).list.asInstanceOf[List[T]]
	
}
