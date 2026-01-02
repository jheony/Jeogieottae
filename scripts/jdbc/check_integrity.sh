#!/bin/bash
set -e

BASE_DIR=$(cd "$(dirname "$0")" && pwd)
source "$BASE_DIR/.env"

docker exec -i mysql8.4 mysql \
  -u"$DB_USERNAME" -p"$DB_PASSWORD" "$DB_NAME" <<EOF

SELECT COUNT(*) AS orphan_room
FROM reservations r
LEFT JOIN rooms ro ON r.room_id = ro.id
WHERE ro.id IS NULL;

SELECT COUNT(*) AS orphan_user
FROM reservations r
LEFT JOIN users u ON r.user_id = u.id
WHERE u.id IS NULL;

EOF
