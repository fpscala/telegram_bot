package actors

import java.text.SimpleDateFormat
import java.util.Date

import akka.actor._
import akka.pattern.pipe
import akka.util.Timeout
import dao.StudentDao
import javax.inject.Inject
import play.api.{Configuration, Environment}
import protocols.StudentProtocol._
import telegrambot.BotInitializer

import scala.concurrent.{ExecutionContext, Future}
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
    actorSystem.scheduler.schedule(initialDelay = 5.seconds, interval = 1.hour) {
      self ! FindBirthday
    }
  }

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def receive = {
    case AddStudent(student) =>
      addStudent(student).pipeTo(sender())

    case FindBirthday =>
      findBirthday()

    case GetStudents =>
      getStudentsList.pipeTo(sender())

    case DeleteStudents(id) =>
      deleteStudentList(id).pipeTo(sender())

    case UpdateStudents(data) =>
      updateStudentsList(data).pipeTo(sender())

    case _ => log.info(s"received unknown message")

  }

  private def addStudent(studentData: Student): Future[Int] = {
    studentDao.addStudent(studentData)
  }

  private def getStudentsList: Future[Seq[Student]] = {
    studentDao.getStudentsList
  }

  private def deleteStudentList(id: Int): Future[Int] = {
    studentDao.deleteStudents(id)
  }

  private def findBirthday(date: Date = new Date()) = {
    studentDao.getAllStudentBirthDay.mapTo[Seq[Student]].foreach { studentList =>
      studentList.foreach { studentData =>
        if (convertToStrDate(studentData.birthDay) == convertToStrDate(date)) {
          new BotInitializer(botUsername, botToken, httpLink).message(studentData)
        }
      }

    }
  }

  private def convertToStrDate(date: Date)
  = {
    new SimpleDateFormat("MM-dd").format(date)
  }

  private def updateStudentsList(data: Student): Future[Int] = {
    studentDao.updateStudents(data)
  }
}