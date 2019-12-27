package protocols

import java.util.Date

import play.api.libs.json.{Json, OFormat}

object StudentProtocol {

  case class AddStudent(student: Student)

  case class Student(id: Option[Int] = None,
                     first_name: String,
                     last_name: String,
                     birthDay: Date,
                     telegram_id: Long
                    )

  implicit val studentFormat: OFormat[Student] = Json.format[Student]
}

