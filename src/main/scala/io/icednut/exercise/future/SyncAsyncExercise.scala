package io.icednut.exercise.future

import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Success

case class Item(id: Long, name: String, categoryId: Long)

case class Category(id: Long, name: String)

class CategoryRepository extends LazyLogging {
  def asyncFindByItemName(itemName: String)(implicit ec: ExecutionContext): Future[Category] = Future {
    Thread.sleep(2000)
    logger.info("CategoryRepository.asyncFindByItemName call")
    val categoryId = 1
    Category(categoryId, s"Category${categoryId}")
  }
}

class ItemRepository extends LazyLogging {
  def asyncFindByCategoryId(categoryId: Long)(implicit ec: ExecutionContext): Future[Iterable[Item]] = Future {
    Thread.sleep(2000)
    logger.info("ItemRepository.asyncFindByCategoryId call")
    Iterable(
      Item(1, "Item1", categoryId),
      Item(2, "Item2", categoryId),
      Item(3, "Item3", categoryId)
    )
  }

  def asyncFindById(itemId: Long)(implicit ec: ExecutionContext): Future[Item] = Future {
    Thread.sleep(2000)
    logger.info("ItemRepository.asyncFindById call")
    Item(itemId, s"Item${itemId}", 1)
  }


  def findById(itemId: Long): Item = {
    Thread.sleep(2000)
    logger.info("ItemRepository.findById call")
    Item(itemId, name = s"Item${itemId}", ???)
  }
}

object FutureAndMonixTaskExercise extends LazyLogging {

  def main(args: Array[String]): Unit = {
    import ExecutionContext.Implicits.global

    val itemRepository = new ItemRepository
    val categoryRepository = new CategoryRepository
    val itemId = 1L

    // blocking
    val syncItem: Item = itemRepository.findById(itemId)
    logger.info(s"syncItem: ${syncItem}")

    // non-blocking
    val asyncItem: Future[Item] = itemRepository.asyncFindById(itemId)
    asyncItem.onComplete {
      case Success(item) => logger.info(s"asyncItem: ${item}")
    }


    // sync
//    val itemSizeForCategory1: Future[Int] = for {
//      item <- itemRepository.asyncFindById(itemId)
//      category <- categoryRepository.asyncFindByItemName(item.name)
//      items <- itemRepository.asyncFindByCategoryId(category.id)
//    } yield {
//      items.size
//    }

    // async
//    val itemFuture = itemRepository.asyncFindById(itemId)
//    val categoryFuture = categoryRepository.asyncFindByItemName(item.name)
//    val itemCategoryFuture = itemRepository.asyncFindByCategoryId(category.id)
//    val itemSizeForCategory2: Future[Int] = for {
//      item <- itemFuture
//      category <- categoryFuture
//      items <- itemCategoryFuture
//    } yield {
//      items.size
//    }

    Thread.sleep(5 * 1000)
  }
}
