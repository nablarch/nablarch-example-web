# 検証環境構築手順

このディレクトリには、性能検証を行うために AWS 環境を Terraform で構築するためのスクリプトなどが配置されている。
ここでは、環境構築の手順について説明する。

## Terraform で構築されるもの

本ディレクトリ内の Terraform を実行すると、以下のものが構築できる。

- 性能検証の実行基盤となるAWSリソースの構築
- 性能検証の準備作業（ツールのインストールや、アプリのビルドなど）

つまり、 Terraform を実行することで「あとはテストを実行するだけ」の状態を構築できる。

### 性能検証の実行基盤となるAWSリソースの構築

AWS リソースは、大まかに以下のものが構築される。
詳細は、各 tf ファイルの中身を参照のこと。

- `network.tf`
    - VPC と３つのサブネット
        - パブリックサブネット１つと、プライベートサブネットが２つ
    - EC2 インスタンスなどに割り当てるセキュリティグループ
- `ec2.tf`
    - インスタンスに SSH 接続するときに使用する Key Pair
    - ３つの EC2 インスタンス
        - 踏み台サーバー(`performance-web-bastion-instance`)
        - 性能検証の対象となるアプリケーションを稼働させる EC2 インスタンス(`performance-web-ap-instance`)
        - JMeterを実行する EC2 インスタンス(`performance-web-jmeter-instance`)
- `rds.tf`
    - アプリケーションから接続する RDS インスタンスと、 DB サブネットグループ

### 性能検証の準備作業

性能検証の準備作業では、大きく以下のことが行われる。

- JDK など、必要なミドルウェアのインストール
- アプリケーションのビルドと配備

これらの処理は、 `aws_instance` の `user_data` で設定するシェルスクリプトで実現している。
詳細は、 `ec2.tf` の内容を参照のこと。

## ツールのインストール

以下のツールが必要になるので、あらかじめインストールしておくこと。

- AWS CLI 2.0.14+
- Terraform v0.13.5+
- Git-Bash
    - Windows の場合(SSHキーの作成に使用)

## 前提条件

以下の準備ができていることが前提となる。

- AWS が利用可能な状態になっていること
    - EC2, RDS を構築する権限を持つIAMユーザのアクセスキー・シークレットキーが作成できていること
- EIP が作成済みであること
    - EC2 インスタンスに割り当てる EIP は事前に作成している前提です

## SSH キーを作成する

Git-Bash で本ディレクトリに移動し、以下のコマンドを実行する。

```bash
$ mkdir ssh-key

$ ssh-keygen -t rsa -f ssh-key/performance -N ''
```

`ssh-key` ディレクトリの下に `performance`, `performance.pub` のファイルが作成できていることを確認する。

## AWS CLI の認証情報を設定する

以下の環境変数を設定する。

- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_DEFAULT_REGION`

## Terraform の変数ファイルを作成する

このディレクトリに `terraform.tfvars` というファイルを作成し、変数を定義する。
定義する変数については、 `variables.tf` を参照（デフォルト値が無いものは定義が必須）。

## Terraform を実行する

コマンドラインで本ディレクトリに移動して、以下のコマンドを実行する。

```
$ terraform init

$ terraform apply
```

### EC2 インスタンスの初期化

EC2 インスタンス初期化時に実行されるシェルスクリプトは、実行完了までにある程度時間がかかる。
このため、起動後すぐに接続した段階では、まだスクリプトが完了していない可能性がある。

スクリプトが完了しているかどうかは、 `/var/log/messages` を `tail` などで監視することで確認できる。

## EIP と EC2 インスタンス(踏み台サーバー)の関連付けを行う

Terraform の実行が完了して踏み台サーバーの EC2 インスタンスが作成されたら、 EIP との関連付けを行う。
操作は、 AWS コンソール上から手動で行う。

なお、踏み台サーバーの名前は `performance-web-bastion-instance` で構築されている。

## SSH で接続できるか確認する

```bash
$ ssh -i ssh-key/performance ec2-user@<EIPのIPアドレス>
```

### APサーバー、 JMeter マシンに接続できるか確認
```bash
# AP サーバー(performance-web-ap-instance)
$ ssh ec2-user@172.16.2.10

# JMeter マシン(performance-web-jmeter-instance)
$ ssh ec2-user@172.16.2.11
```

- SSH の公開鍵は、 `performance-web-bastion-instance` に配備したのものと同じ鍵が各マシンに配備されている
- また、 `performance-web-bastion-instance` の `.ssh/id_rsa` に秘密鍵を配備しているので、特に準備作業をせずに SSH 接続の確認ができる
- IP アドレスは、 `ec2.tf` ファイルを変更していなければ上記アドレスで接続できるように構築されている

## テストの実行

`jmeter/README.md` を参照。
