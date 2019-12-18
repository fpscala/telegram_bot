package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import akka.util.Timeout
import dao.HolidayCongratulationDao
import javax.inject.Inject
import play.api.Environment
import protocols.HolidayCongratulationProtocol.{AddHolidayCongratulation, HolidayCongratulation}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class HolidayCongratulationManager @Inject()(val environment: Environment,
                                             holidayCongratulationDao: HolidayCongratulationDao
                              ) (implicit val ec: ExecutionContext)

  extends Actor with ActorLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {
    case AddHolidayCongratulation(holidayCongratulation) =>
      addHolidayCongratulation(holidayCongratulation).pipeTo(sender())

    case _ => log.info(s"received unknown message")

  }

  private def addHolidayCongratulation(holidayCongratulation: HolidayCongratulation) = {
    holidayCongratulationDao.addHolidayCongratulation(holidayCongratulation)
  }

}