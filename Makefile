SHELL := /usr/bin/env bash

.PHONY: reset data reservation chmod fk

BASE_DIR := $(dir $(realpath $(firstword $(MAKEFILE_LIST))))
SCRIPTS_DIR := $(BASE_DIR)/scripts
JDBC_DIR := $(SCRIPTS_DIR)/jdbc

chmod:
	@chmod +x $(SCRIPTS_DIR)/*.sh
	@chmod +x $(JDBC_DIR)/run.sh
	@chmod +x $(JDBC_DIR)/check_integrity.sh
	@chmod +x $(SCRIPTS_DIR)/add_fks.sh

data:
	$(SCRIPTS_DIR)/load_users.sh
	$(SCRIPTS_DIR)/load_accommodations.sh
	$(SCRIPTS_DIR)/load_special_prices.sh
	$(SCRIPTS_DIR)/load_rooms.sh
	$(SCRIPTS_DIR)/load_coupons.sh

reservation:
	$(JDBC_DIR)/run.sh

fk:
	$(JDBC_DIR)/check_integrity.sh
	$(SCRIPTS_DIR)/add_fks.sh

reset: chmod
	docker compose down -v
	docker compose up -d
	@echo "기다려주세요 (MySQL 초기화 중)..."
	$(MAKE) data
	$(MAKE) reservation
