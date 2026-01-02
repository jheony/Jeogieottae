#!/bin/bash
set -e

BASE_DIR=$(cd "$(dirname "$0")" && pwd)
cd "$BASE_DIR"

set -a
source .env
set +a

echo "DB_USERNAME=$DB_USERNAME"
echo "DB_PASSWORD=$DB_PASSWORD"

echo "Compile ReservationLoader"
javac -cp lib/mysql-connector-j-8.4.0.jar ReservationLoader.java

echo "Run ReservationLoader"
java -cp .:lib/mysql-connector-j-8.4.0.jar ReservationLoader
