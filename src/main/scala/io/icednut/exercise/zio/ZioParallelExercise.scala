package io.icednut.exercise.zio

import io.icednut.exercise.common._
import zio._
import zio.clock._
import zio.duration._
import zio.logging.slf4j.Slf4jLogger
import zio.logging.{Logging, log}

object ZioFiberApiComponent {
  private val env: ULayer[Logging] = Slf4jLogger.make { (_, message) => message }

  def itemRepository: ItemRepositoryZioFiber.type = ItemRepositoryZioFiber

  def catalogRepository: CatalogRepositoryZioFiber.type = CatalogRepositoryZioFiber

  def brandRepository: BrandRepositoryZioFiber.type = BrandRepositoryZioFiber

  def itemWishCountRepository: ItemWishCountRepositoryZioFiber.type = ItemWishCountRepositoryZioFiber

  def categoryRepository: CategoryRepositoryZioFiber.type = CategoryRepositoryZioFiber

  def itemDetailRepository: ItemDetailRepositoryZioFiber.type = ItemDetailRepositoryZioFiber

  def itemCertificationRepository: ItemCertificationRepositoryZioFiber.type = ItemCertificationRepositoryZioFiber

  object ItemRepositoryZioFiber {
    def findById(id: Int): URIO[zio.ZEnv, Fiber.Runtime[Nothing, Item]] =
      (
        sleep(1 seconds) *>
          log.info(s"item-$id").provideCustomLayer(env) *>
          ZIO.effectTotal(Item(id, 1000, 100000))
        ).fork
  }

  object CatalogRepositoryZioFiber {
    def findById(id: Int): URIO[zio.ZEnv, Fiber.Runtime[Nothing, Catalog]] =
      (
        sleep(1 seconds) *>
          log.info(s"catalog-$id").provideCustomLayer(env) *>
          ZIO.effectTotal(s"catalog-$id")
        ).fork
  }

  object BrandRepositoryZioFiber {
    def findById(id: Int): URIO[zio.ZEnv, Fiber.Runtime[Nothing, Brand]] =
      (
        sleep(1 seconds) *>
          log.info(s"brand-$id").provideCustomLayer(env) *>
          ZIO.effectTotal(s"brand-$id")
        ).fork
  }

  object ItemWishCountRepositoryZioFiber {
    def findByItemId(itemId: Int): URIO[zio.ZEnv, Fiber.Runtime[Nothing, Wish]] =
      (
        sleep(1 seconds) *>
          log.info(s"wish-$itemId").provideCustomLayer(env) *>
          ZIO.effectTotal(s"wish-$itemId")
        ).fork
  }

  object CategoryRepositoryZioFiber {
    def findOneByBrandId(brandId: Int): URIO[zio.ZEnv, Fiber.Runtime[Nothing, Category]] =
      (
        sleep(1 seconds) *>
          log.info(s"category-$brandId").provideCustomLayer(env) *>
          ZIO.effectTotal(s"category-$brandId")
        ).fork
  }

  object ItemDetailRepositoryZioFiber {
    def findByItemId(itemId: Int): URIO[zio.ZEnv, Fiber.Runtime[Nothing, Detail]] =
      (
        sleep(1 seconds) *>
          log.info(s"detail-$itemId").provideCustomLayer(env) *>
          ZIO.effectTotal(s"detail-$itemId")
        ).fork
  }

  object ItemCertificationRepositoryZioFiber {
    def findByItemId(itemId: Int) =
      (
        sleep(1 seconds) *>
          log.info(s"certificatTaskn-$itemId").provideCustomLayer(env) *>
          ZIO.effectTotal(s"certificatTaskn-$itemId")
        ).fork
  }
}

object ZioParallelExercise extends zio.App {

  import ZioFiberApiComponent._

  val itemId = 10
  val app = for {
    itemFiber <- itemRepository.findById(itemId)
    item <- itemFiber.join
    catalogFiber <- catalogRepository.findById(item.catalogId)
    brandFiber <- brandRepository.findById(item.brandId)
    wishFiber <- itemWishCountRepository.findByItemId(item.id)
    categoryFiber <- categoryRepository.findOneByBrandId(item.brandId)
    itemDetailFiber <- itemDetailRepository.findByItemId(item.id)
    itemCertificaitonFiber <- itemCertificationRepository.findByItemId(item.id)
    catalog <- catalogFiber.join
    brand <- brandFiber.join
    wish <- wishFiber.join
    category <- categoryFiber.join
    itemDetail <- itemDetailFiber.join
    itemCertificaiton <- itemCertificaitonFiber.join
  } yield List(brand, catalog, wish, category, itemDetail, itemCertificaiton)

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = app.exitCode
}
