# --- !Ups
CREATE TABLE "HolidaysCongratulations" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "wishes" VARCHAR NOT NULL,
  "holidayId" INTEGER NOT NULL
);

# --- !Downs
DROP TABLE "HolidaysCongratulations";