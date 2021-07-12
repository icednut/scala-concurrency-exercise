package io.icednut.exercise.monix

import cats.implicits._
import cats.{Monad, _}
import model._
import io.icednut.exercise.monix.util.log
import monix.eval.Task

import java.text.SimpleDateFormat
import java.util.Date
import scala.concurrent.duration._
import scala.concurrent.{Await, Awaitable, Future}
import scala.language.postfixOps

object util {
  val formatter = new SimpleDateFormat("HH:mm:ss")

  def log(str: => String): Unit =
    println(s"${formatter.format(new Date())} - $str")

}

object model {
  type Catalog = String
  type Brand = String
  type Wish = String
  type Category = String
  type Detail = String
  type Certification = String

  case class Item(id: Int, catalogId: Int, brandId: Int)
}

sealed trait AsyncApiComponent[AsyncIO[+_]] {

  import model._

  def itemRepository: ItemRepository

  def catalogRepository: CatalogRepository

  def brandRepository: BrandRepository

  def itemWishCountRepository: ItemWishCountRepository

  def categoryRepository: CategoryRepository

  def itemDetailRepository: ItemDetailRepository

  def itemCertificationRepository: ItemCertificationRepository


  trait ItemRepository {
    def findById(id: Int): AsyncIO[Item]
  }

  trait CatalogRepository {
    def findById(id: Int): AsyncIO[Catalog]
  }

  trait BrandRepository {
    def findById(id: Int): AsyncIO[Brand]
  }

  trait ItemWishCountRepository {
    def findByItemId(id: Int): AsyncIO[Wish]
  }

  trait CategoryRepository {
    def findOneByBrandId(id: Int): AsyncIO[Category]
  }

  trait ItemDetailRepository {
    def findByItemId(id: Int): AsyncIO[Detail]
  }

  trait ItemCertificationRepository {
    def findByItemId(id: Int): AsyncIO[Certification]
  }
}

object ScalaFutureApiComponent extends AsyncApiComponent[Future] {

  import scala.concurrent.ExecutionContext.Implicits.global
  import model._

  def itemRepository: ItemRepository = itemRepositoryScalaFuture

  def catalogRepository: CatalogRepository = catalogRepositoryScalaFuture

  def brandRepository: BrandRepository = brandRepositoryScalaFuture

  def itemWishCountRepository: ItemWishCountRepository = itemWishCountRepositoryScalaFuture

  def categoryRepository: CategoryRepository = categoryRepositoryScalaFuture

  def itemDetailRepository: ItemDetailRepository = itemDetailRepositoryScalaFuture

  def itemCertificationRepository: ItemCertificationRepository = itemCertificationRepositoryScalaFuture

  object itemRepositoryScalaFuture extends ItemRepository {
    def findById(id: Int): Future[Item] = Future {
      Thread.sleep(1000)
      log(s"item-$id")
      Item(id, 1000, 100000)
    }
  }

  object catalogRepositoryScalaFuture extends CatalogRepository {
    def findById(id: Int): Future[Catalog] = Future {
      Thread.sleep(1000)
      log(s"catalog-$id")
      s"catalog-$id"
    }
  }

  object brandRepositoryScalaFuture extends BrandRepository {
    def findById(id: Int): Future[Brand] = Future {
      Thread.sleep(1000)
      log(s"brand-$id")
      s"brand-$id"
    }
  }

  object itemWishCountRepositoryScalaFuture extends ItemWishCountRepository {
    def findByItemId(id: Int): Future[Wish] = Future {
      Thread.sleep(1000)
      log(s"wish-$id")
      s"wish-$id"
    }
  }

  object categoryRepositoryScalaFuture extends CategoryRepository {
    def findOneByBrandId(id: Int): Future[Category] = Future {
      Thread.sleep(1000)
      log(s"category-$id")
      s"category-$id"
    }
  }

  object itemDetailRepositoryScalaFuture extends ItemDetailRepository {
    def findByItemId(id: Int): Future[Detail] = Future {
      Thread.sleep(1000)
      log(s"detail-$id")
      s"detail-$id"
    }
  }

  object itemCertificationRepositoryScalaFuture extends ItemCertificationRepository {
    def findByItemId(id: Int): Future[Certification] = Future {
      Thread.sleep(1000)
      log(s"certification-$id")
      s"certification-$id"
    }
  }


}


object MonixTaskApiComponent extends AsyncApiComponent[Task] {

  def itemRepository: ItemRepository = itemRepositoryMonixTask

  def catalogRepository: CatalogRepository = catalogRepositoryMonixTask

  def brandRepository: BrandRepository = brandRepositoryMonixTask

  def itemWishCountRepository: ItemWishCountRepository = itemWishCountRepositoryMonixTask

  def categoryRepository: CategoryRepository = categoryRepositoryMonixTask

  def itemDetailRepository: ItemDetailRepository = itemDetailRepositoryMonixTask

  def itemCertificationRepository: ItemCertificationRepository = itemCertificationRepositoryMonixTask

  object itemRepositoryMonixTask extends ItemRepository {
    def findById(id: Int): Task[Item] = Task {
      Thread.sleep(1000)
      log(s"item-$id")
      Item(id, 1000, 100000)
    }
  }

  object catalogRepositoryMonixTask extends CatalogRepository {
    def findById(id: Int): Task[Catalog] = Task {
      Thread.sleep(1000)
      log(s"catalog-$id")
      s"catalog-$id"
    }
  }

