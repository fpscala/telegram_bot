# --- !Ups
CREATE TABLE "Student" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "firstName" VARCHAR NOT NULL,
  "lastName" VARCHAR NOT NULL,
  "birthday" DATE NOT NULL,
  "telegramId" INTEGER NOT NULL
);

# --- !Downs
DROP TABLE "Student";