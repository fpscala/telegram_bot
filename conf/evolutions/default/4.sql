# --- !Ups
CREATE TABLE "BirthdayCongratulations" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "wishes" VARCHAR NOT NULL,
  "studentId" INTEGER NOT NULL
);

# --- !Downs
DROP TABLE "BirthdayCongratulations";