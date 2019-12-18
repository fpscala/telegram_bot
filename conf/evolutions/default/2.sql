# --- !Ups
CREATE TABLE "Student" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "name" VARCHAR NOT NULL,
  "holidayDate" DATE NOT NULL,
);

# --- !Downs
DROP TABLE "Holiday";