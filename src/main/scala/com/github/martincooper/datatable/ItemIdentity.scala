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

/** Specifies how an item is identified. */
sealed trait ItemIdentity
case class ItemByName(columnName: String) extends ItemIdentity
case class ItemByIndex(columnIndex: Int) extends ItemIdentity

object ItemIdentityImplicits {

  /** Implicit conversion from string (name) into IdentityByName item. */
  implicit def StringToItemName(itemName : String): ItemIdentity = new ItemByName(itemName)

  /** Implicit conversion from integer (index) into IdentityByIndex item. */
  implicit def IntegerToItemName(itemIndex : Int): ItemIdentity = new ItemByIndex(itemIndex)
}