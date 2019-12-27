package actors

import java.text.SimpleDateFormat
import java.util.Date

import akka.actor._
import akka.pattern.pipe
import akka.util.Timeout
import dao.StudentDao
import javax.inject.Inject
import play.api.{Configuration, Environment}
import protocols.StudentProtocol.{AddStudent, FindBirthday, Student}
import telegrambot.BotInitializer

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

class StudentManager @Inject()(val environment: Environment,
                               val configuration: Configuration,
                               studentDao: StudentDao,
                               implicit val actorSystem: ActorSystem
                              )
                              (implicit val ec: ExecutionContext)
  extends Actor with ActorLogging {

  val botUsername: String = configuration.get[String]("bot-username")
  val botToken: String = configuration.get[String]("bot-token")
  val httpLink: String = configuration.get[String]("http-link")

  override def preStart(): Unit = {
    actorSystem.scheduler.schedule(initialDelay = 1.seconds, interval = 60.second) {
      self ! FindBirthday
    }
  }

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {
    case AddStudent(student) =>
      addStudent(student).pipeTo(sender())

    case FindBirthday =>
      findBirthday()

    case _ => log.info(s"received unknown message")

  }

  private def addStudent(studentData: Student) = {
    studentDao.addStudent(studentData)
  }

  private def findBirthday(date: Date = new Date()) = {
    studentDao.getAllStudentBirthDay.mapTo[Seq[Date]].foreach { dateList =>
      dateList.foreach { dateSql =>
        if (convertToStrDate(dateSql) == convertToStrDate(date)) {
          studentDao.findBirthday(dateSql).mapTo[Seq[Student]].map { list =>
            list.foreach { student =>
              new BotInitializer(botUsername, botToken, httpLink).massege(student.first_name)
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