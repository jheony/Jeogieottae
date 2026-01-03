#!/bin/bash
set -e
source .env

cd csv/generator
javac RoomCsvSplitGenerator.java
java RoomCsvSplitGenerator
cd ../../

until docker exec mysql8.4 \
  mysql -u root -p${DB_PASSWORD} \
        -e "SELECT 1" >/dev/null 2>&1
do
  sleep 2
done

for file in csv/rooms/rooms_*.csv
do
  FILE_NAME=$(basename "$file")

mkdir -p csv/rooms
docker exec mysql8.4 mkdir -p /var/lib/mysql-files/rooms

  docker cp "$file" \
    mysql8.4:/var/lib/mysql-files/rooms/"${FILE_NAME}"

  docker exec -i mysql8.4 \
    mysql --local-infile=1 \
          --default-character-set=utf8mb4 \
          -u root -p${DB_PASSWORD} jeogieottae <<EOF
LOAD DATA INFILE '/var/lib/mysql-files/rooms/${FILE_NAME}'
INTO TABLE rooms
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(name, price, accommodation_id, @special_price_id)
SET special_price_id = NULLIF(@special_price_id, '');
EOF

done
