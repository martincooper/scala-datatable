package com.github.martincooper.datatable

import scala.reflect.runtime._
import scala.reflect.runtime.universe._

object DataColumnBuilder {

  def createColumnOfType[T: TypeTag](args: AnyRef*)(ctor: Int = 0): DataColumn[T] = {
    val tt = typeTag[DataColumn[T]]

    currentMirror.reflectClass(tt.tpe.typeSymbol.asClass).reflectConstructor(
      tt.tpe.members.filter(m =>
        m.isMethod && m.asMethod.isConstructor
      ).iterator.toSeq(ctor).asMethod
    )(args: _*).asInstanceOf[DataColumn[T]]
  }

  def createInstance[T: TypeTag](args: AnyRef*)(ctor: Int = 0): T = {
    val tt = typeTag[T]

    currentMirror.reflectClass(tt.tpe.typeSymbol.asClass).reflectConstructor(
      tt.tpe.members.filter(m =>
        m.isMethod && m.asMethod.isConstructor
      ).iterator.toSeq(ctor).asMethod
    )(args: _*).asInstanceOf[T]
  }

  def createColumnOfType(typeName: String): GenericColumn = {

    val mirror = universe.runtimeMirror(getClass.getClassLoader)
    val classInstance = Class.forName("Integer")
    val classSymbol = mirror.classSymbol(classInstance)

    val classType = classSymbol.toType; //println(classType) gives ABC
    println(classType)

    classInstance.newInstance().asInstanceOf[GenericColumn]
  }

}
