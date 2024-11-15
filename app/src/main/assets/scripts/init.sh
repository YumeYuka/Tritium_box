#!/system/bin/sh
BASE_DIR=$(dirname "$0")

PID_FILE="${BASE_DIR}/../binaries/log.txt"

if [[ -f $PID_FILE ]]; then
    PID=$(grep -oP '^pid=\K\d+' "$PID_FILE")
else
    echo "PID文件不存在: $PID_FILE"
    chmod 777 "${BASE_DIR}/run_scheduler.sh"
    "${BASE_DIR}/run_scheduler.sh"
fi

if [[ -z $PID ]]; then
    echo "PID文件中未找到有效的PID"
    chmod 777 "${BASE_DIR}/run_scheduler.sh"
     "${BASE_DIR}/run_scheduler.sh"
fi

if ps -p "$PID" > /dev/null 2>&1; then
    echo "PID $PID 对应的进程正在运行"
    exit 0
else
    echo "PID $PID 对应的进程未运行"
    chmod 777 "${BASE_DIR}/run_scheduler.sh"
    "${BASE_DIR}/run_scheduler.sh"
fi