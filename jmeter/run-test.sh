#!/bin/bash -u

THIS_DIR=$(cd $(dirname $0); pwd)
PROJECT_DIR=$(cd $THIS_DIR/..; pwd)

# 環境設定ファイルの読み込み
ENV_FILE=$THIS_DIR/env.sh

if [ ! -e $ENV_FILE ]; then
    cat << EOS > $ENV_FILE
# アプリを動かしているEC2のホスト情報
export EC2_HOST=changeme

# RDSのエンドポイント
export RDS_ENDPOINT=changeme

# 実行するスレッド数のパターン
export THREAD_PATTERN="1 5 10 50 100"
# 各スレッド数での試行回数
export LOOP_COUNT=5
# JMeter実行時間（秒）
export TEST_DURATION=180
EOS

    echo $ENV_FILE が存在しなかったので生成しました。
    echo ファイルを開いて変数を設定してください。
    exit 1
fi

source $ENV_FILE

# 対象の決定 (old or new)
TARGET=$1

if [ "$TARGET" != "old" -a "$TARGET" != "new" ]; then
    echo 第一引数は old または new を指定してください TARGET=$TARGET
    exit 1
fi

# 関数定義
function initializeDatabase() {
    cd $PROJECT_DIR
    mvn generate-resources -Ddb.host=$RDS_ENDPOINT
}

function deployWar() {
    if [ "$TARGET" == "old" ]; then
        local WAR_FILE=example-web-old.war
    else
        local WAR_FILE=example-web-new.war
    fi

    ssh ec2-user@$EC2_HOST \
        "source ~/.bash_profile; rm -rf \$TOMCAT_HOME/webapps/ROOT; unzip \$HOME/$WAR_FILE -d \$TOMCAT_HOME/webapps/ROOT"
}

function startTomcat() {
    ssh ec2-user@$EC2_HOST \
        'source ~/.bash_profile; $TOMCAT_HOME/bin/startup.sh'
}

function stopTomcat() {
    ssh ec2-user@$EC2_HOST \
        'source ~/.bash_profile; $TOMCAT_HOME/bin/shutdown.sh'
}

function startJstat() {
    ssh ec2-user@$EC2_HOST \
        "source ~/.bash_profile; jcmd | sed -n -r 's/([0-9]+) org.apache.catalina.startup.Bootstrap start/\1/p' | xargs -I {} jstat -gc {} 1s > ~/logs/jstat.log &"
}

function stopJstatAndCollectLog() {
    # jstat コマンドのプロセスを kill
    ssh ec2-user@$EC2_HOST \
        "source ~/.bash_profile; jcmd | sed -n -r 's/([0-9]+) sun.tools.jstat.Jstat.+/\1/p' | xargs kill"

    # jstat.log をローカルにダウンロード
    scp ec2-user@$EC2_HOST:/home/ec2-user/logs/jstat.log $WORK_DIR

    # 必要な項目だけを抽出して、タブ区切りにして別ファイルに出力
    awk 'BEGIN { OFS = "\t" } { print $6,$8,$13,$14,$15,$16,$17}' $WORK_DIR/jstat.log > jstat_filtered.log
}

function runJMeterForWarmUp() {
    local JMETER_REPORT_DIR=$WORK_DIR/reports_warm_up
    local JMX_FILE=$THIS_DIR/test.jmx

    cd $WORK_DIR

    jmeter.sh -n \
        -t $JMX_FILE \
        -j $WORK_DIR/test.log \
        -e -l $JMETER_REPORT_DIR/test.jtl \
        -o $JMETER_REPORT_DIR \
        -Jthread.number=1 \
        -Jserver.host=$EC2_HOST \
        -Jtest.duration=60
}

function runJMeter() {
    local JMETER_REPORT_DIR=$WORK_DIR/reports
    local JMX_FILE=$THIS_DIR/test.jmx

    cd $WORK_DIR

    jmeter.sh -n \
        -t $JMX_FILE \
        -j $WORK_DIR/test.log \
        -e -l $JMETER_REPORT_DIR/test.jtl \
        -o $JMETER_REPORT_DIR \
        -Jthread.number=$THREAD_NUMBER \
        -Jserver.host=$EC2_HOST \
        -Jtest.duration=$TEST_DURATION
}

function collectLogs() {
    local LOG_ZIP_FILE_NAME=logs_`date "+%Y%m%d_%H%M%S"`.zip

    # Tomcat とアプリのログを zip 圧縮
    ssh ec2-user@$EC2_HOST \
        "source ~/.bash_profile; zip -j ~/$LOG_ZIP_FILE_NAME ~/logs/* \$TOMCAT_HOME/logs/*"
    
    # zip をローカルにダウンロード
    scp ec2-user@$EC2_HOST:/home/ec2-user/$LOG_ZIP_FILE_NAME $WORK_DIR

    # 圧縮前のログを削除
    ssh ec2-user@$EC2_HOST \
        'source ~/.bash_profile; rm ~/logs/* $TOMCAT_HOME/logs/*'

    # リモートの zip を削除
    ssh ec2-user@$EC2_HOST \
        "source ~/.bash_profile; rm ~/$LOG_ZIP_FILE_NAME"
}

# main 処理
echo THREAD_PATTERN=$THREAD_PATTERN
echo LOOP_COUNT=$LOOP_COUNT

OUT_DIR=$THIS_DIR/logs/$TARGET/test_`date "+%Y%m%d_%H%M%S"`
mkdir -p $OUT_DIR

# スレッド（ユーザ）数ループ
for THREAD_NUMBER in $THREAD_PATTERN
do
    THREAD_DIR=$OUT_DIR/$THREAD_NUMBER
    mkdir $THREAD_DIR

    # 試行回ループ
    for i in `seq 1 $LOOP_COUNT`
    do
        WORK_DIR=$THREAD_DIR/$i
        mkdir $WORK_DIR
        
        STATE="(thread=$THREAD_NUMBER, i=$i)"
        echo ===== Initialize Database $STATE =====
        initializeDatabase

        echo ===== Deploy War $STATE =====
        deployWar

        echo ===== Start Application Server $STATE =====
        startTomcat

        echo ===== Waiting Application Server Started =====
        until curl http://${EC2_HOST}:8080 -I | grep "HTTP/1.1 200"
        do
            sleep 5
        done

        echo ===== Warm Up $STATE =====
        runJMeterForWarmUp

        echo ===== Start Jstat $STATE =====
        startJstat

        echo ===== Run JMeter $STATE =====
        runJMeter

        echo ===== Stop Jstat $STATE =====
        stopJstatAndCollectLog

        echo ===== Stop Application Server $STATE =====
        stopTomcat

        echo ===== Collect Application Logs $STATE =====
        collectLogs
    done
done

echo ===== Collect Total Throughput =====
cd $THIS_DIR/CollectTotalThroughput
if [ ! -e CollectTotalThroughput.class ]; then
  javac CollectTotalThroughput.java
fi
java -cp . CollectTotalThroughput $OUT_DIR | sort -t $'\t' -k 1,2 -n > $OUT_DIR/total_throughput.txt
