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

import com.github.martincooper.datatable.GenericColumn

import scala.collection.{ mutable, IndexedSeqLike }

/** Implements a collection GenericColumns with additional modification methods implemented. */
class DataColumnCollection(dataColumns: Iterable[GenericColumn])
  extends IndexedSeq[GenericColumn] with IndexedSeqLike[GenericColumn, DataColumnCollection] {

  def columns = dataColumns.toVector

  override def length: Int = columns.length

  override def apply(idx: Int): GenericColumn = columns(idx)

  override def newBuilder: mutable.Builder[GenericColumn, DataColumnCollection] = DataColumnCollection.newBuilder
}

object DataColumnCollection {

  /** Builder for a new DataColumnCollection */
  def newBuilder: mutable.Builder[GenericColumn, DataColumnCollection] =
    Vector.newBuilder[GenericColumn] mapResult(vector => new DataColumnCollection(vector))
}