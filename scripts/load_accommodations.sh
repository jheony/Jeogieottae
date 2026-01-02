source .env


until docker exec mysql8.4 \
  mysql --local-infile=1 \
        --default-character-set=utf8mb4 \
        -u root -p${DB_PASSWORD} \
        -e "SELECT 1" >/dev/null 2>&1
do
  sleep 2
done


for file in csv/accommodations/accommodations_*.csv
do
  FILE_NAME=$(basename "$file")
  echo "loading ${FILE_NAME}"

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