  object brandRepositoryMonixTask extends BrandRepository {
    def findById(id: Int): Task[Brand] = Task {
      Thread.sleep(1000)
      log(s"brand-$id")
      s"brand-$id"
    }
  }

  object itemWishCountRepositoryMonixTask extends ItemWishCountRepository {
    def findByItemId(id: Int): Task[Wish] = Task {
      Thread.sleep(1000)
      log(s"wish-$id")
      s"wish-$id"
    }
  }

  object categoryRepositoryMonixTask extends CategoryRepository {
    def findOneByBrandId(id: Int): Task[Category] = Task {
      Thread.sleep(1000)
      log(s"category-$id")
      s"category-$id"
    }
  }

  object itemDetailRepositoryMonixTask extends ItemDetailRepository {
    def findByItemId(id: Int): Task[Detail] = Task {
      Thread.sleep(1000)
      log(s"detail-$id")
      s"detail-$id"
    }
  }

  object itemCertificationRepositoryMonixTask extends ItemCertificationRepository {
    def findByItemId(id: Int): Task[Certification] = Task {
      Thread.sleep(1000)
      log(s"certificatTaskn-$id")
      s"certificatTaskn-$id"
    }
  }

}


// 모두 sync 방식으로 동작하고 있음
object experiment {

  import monix.execution.Scheduler.Implicits.global

  def main(args: Array[String]): Unit = {
    println("scala future monad with eager evaluation")
    awaitTime {
      getProduct(ScalaFutureApiComponent, 10)
    }

    println
    println("monix task monad with lazy evaluation")
    awaitTime {
      getProduct(MonixTaskApiComponent, 10).runToFuture
    }

    println
    println("scala future applicative with eager evaluation")
    awaitTime {
      getProductAp(ScalaFutureApiComponent, 10)
    }

    println
    println("monix task applicative with lazy evaluation 1")
    awaitTime {
      getProductAp(MonixTaskApiComponent, 10).runToFuture
    }

    println
    println("monix task applicative with lazy evaluation 2")
    awaitTime {
      import MonixTaskApiComponent._

      val itemId = 10
      val task: Task[List[Category]] = itemRepository.findById(itemId).flatMap { item =>
        import monix.eval.Task
        Applicative[Task].map6(
          catalogRepository.findById(item.catalogId),
          brandRepository.findById(item.brandId),
          itemWishCountRepository.findByItemId(item.id),
          categoryRepository.findOneByBrandId(item.brandId),
          itemDetailRepository.findByItemId(item.id),
          itemCertificationRepository.findByItemId(item.id)
        ) {
          case (catalog, brand, wish, category, detail, cert) =>
            List(brand, catalog, wish, category, detail, cert)
        }
      }
      task.runToFuture
    }

    println
    println("monix task applicative with lazy evaluation 3")
    awaitTime {
      import MonixTaskApiComponent._

      val itemId = 10
      val task: Task[List[Category]] = itemRepository.findById(itemId).flatMap { item =>
        Task.parMap6(
          catalogRepository.findById(item.catalogId),
          brandRepository.findById(item.brandId),
          itemWishCountRepository.findByItemId(item.id),
          categoryRepository.findOneByBrandId(item.brandId),
          itemDetailRepository.findByItemId(item.id),
          itemCertificationRepository.findByItemId(item.id)
        ) {
          case (catalog, brand, wish, category, detail, cert) =>
            List(brand, catalog, wish, category, detail, cert)
        }
      }
      task.runToFuture
    }
  }

  def awaitTime[R](block: => Awaitable[R]): R = {
    val start = System.nanoTime()
    val result = Await.result(block, 1 minutes)
    val elapsed = System.nanoTime() - start
    println("elapsed : " + elapsed / (1000 * 1000 * 1000.0) + " sec")
    result
  }

  def getProduct[F[+_] : Monad](api: AsyncApiComponent[F], itemId: Int): F[List[String]] = {
    import api._
    val itemFuture = itemRepository.findById(itemId)
    itemFuture.flatMap { item =>
      val catalogFuture = catalogRepository.findById(item.catalogId)
      val brandFuture = brandRepository.findById(item.brandId)
      val wishFuture = itemWishCountRepository.findByItemId(item.id)
      val categoryFuture = categoryRepository.findOneByBrandId(item.brandId)
      val itemDetailFuture = itemDetailRepository.findByItemId(item.id)
      val itemCertificationFuture = itemCertificationRepository.findByItemId(item.id)
      for {
        catalog <- catalogFuture
        brand <- brandFuture
        wishCount <- wishFuture
        category <- categoryFuture
        detail <- itemDetailFuture
        certifications <- itemCertificationFuture
      } yield
        List(brand, catalog, wishCount, category, detail, certifications)
    }
  }

  def getProductAp[F[+_] : Monad : Applicative](api: AsyncApiComponent[F], itemId: Int): F[List[String]] = {
    import api._

    itemRepository.findById(itemId).flatMap { item =>
      (
        catalogRepository.findById(item.catalogId),
        brandRepository.findById(item.brandId),
        itemWishCountRepository.findByItemId(item.id),
        categoryRepository.findOneByBrandId(item.brandId),
        itemDetailRepository.findByItemId(item.id),
        itemCertificationRepository.findByItemId(item.id)
        ).mapN { case (catalog, brand, wish, category, detail, cert) =>
        List(brand, catalog, wish, category, detail, cert)
      }
    }
  }
}
