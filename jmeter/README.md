# README
## テストの内容
- 以下のスレッド数（同時接続ユーザー数）でテストを実行する
    - 1
    - 5
    - 10
    - 50
    - 100
- それぞれのスレッド数で、1回につき3分間 JMeter のテストを5回ずつ実行する
    - 3(minutes) * 5(counts) * 5(patterns)
    - 内訳
        - 1スレッドで3分間テストを5回実行
        - 5スレッドで3分間テストを5回実行
        - 10スレッドで3分間テストを5回実行
        - 50スレッドで3分間テストを5回実行
        - 100スレッドで3分間テストを5回実行
- それぞれのスレッド数で、スループットの中央値を採用する

## 環境構築

性能検証の環境構築については `terraform/README.md` を参照。

## 実行方法

Terraform で構築した JMeter 実行用の EC2 インスタンス(`performance_web_jmeter_instance`)に SSH で接続する。

`run-test.sh` を実行する。

```bash
$ cd /home/ec2-user/nablarch-example-web/jmeter

$ chmod +x run-test.sh

$ ./run-test.sh
```

初回は `env.sh` ファイルが生成されるので、内容を確認して変数を設定する。

```bash
$ vi /home/ec2-user/nablarch-example-web/jmeter/env.sh
```

`env.sh` の設定が完了したら、対象を指定して `run-test.sh` を実行する。

```bash
$ cd /home/ec2-user/nablarch-example-web/jmeter

# 修正前モジュールを実行する
$ ./run-test.sh old

# 修正後モジュールを実行する
$ ./run-test.sh new
```

- `/home/ec2-user/nablarch-example-batch/performance_test/logs` の下にログが出力される。
- `logs` の下は、 `logs/<old|new>/test_<起動時刻>/<スレッド数>/<試行回数>` というルールでディレクトリが作成される
- 全テスト終了するのに **1時間半程度かかる** ので注意
- 全テストが終了すると、 `logs/<old|new>/test_<起動時刻>/total_throughput.txt` に各ケースでのトータルのスループットがまとめられて出力される
    - `logs/<old|new>/test_<起動時刻>/jstat_filtered.log` には、 jstat のログから必要な項目だけを抽出してタブ区切りにした情報が出力されている
- これらを、 Excel などに張り付けてグラフ化して確認する
