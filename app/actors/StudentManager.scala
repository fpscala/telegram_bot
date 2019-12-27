package actors

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import akka.actor._
import akka.pattern.pipe
import akka.util.Timeout
import dao.StudentDao
import javax.inject.Inject
import play.api.Environment
import protocols.StudentProtocol.{AddStudent, FindBirthday, Student}
import telegrambot.SendToServer

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class StudentManager @Inject()(val environment: Environment,
                               studentDao: StudentDao,
                               implicit val actorSystem: ActorSystem
                              )
                              (implicit val ec: ExecutionContext)
  extends Actor with ActorLogging {
  override def preStart(): Unit = {
    actorSystem.scheduler.schedule(initialDelay = 1.seconds, interval = 60.second) {
      self ! FindBirthday(new Date())
    }
  }

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {
    case AddStudent(student) =>
      addStudent(student).pipeTo(sender())

    case FindBirthday(date) =>
      findBirthday(date).pipeTo(sender())

    case _ => log.info(s"received unknown message")

  }

  private def addStudent(studentData: Student) = {
    studentDao.addStudent(studentData)
  }

  private def findBirthday(date: Date) = {
    studentDao.getAllStudentBirthDay.mapTo[Seq[Date]].foreach { dateList =>
      dateList.foreach{ dateSql =>
        if (convertToStrDate(dateSql) == convertToStrDate(date)) {
          studentDao.findBirthday(dateSql).mapTo[Seq[Student]].map{ list =>
            list.foreach{ student =>
              SendToServer.checkingResponse(student)
            }
          }
        }
      }
    }
  }

  private def convertToStrDate(date: Date)
  = {
    new SimpleDateFormat("MM-dd").format(date)
  }

}