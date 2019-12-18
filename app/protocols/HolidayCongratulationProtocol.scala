package protocols

import play.api.libs.json.{Json, OFormat}

object HolidayCongratulationProtocol {

  case class AddHolidayCongratulation(student: HolidayCongratulation)

  case class HolidayCongratulation(id: Option[Int] = None,
                                   wishes: String,
                                   holiday_id: Int
                    )

  implicit val holidayCongratulationFormat: OFormat[HolidayCongratulation] = Json.format[HolidayCongratulation]
}

