package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import akka.util.Timeout
import dao.BirthdayCongratulationDao
import javax.inject.Inject
import play.api.Environment
import protocols.BirthdayCongratulationProtocol.{AddBirthdayCongratulation, BirthdayCongratulation}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class BirthdayCongratulationManager @Inject()(val environment: Environment,
                                              birthdayCongratulationDao: BirthdayCongratulationDao
                                            )(implicit val ec: ExecutionContext)

  extends Actor with ActorLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {
    case AddBirthdayCongratulation(birthdayCongratulation) =>
      addBirthdayCongratulation(birthdayCongratulation).pipeTo(sender())

    case _ => log.info(s"received unknown message")

  }

  private def addBirthdayCongratulation(birthdayCongratulation: BirthdayCongratulation) = {
    birthdayCongratulationDao.addBirthdayCongratulation(birthdayCongratulation)
  }

}