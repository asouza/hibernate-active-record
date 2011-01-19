package br.com.caelum.ar

import org.hibernate.criterion.Projections
import java.io.Serializable
import javax.persistence.Entity
import java.util.List
import scala.Iterable
object ActiveRecord {
	implicit def objectToActiveRecord[T](obj:T) = new ActiveRecord[T](obj)
}

class ActiveRecord[T](obj:T) {	
	import br.com.caelum.ar.ActiveRecord._
	val asJavaObject =  obj.asInstanceOf[Object]
	var klass = asJavaObject.getClass
	if (!klass.isAnnotationPresent(classOf[Entity]) && !obj.isInstanceOf[Iterable[_]]){
			try{
				klass = asJavaObject.getClass.getMethod("apply").getReturnType()
			}
			catch {
				case ex:NoSuchMethodException => throw new IllegalStateException(klass + " does not have @Entity annotation")
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
	
	def delete  {
		if(obj.isInstanceOf[Iterable[_]]){
			val objects = obj.asInstanceOf[Iterable[_]]
			objects.foreach(_.delete)
		}
		else{
			Sessions.get.delete(obj)
		}
	}
	
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
