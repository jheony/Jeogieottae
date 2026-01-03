.PHONY: reset data reservation chmod fk

chmod:
	@chmod +x scripts/*.sh scripts/jdbc/run.sh scripts/jdbc/check_integrity.sh scripts/add_fks.sh

data:
	./scripts/load_users.sh
	./scripts/load_accommodations.sh
	./scripts/load_special_prices.sh
	./scripts/load_rooms.sh
	./scripts/load_coupons.sh

reservation:
	./scripts/jdbc/run.sh

fk:
	./scripts/jdbc/check_integrity.sh
	./scripts/add_fks.sh

reset: chmod
	docker compose down -v
	docker compose up -d
	@echo "기다려주세요 (MySQL 초기화 중)..."
	sleep 10
	make data
	make reservation
