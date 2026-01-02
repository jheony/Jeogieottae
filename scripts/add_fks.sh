#!/bin/bash
set -e

BASE_DIR=$(cd "$(dirname "$0")" && pwd)
source "$BASE_DIR/jdbc/.env"

docker exec -i mysql8.4 mysql \
  -u"$DB_USERNAME" -p"$DB_PASSWORD" "$DB_NAME" <<EOF

ALTER TABLE reservations
ADD CONSTRAINT fk_reservations_room
FOREIGN KEY (room_id) REFERENCES rooms(id);

ALTER TABLE reservations
ADD CONSTRAINT fk_reservations_user
FOREIGN KEY (user_id) REFERENCES users(id);

EOF
