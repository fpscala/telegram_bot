package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import akka.util.Timeout
import dao.HolidayCongratulationDao
import javax.inject.Inject
import play.api.Environment
import protocols.HolidayCongratulationProtocol._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class HolidayCongratulationManager @Inject()(val environment: Environment,
                                             holidayCongratulationDao: HolidayCongratulationDao
                              ) (implicit val ec: ExecutionContext)

  extends Actor with ActorLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {
    case AddHolidayCongratulation(holidayCongratulation) =>
//      log.warning(s"Menejerga keldi.........")
      addHolidayCongratulation(holidayCongratulation).pipeTo(sender())

    case _ => log.info(s"received unknown message")

  }

  private def addHolidayCongratulation(holidayCongratulation: HolidayCongratulation) = {
//    log.warning(s"Menejerga keldi...............")
    holidayCongratulationDao.addHolidayCongratulation(holidayCongratulation)
  }

}