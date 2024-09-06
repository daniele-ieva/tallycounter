CREATE TABLE "categories" (
  "category" varchar PRIMARY KEY
);

CREATE TABLE "tallies" (
  "name" varchar PRIMARY KEY,
  "category" varchar NOT NULL,
  "tally" integer NOT NULL
);

ALTER TABLE "tallies" ADD FOREIGN KEY ("category") REFERENCES "categories" ("category");
