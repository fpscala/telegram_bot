package dao

import java.util.Date

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import com.typesafe.scalalogging.LazyLogging
import javax.inject.{Inject, Singleton}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import protocols.StudentProtocol._
import slick.jdbc.JdbcProfile
import utils.Date2SqlDate

import scala.concurrent.{ExecutionContext, Future}


trait StudentComponent {
  self: HasDatabaseConfigProvider[JdbcProfile] =>

  import utils.PostgresDriver.api._

  class StudentTable(tag: Tag) extends Table[Student](tag, "Student") with Date2SqlDate {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def firstName = column[String]("firstName")

    def lastName = column[String]("lastName")

    def birthday = column[Date]("birthday")

    def telegramId = column[Int]("telegramId")

    def * = (id.?, firstName, lastName, birthday, telegramId) <> (Student.tupled, Student.unapply _)
  }

}

@ImplementedBy(classOf[StudentDaoImpl])
trait StudentDao {
  def addStudent(studentData: Student): Future[Int]

  def getAllStudentBirthDay: Future[Seq[Student]]

  def getStudentsList: Future[Seq[Student]]

  def updateStudents(data: Student): Future[Int]

  def deleteStudents(id: Int): Future[Int]
}

@Singleton
class StudentDaoImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider,
                               val actorSystem: ActorSystem)
                              (implicit val ec: ExecutionContext)
  extends StudentDao
    with StudentComponent
    with HasDatabaseConfigProvider[JdbcProfile]
    with Date2SqlDate
    with LazyLogging {

  import utils.PostgresDriver.api._

  val students = TableQuery[StudentTable]

  override def addStudent(studentData: Student): Future[Int] = {
    db.run {
      (students returning students.map(_.id)) += studentData
    }
  }

  override def getAllStudentBirthDay: Future[Seq[Student]] = {
    db.run {
      students.result
    }
  }

  override def getStudentsList: Future[Seq[Student]] = {
    db.run {
      students.result
    }
  }

  override def updateStudents(data: Student): Future[Int] = {
    db.run {
      students.filter(_.id === data.id).update(data)
    }
  }

  override def deleteStudents(id: Int): Future[Int] = {
    db.run{
      students.filter(_.id === id).delete
    }
  }
}

