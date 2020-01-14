package dao

import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.BirthdayCongratulationProtocol.BirthdayCongratulation
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.Future

trait BirthdayCongratulationComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class BirthdayCongratulationTable(tag: Tag) extends Table[BirthdayCongratulation](tag, "BirthdayCongratulations") with Date2SqlDate  {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def wishes = column[String]("wishes")

    def * = (id.?, wishes) <> (BirthdayCongratulation.tupled, BirthdayCongratulation.unapply _)
  }
}

@ImplementedBy(classOf[HolidayCongratulationDaoImpl])
trait BirthdayCongratulationDao {
  def addBirthdayCongratulation(birthdayCongratulationData: BirthdayCongratulation): Future[Int]
}

@Singleton
class BirthdayCongratulationDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends BirthdayCongratulationDao
    with BirthdayCongratulationComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with LazyLogging {

  import utils.PostgresDriver.api._

  val birthdayCongratulation = TableQuery[BirthdayCongratulationTable]

  override def addBirthdayCongratulation(birthdayCongratulationData: BirthdayCongratulation): Future[Int] = {
    db.run {
//      logger.warn(s"Daoga keldi.....................")
      (birthdayCongratulation returning birthdayCongratulation.map(_.id)) += birthdayCongratulationData
    }
  }
}

