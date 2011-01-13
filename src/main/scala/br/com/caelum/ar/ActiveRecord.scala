package br.com.caelum.ar

object ActiveRecord {
	implicit def objectToActiveRecord[T](obj:T) = new ActiveRecord[T](obj)
}

class ActiveRecord[T](obj:T) {

	def save = {
		Sessions.get.save(obj)
		obj.asInstanceOf[T]
	}
	
	def update = {
			Sessions.get.update(obj)
			obj.asInstanceOf[T]
	}
	
	def delete = Sessions.get.delete(obj)			
	
}
