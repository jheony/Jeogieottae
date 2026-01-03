#!/usr/bin/env bash
set -e

BASE_DIR=$(cd "$(dirname "$0")" && pwd)
ROOT_DIR=$(cd "$BASE_DIR/.." && pwd)

source "$ROOT_DIR/.env"

cd csv/generator
javac AccommodationCsvGenerator.java
java AccommodationCsvGenerator
cd ../../

until docker exec mysql8.4 \
  mysql --local-infile=1 \
        --default-character-set=utf8mb4 \
        -u root -p${DB_PASSWORD} \
        -e "SELECT 1" >/dev/null 2>&1
do
  sleep 2
done


docker exec mysql8.4 mkdir -p /var/lib/mysql-files/accommodations

for file in csv/accommodations/accommodations_*.csv
do
  FILE_NAME=$(basename "$file")

  docker cp "$file" \
    mysql8.4:/var/lib/mysql-files/accommodations/"${FILE_NAME}"

  docker exec -i mysql8.4 \
    mysql --local-infile=1 \
          --default-character-set=utf8mb4 \
          -u root -p${DB_PASSWORD} jeogieottae <<EOF
LOAD DATA LOCAL INFILE '/var/lib/mysql-files/accommodations/${FILE_NAME}'
INTO TABLE accommodations
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(name, type, location, rating, view_count);
EOF
done
