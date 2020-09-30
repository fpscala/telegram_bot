# --- !Ups
CREATE TABLE "Student" (
  "id" SERIAL NOT NULL PRIMARY KEY,
  "first_name" VARCHAR NOT NULL,
  "last_name" VARCHAR NOT NULL,
  "birthday" DATE NOT NULL,
  "telegram_id" INTEGER NOT NULL
);

# --- !Downs
DROP TABLE "Student";
ALTER TABLE "Student" RENAME "firstName" TO "first_name";
ALTER TABLE "Student" RENAME "lastName" TO "last_name";
ALTER TABLE "Student" RENAME "telegramId" TO "telegram_id";