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
import protocols.StudentProtocol._
import views.html.index

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.DurationInt

@Singleton
class StudentController @Inject()(val controllerComponents: ControllerComponents,
                                  @Named("student-manager") val studentManager: ActorRef,
                                  implicit val webJarsUtil: WebJarsUtil,
                                  indexTemplate: index
                                 )
                                 (implicit val ec: ExecutionContext)
  extends BaseController with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def index: Action[AnyContent] = Action {
    Ok(indexTemplate(Some("")))
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

  def getStudents: Action[AnyContent] = Action.async {
    (studentManager ? GetStudents).mapTo[Seq[Student]].map { s =>
      Ok(Json.toJson(s.sortBy(_.id)))
    }.recover {
      case err =>
        logger.error(s"error: $err")
        BadRequest
    }
  }


//  def studentPost = {
//    logger.warn(s"Keldi.........")
//    (studentManager ? AddStudent(Student(None, "Maftunbek", "Raxmatov", new Date, 123546))).mapTo[Int].map { pr =>
//      Ok(Json.toJson(s"ajji: $pr"))
//    }
//  }
}
