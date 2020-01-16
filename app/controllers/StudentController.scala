package controllers


import java.util.Date

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import javax.inject._
import org.webjars.play.WebJarsUtil
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import protocols.StudentProtocol.{AddStudent, GetStudentsList, Student}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

@Singleton
class StudentController @Inject()(val controllerComponents: ControllerComponents,
                                  @Named("student-manager") val studentManager: ActorRef,
                                  implicit val webJarsUtil: WebJarsUtil
                                 )
                                 (implicit val ec: ExecutionContext)
  extends BaseController with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)
  val LoginSessionKey = "login.key"

  def index: Action[AnyContent] = Action {
    Ok(views.html.student_dashboard(""))
  }

  def students: Action[AnyContent] = Action {
    Ok(views.html.student_dashboard(""))
  }

  def getStudents: Action[AnyContent] = Action.async { implicit request =>
    (studentManager ? GetStudentsList).mapTo[Seq[Student]].map {
      students =>
          Ok(Json.toJson(students))
    }
  }

  def addStudent: Action[JsValue] = Action.async(parse.json){ implicit request => {
    val firstName = (request.body \ "firstName").as[String]
    val lastName = (request.body \ "lastName").as[String]
    val birthday = (request.body \ "birthday").as[Date]
    val telegramId = (request.body \ "telegramId").as[Int]
    (studentManager ? AddStudent(Student(None, firstName, lastName, birthday, telegramId))).mapTo[Int].map { id =>
      Ok(Json.toJson(id))
    }
  }
  }
//
//  def studentPost = {
//    logger.warn(s"Keldi.........")
//    (studentManager ? AddStudent(Student(None, "Maftunbek", "Raxmatov", new Date, 123546))).mapTo[Int].map { pr =>
//      Ok(Json.toJson(s"ajji: $pr"))
//    }
//  }
}
