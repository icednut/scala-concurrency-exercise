package io.icednut.exercise

package object common {
  type Catalog = String
  type Brand = String
  type Wish = String
  type Category = String
  type Detail = String
  type Certification = String

  case class Item(id: Int, catalogId: Int, brandId: Int)
}
