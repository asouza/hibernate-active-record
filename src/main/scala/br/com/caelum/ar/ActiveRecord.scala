package br.com.caelum.ar
import javax.persistence.Entity
import java.io.Serializable
import java.util.List
import org.hibernate.criterion.Projections
object ActiveRecord {
	implicit def objectToActiveRecord[T](obj:T) = new ActiveRecord[T](obj)	
}

class ActiveRecord[T](obj:T) {	
	import br.com.caelum.ar.ActiveRecord._
	val asJavaObject =  obj.asInstanceOf[Object]
	var klass = asJavaObject.getClass
	if (!klass.isAnnotationPresent(classOf[Entity]) && !obj.isInstanceOf[scala.Iterable[_]] && !obj.isInstanceOf[java.lang.Iterable[_]]){
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
		import scala.collection.JavaConversions._
		obj match {
			case items:scala.Iterable[_] => items.foreach(_.delete)
			case items:java.lang.Iterable[_] => items.foreach(_.delete)
			case _ => Sessions.get.delete(obj)
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
