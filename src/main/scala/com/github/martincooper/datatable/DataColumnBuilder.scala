/**
 * Copyright 2014 Martin Cooper
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
