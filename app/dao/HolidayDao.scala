package dao

import java.util.Date

import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.HolidayProtocol.Holiday
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.Future

trait HolidayComponent { self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class HolidayTable(tag: Tag) extends Table[Holiday](tag, "Holiday") with Date2SqlDate  {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("holidayName")
    def date = column[Date]("holidayDate")

    def * = (id.?, name, date) <> (Holiday.tupled, Holiday.unapply _)
  }
}

@ImplementedBy(classOf[HolidayDaoImpl])
trait HolidayDao {
  def addHoliday(holidayData: Holiday): Future[Int]
}

@Singleton
class HolidayDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HolidayDao
    with HolidayComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with LazyLogging
    with Date2SqlDate {

  import utils.PostgresDriver.api._

  val holiday = TableQuery[HolidayTable]

  override def addHoliday(holidayData: Holiday): Future[Int] = {
    db.run {
      (holiday returning holiday.map(_.id)) += holidayData
    }
  }
}

