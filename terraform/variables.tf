variable "ssh_client_cidr_list" {
  description = "踏み台サーバーへのSSH接続を許可するクライアント側のcidrリスト"
  type = list(string)
}

variable "tomcat_version" {
  description = "APサーバーにインストールするTomcatのバージョン（設定例：9.0.41）"
  type = string
}

variable "jdk_version" {
  description = "使用するJDKのバージョン（設定例：8u275）"
  type = string
}

variable "jmeter_version" {
  description = "使用するJMeterのバージョン（設定例：5.2.1）"
  type = string
  default = "5.2.1"
}

variable "maven_version" {
  description = "使用するMavenのバージョン（設定例：3.6.3）※メジャーバージョンは 3 前提"
  type = string
  default = "3.6.3"
}

variable "aws_access_key" {
  description = "アプリケーション起動時に環境変数に設定するAWSのアクセスキー"
  type = string
}

variable "aws_secret_access_key" {
  description = "アプリケーション起動時に環境変数に設定するAWSのシークレットアクセスキー"
  type = string
}

variable "git_branch_name" {
  description = "JMeterマシンでチェックアウトするブランチ名"
  type = string
}

variable "git_old_tag_name" {
  description = "修正前のバッチのバージョンに割り当てられたタグ"
  type = string
}

variable "git_new_tag_name" {
  description = "修正後のバッチのバージョンに割り当てられたタグ"
  type = string
}

variable "old_war_final_name" {
  description = "mvn package で生成されるwarファイルのベース名(修正前モジュール)"
  type = string
  default = "example-web-old"
}

variable "new_war_final_name" {
  description = "mvn package で生成されるwarファイルのベース名(修正後モジュール)"
  type = string
  default = "example-web-new"
}