package controllers

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.scalalogging.LazyLogging
import javax.inject._
import org.webjars.play.WebJarsUtil
import play.api.libs.json.Json
import play.api.mvc._
import protocols.HolidayCongratulationProtocol._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

@Singleton
class HolidayCongratulationController @Inject()(val controllerComponents: ControllerComponents,
                                                @Named("holidayCongratulation-manager") val holidayCongratulationManager: ActorRef,
                                                implicit val webJarsUtil: WebJarsUtil
                                 )
                                               (implicit val ec: ExecutionContext)
  extends BaseController with LazyLogging {

  implicit val defaultTimeout: Timeout = Timeout(60.seconds)

  def index: Action[AnyContent] = Action {
    holidayCongratulationPost
    Ok(views.html.holidayCongratulation.holidayCongratulation(""))
  }

  def holidayCongratulationPost = {
    logger.warn(s"Keldi.........")
    (holidayCongratulationManager ? AddHolidayCongratulation(HolidayCongratulation(None, "Yangi yilingiz bilan!", 123546))).mapTo[Int].map { pr =>
      Ok(Json.toJson(s"ajji: $pr"))
    }
  }

}
