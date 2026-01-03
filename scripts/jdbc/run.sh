#!/bin/bash
set -e

BASE_DIR=$(cd "$(dirname "$0")" && pwd)

cd "$BASE_DIR"

ROOT_DIR=$(cd "$BASE_DIR/../.." && pwd)
ENV_FILE="$ROOT_DIR/.env"

set -a
source "$ENV_FILE"
set +a

echo "DB_USERNAME=$DB_USERNAME"
echo "DB_PASSWORD=$DB_PASSWORD"

LIB_JAR="$BASE_DIR/lib/mysql-connector-j-8.4.0.jar"

ls -l "$LIB_JAR"

echo "Compile ReservationLoader"
javac -cp "$LIB_JAR" ReservationLoader.java

echo "Run ReservationLoader"
java -cp ".:$LIB_JAR" ReservationLoader
