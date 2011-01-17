package br.com.caelum.ar

import org.hibernate.criterion.Projections
import java.io.Serializable
import javax.persistence.Entity
import java.util.List
object ActiveRecord {
	implicit def objectToActiveRecord[T <: Object](obj:T) = new ActiveRecord[T](obj)
}

class ActiveRecord[T](obj:Object) {	
	var klass = obj.getClass
	if (!obj.getClass.isAnnotationPresent(classOf[Entity])){
			try{
				klass = obj.getClass().getMethod("apply").getReturnType()
			}
			catch {
				case ex:NoSuchMethodException => throw new IllegalStateException(obj.getClass() + " does not have @Entity annotation")
			}
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
	
	def all = Sessions.get.createCriteria(klass).list.asInstanceOf[List[T]]
	
	def count = Sessions.get.createCriteria(klass).setProjection(Projections.rowCount).uniqueResult.asInstanceOf[Number].intValue
	
	def find(id:Serializable) = Sessions.get.load(klass,id).asInstanceOf[T]
	
	def last:Option[T] = {
		var size = count.intValue
		if(size>0){
			return Some(Sessions.get.createCriteria(klass).setFirstResult(size-1).uniqueResult.asInstanceOf[T])
		}
		return None
	}
	
	def first = Sessions.get.createCriteria(klass).setMaxResults(1).uniqueResult.asInstanceOf[T]
	
}
