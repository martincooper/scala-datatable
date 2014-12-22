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

/** Trait defining add / update / insert / delete. */
trait Modifiable[I, V, R] {
  def add(value: V): R
  def replace(index: I, values: V): R
  def insert(index: I, value: V): R
  def remove(index: I): R
}

/** Modifiable, with an integer indexer. */
trait ModifiableByIndex[V, R] extends Modifiable[Int, V, R] { }

/** Modifiable, with additional string (name) indexer. */
trait ModifiableByName[V, R] extends ModifiableByIndex[V, R] {
  def replace(index: String, values: V): R
  def insert(index: String, value: V): R
  def remove(index: String): R
}