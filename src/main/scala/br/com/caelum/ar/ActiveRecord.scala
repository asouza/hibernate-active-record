package br.com.caelum.ar

import java.io.Serializable
import javax.persistence.Entity
import java.util.List
object ActiveRecord {
	implicit def objectToActiveRecord[T <: Object](obj:T) = new ActiveRecord[T](obj)
}

class ActiveRecord[T](obj:Object) {	
	if (!obj.getClass.isAnnotationPresent(classOf[Entity])){
		throw new IllegalStateException(obj.getClass() + " does not have @Entity annotation");
	}
	
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
	
	def find(id:Serializable) = Sessions.get.load(obj.getClass,id)
	
}
