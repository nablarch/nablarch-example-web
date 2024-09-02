nablarch-example-web
===========================

Nablarchアプリケーションフレームワークを利用して作成したウェブExampleアプリケーションです。

## 実行手順

### 1.動作環境
実行環境に以下のソフトウェアがインストールされている事を前提とします。
* Java Version : 17
* Maven 3.9.0以降

以下は、本手順では事前準備不要です。

|ソフトウェア|説明|
|:---|:---|
|APサーバ|このアプリケーションはJetty12を使用しています。jetty-ee10-maven-pluginはJetty12へのアプリケーションのデプロイ、起動を行います。|
|DBサーバ|このアプリケーションはH2 Database Engine(以下H2)を組み込んであるため、別途インストールの必要はありません。|

### 2. プロジェクトリポジトリの取得
Gitを使用している場合、アプリケーションを配置したいディレクトリにて「git clone」コマンドを実行してください。
以下、コマンドの例です。

    $mkdir c:\example
    $cd c:\example
    $git clone https://github.com/nablarch/nablarch-example-web.git

Gitを使用しない場合、最新のタグからzipをダウンロードし、任意のディレクトリへ展開してください。


### 3. アプリケーションのビルドと起動
jetty-ee10-maven-pluginを実行し、組み込みJetty 12を起動させます。以下のコマンドを実行してください。

    $mvn jetty:run

※`jetty:run`を実行する際にデータベースのセットアップ及びエンティティクラスの作成を行う`generate-resources`、アプリケーションのビルドを行う`compile`も合わせて実行されるため、明示的に実行する手順は不要となります。

起動に成功したら、ブラウザで http://localhost:8080/ を開いてください。ログイン画面が表示されます。
以下のログインID、パスワードでログインできます。

| ログインID | パスワード | ロール     |
|:-----------|:-----------|:-----------|
| 10000001   | pass123-   | 管理者     |
| 10000002   | pass123-   | 一般ユーザ |

※プロジェクトの一括アップロード機能は、管理者ロールを持つユーザだけが使用できます。

### 4. DBの確認方法

1. https://www.h2database.com/html/download.html からH2をインストールしてください。  


2. {インストールフォルダ}/bin/h2.bat を実行してください(コマンドプロンプトが開く)。  
  ※h2.bat実行中はExampleアプリケーションからDBへアクセスすることができないため、Exampleアプリケーションを停止しておいてください。

3. ブラウザから http://localhost:8082 を開き、以下の情報でH2コンソールにログインしてください。
   JDBC URLの{dbファイルのパス}には、`nablarch_example.mv.db`ファイルの格納ディレクトリまでのパスを指定してください。  
  JDBC URL：jdbc:h2:{dbファイルのパス}/nablarch_example  
  ユーザ名：NABLARCH_EXAMPLE  
  パスワード：NABLARCH_EXAMPLE
