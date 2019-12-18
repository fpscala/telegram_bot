package dao

import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.HolidayCongratulationProtocol.HolidayCongratulation
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate
import scala.concurrent.Future

trait HolidayCongratulationComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class HolidayCongratulationTable(tag: Tag) extends Table[HolidayCongratulation](tag, "HolidaysCongratulations") with Date2SqlDate  {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def wishes = column[String]("wishes")
    def holidayId = column[Int]("holidayId")

    def * = (id.?, wishes, holidayId) <> (HolidayCongratulation.tupled, HolidayCongratulation.unapply _)
  }
}

@ImplementedBy(classOf[HolidayCongratulationDaoImpl])
trait HolidayCongratulationDao {
  def addHolidayCongratulation(holidayCongratulationData: HolidayCongratulation): Future[Int]
}

@Singleton
class HolidayCongratulationDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HolidayCongratulationDao
    with HolidayCongratulationComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with LazyLogging {

  import utils.PostgresDriver.api._

  val holidayCongratulation = TableQuery[HolidayCongratulationTable]

  override def addHolidayCongratulation(holidayCongratulationData: HolidayCongratulation): Future[Int] = {
    db.run {
//      logger.warn(s"Daoga keldi.....................")
      (holidayCongratulation returning holidayCongratulation.map(_.id)) += holidayCongratulationData
    }
  }
}

