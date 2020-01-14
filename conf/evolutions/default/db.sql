# --- !Ups
CREATE TABLE "Student" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "firstName" VARCHAR NOT NULL,
  "lastName" VARCHAR NOT NULL,
  "birthday" DATE NOT NULL,
  "telegramId" INTEGER NOT NULL
);

CREATE TABLE "Holiday" (
                           "id" SERIAL NOT NULL PRIMARY KEY,
                           "holidayName" VARCHAR NOT NULL,
                           "holidayDate" DATE NOT NULL
);
CREATE TABLE "HolidaysCongratulations" (
                                           "id" SERIAL NOT NULL PRIMARY KEY,
                                           "wishes" VARCHAR NOT NULL,
                                           "holidayId" INTEGER NOT NULL
);

CREATE TABLE "BirthdayCongratulations" (
                                           "id" SERIAL NOT NULL PRIMARY KEY,
                                           "wishes" VARCHAR NOT NULL
);

# --- !Downs
DROP TABLE "Student";
DROP TABLE "Holiday";
DROP TABLE "HolidaysCongratulations";
DROP TABLE "BirthdayCongratulations";