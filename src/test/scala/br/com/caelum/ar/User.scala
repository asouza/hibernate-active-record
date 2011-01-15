package br.com.caelum.ar

import javax.persistence.{ Entity, GeneratedValue, Id };


@Entity
case class User {
  @GeneratedValue
  @Id
  var id: Integer = _
  var name: String = _
  var age: Int = _
  

  def this(id:Integer,name:String,age:Int) = {
    this()
    this.id  = id
    this.age  = age
    this.name  = name
    
  }
  
  def this(name: String, age: Int) = this(null, name, age)


}