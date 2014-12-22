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

object VectorExtensions {

  /** Returns a new Vector[T] with the new value appended to the end. */
  def addItem[T](vector: Vector[T], index: Int): Vector[T] = {
    val (dataStart, dataEnd) = vector.splitAt(index)
    dataStart ++ dataEnd.tail
  }

  /** Returns a new Vector[T] with the value at the specified index removed. */
  def removeItem[T](vector: Vector[T], index: Int): Vector[T] = {
    val (dataStart, dataEnd) = vector.splitAt(index)
    dataStart ++ dataEnd.tail
  }

  /** Returns a new Vector[T] with the value replaced at the specified index. */
  def replaceItem[T](vector: Vector[T], index: Int, value: T): Vector[T] = {
    vector.updated(index, value)
  }

  /** Returns a new Vector[T] with the value inserted at the specified index. */
  def insertItem[T](vector: Vector[T], index: Int, value: T): Vector[T] = {
    val (dataStart, dataEnd) = vector.splitAt(index)
    dataStart ++ (value +: dataEnd)
  }
}
