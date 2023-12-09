#!/bin/bash
set -e

until mysqladmin -u root -p"${MYSQL_ROOT_PASSWORD}" -h 172.28.0.2 ping; do
  echo "# waiting for master - $(date)"
  sleep 3
done

# 유저 생성
mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -e "CREATE USER 'imhero'@'172.28.0.%' IDENTIFIED BY '${MYSQL_USER_PASSWORD}'"
mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -e "GRANT ALL PRIVILEGES ON *.* TO 'imhero'@'172.28.0.%' WITH GRANT OPTION"
mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -e "FLUSH PRIVILEGES"

# master log file 가져옴
master_log_file=$(mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -h 172.28.0.2 -e "SHOW MASTER STATUS\G" | grep mysql-bin)
re="[a-z]*-bin.[0-9]*"

if [[ $master_log_file =~ $re ]];then
  master_log_file=${BASH_REMATCH[0]}
fi

# 마스터 log position 가져옴
master_log_pos=$(mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -h 172.28.0.2 -e "SHOW MASTER STATUS\G" | grep Position)
re="[0-9]+"

if [[ $master_log_pos =~ $re ]];then
  master_log_pos=${BASH_REMATCH[0]}
fi

# 마스터 연결
sql="CHANGE MASTER TO MASTER_HOST='172.28.0.2', MASTER_USER='imhero', MASTER_PASSWORD='${MYSQL_USER_PASSWORD}', MASTER_LOG_FILE='${master_log_file}', MASTER_LOG_POS=${master_log_pos}, GET_MASTER_PUBLIC_KEY=1"
mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -e "${sql}"
mysql -u root -p"${MYSQL_ROOT_PASSWORD}" -e "START SLAVE"
