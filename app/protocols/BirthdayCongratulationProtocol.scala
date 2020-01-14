package protocols

import play.api.libs.json.{Json, OFormat}

object BirthdayCongratulationProtocol {

  case class AddBirthdayCongratulation(student: BirthdayCongratulation)

  case class BirthdayCongratulation(id: Option[Int] = None,
                                   wishes: String,
                                   )

  implicit val birthdayCongratulationFormat: OFormat[BirthdayCongratulation] = Json.format[BirthdayCongratulation]
}

