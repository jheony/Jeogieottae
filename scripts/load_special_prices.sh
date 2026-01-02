#!/bin/bash
set -e
source .env

until docker exec mysql8.4 \
  mysql --local-infile=1 \
        --default-character-set=utf8mb4 \
        -u root -p${DB_PASSWORD} \
        -e "SELECT 1" >/dev/null 2>&1
do
  sleep 2
done

docker exec -i mysql8.4 \
  mysql --local-infile=1 \
        --default-character-set=utf8mb4 \
        -u root -p${DB_PASSWORD} jeogieottae <<EOF
LOAD DATA INFILE '/var/lib/mysql-files/special_prices/special_prices_100.csv'
INTO TABLE special_prices
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(name, discount, due_timestamp);
EOF
