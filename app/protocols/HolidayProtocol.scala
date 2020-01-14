package protocols

import java.util.Date

import play.api.libs.json.{Json, OFormat}

object HolidayProtocol {

  case class AddHoliday(holiday: Holiday)

  case class Holiday(id: Option[Int] = None,
                     name: String,
                     date: Date
                    )

  implicit val holidayFormat: OFormat[Holiday] = Json.format[Holiday]
}

