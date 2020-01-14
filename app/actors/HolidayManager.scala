package actors

import akka.actor._
import akka.pattern.pipe
import akka.util.Timeout
import dao.HolidayDao
import javax.inject.Inject
import play.api.{Configuration, Environment}
import protocols.HolidayProtocol.{AddHoliday, Holiday}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class HolidayManager @Inject()(val environment: Environment,
                               val configuration: Configuration,
                               holidayDao: HolidayDao,
                               implicit val actorSystem: ActorSystem
                              )
                              (implicit val ec: ExecutionContext)
  extends Actor with ActorLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {
    case AddHoliday(holiday) =>
      addHoliday(holiday).pipeTo(sender())

    case _ => log.info(s"received unknown message")

  }
  private def addHoliday(data: Holiday): Future[Int] = {
    holidayDao.addHoliday(data)
  }
}