CREATE TABLE "categories" (
  "id" uuid PRIMARY KEY,
  "category" varchar UNIQUE NOT NULL
);

CREATE TABLE "tallies" (
  "name" varchar PRIMARY KEY,
  "category" uuid NOT NULL,
  "tally" integer NOT NULL
);

ALTER TABLE "tallies" ADD FOREIGN KEY ("category") REFERENCES "categories" ("id");
