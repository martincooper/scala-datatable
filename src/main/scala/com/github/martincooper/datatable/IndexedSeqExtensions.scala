/**
 * Copyright 2014-2015 Martin Cooper
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

import scala.util.{ Success, Failure, Try }

object IndexedSeqExtensions {

  /** Returns a new IndexedSeq[T] with the new value appended to the end. */
  def addItem[T](items: IndexedSeq[T], value: T): Try[IndexedSeq[T]] = {
    Success(items :+ value)
  }

  /** Returns a new IndexedSeq[T] with the value at the specified index removed. */
  def removeItem[T](items: IndexedSeq[T], index: Int): Try[IndexedSeq[T]] = {
    outOfBounds[T](items, index) match {
      case true => Failure(DataTableException("Item index out of bounds for remove."))
      case false =>
        val (dataStart, dataEnd) = items.splitAt(index)
        Success(dataStart ++ dataEnd.tail)
    }
  }

  /** Returns a new IndexedSeq[T] with the value replaced at the specified index. */
  def replaceItem[T](items: IndexedSeq[T], index: Int, value: T): Try[IndexedSeq[T]] = {
    outOfBounds[T](items, index) match {
      case true => Failure(DataTableException("Item index out of bounds for replace."))
      case false =>
        Success(items.updated(index, value))
    }
  }

  /** Returns a new IndexedSeq[T] with the value inserted at the specified index. */
  def insertItem[T](items: IndexedSeq[T], index: Int, value: T): Try[IndexedSeq[T]] = {
    outOfBounds[T](items, index) match {
      case true => Failure(DataTableException("Item index out of bounds for insert."))
      case false =>
        val (dataStart, dataEnd) = items.splitAt(index)
        Success(dataStart ++ (value +: dataEnd))
    }
  }

  /** Returns true if the specified index is out of bounds, assuming zero based. */
  def outOfBounds[T](items: IndexedSeq[T], index: Int): Boolean = {
    outOfBounds(items.length, index)
  }

  /** Returns true if the specified index is out of bounds. */
  def outOfBounds(itemCount: Int, index: Int): Boolean = {
    itemCount == 0 || (index < 0 || index >= itemCount)
  }
}
