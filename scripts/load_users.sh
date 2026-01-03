#!/usr/bin/env bash
set -e

BASE_DIR=$(cd "$(dirname "$0")" && pwd)
ROOT_DIR=$(cd "$BASE_DIR/.." && pwd)

source "$ROOT_DIR/.env"

cd csv/generator
javac UserCsvGenerator.java
java UserCsvGenerator
cd ../../

until docker exec mysql8.4 \
  mysql --local-infile=1 -u root -p${DB_PASSWORD} -e "SELECT 1" >/dev/null 2>&1
do
  sleep 2
done

docker exec mysql8.4 mkdir -p /var/lib/mysql-files/users

docker cp csv/users/users_100.csv \
  mysql8.4:/var/lib/mysql-files/users/users_100.csv

docker exec -i mysql8.4 \
  mysql --local-infile=1 \
        --default-character-set=utf8mb4 \
        -u root -p${DB_PASSWORD} jeogieottae <<EOF
LOAD DATA LOCAL INFILE '/var/lib/mysql-files/users/users_100.csv'
INTO TABLE users
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(email, username, password, is_deleted);
EOF
