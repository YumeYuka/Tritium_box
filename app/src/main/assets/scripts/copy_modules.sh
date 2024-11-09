#!/system/bin/sh

# 定义源目录和目标目录
SOURCE_DIR="/data/data/cn.nightrainmilkyway.tritium/files/modules/"
TARGET_DIR="/storage/emulated/0/Download/"

# 确保目标目录存在，如果不存在则创建
mkdir -p "$TARGET_DIR"

# 循环遍历源目录中的所有文件
for file in "$SOURCE_DIR"*; do
    # 检查是否是文件并且文件存在
    if [ -f "$file" ]; then
        # 复制文件到目标目录
        cp "$file" "$TARGET_DIR"
    fi
done

# 脚本结束
exit 0

