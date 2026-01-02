source .env

until docker exec mysql8.4 \
  mysql --local-infile=1 -u root -p${DB_PASSWORD} -e "SELECT 1" >/dev/null 2>&1
do
  sleep 2
done

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

