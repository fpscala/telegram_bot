package actors

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import akka.util.Timeout
import dao.StudentDao
import javax.inject.Inject
import play.api.Environment
import protocols.StudentProtocol.{AddStudent, Student}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.DurationInt

class StudentManager @Inject()(val environment: Environment,
                               studentDao: StudentDao
                              )
                              (implicit val ec: ExecutionContext)
  extends Actor with ActorLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {
    case AddStudent(student) =>
      addStudent(student).pipeTo(sender())

    case _ => log.info(s"received unknown message")

  }

  private def addStudent(studentData: Student): Future[Int] = {
    studentDao.addStudent(studentData)

  }

}