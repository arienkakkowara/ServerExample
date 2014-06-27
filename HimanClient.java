package ex03;

import java.io.*;
import java.net.*;

/**
 * 肥満度測定クライアント
 * キーボードで身長体重入力するとBMI計算して太りすぎかどうか判定してくれるよ
 * テスト用なんで実数値以外入力した時の例外処理とかはないよ
 * @author arienkakkowara
 */
public class HimanClient {

    public static void main(String[] args) throws IOException {
        // サーバーソケットと入出力たち
        Socket kaiwaS = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            // 自身のアドレスの50001番ポートにアクセス
            kaiwaS = new Socket(InetAddress.getLocalHost(), 50001);
            // 
            in = new BufferedReader(new InputStreamReader(kaiwaS.getInputStream()));
            out = new PrintWriter(kaiwaS.getOutputStream(), true);
        } catch (UnknownHostException e) {
            System.out.println("ホストに接続できません。");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IOコネクションを得られません。");
            System.exit(1);
        }

        // キーボード入力に必要なインスタンス生成
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        // サーバー、クライアントの送受信する文字列を管理する変数
        String fromServer;
        String fromUser;
        
        
        while ((fromServer = in.readLine()) != null) {
            // 肥満度サーバーから受け取った文字列を表示するよ
            System.out.println("肥満度サーバ: " + fromServer);
            
            // サーバーからもらった文字列の終わりがですならおしまい
            if (fromServer.endsWith("です"))
                break;
            
            // キーボード入力1行分をString型変数に格納するよ
            fromUser = stdIn.readLine();
            // サーバーにString型文字列を送りつけるよ
            out.println(fromUser);
        }

        // いろいろ閉じるよ
        out.close();
        in.close();
        stdIn.close();
        kaiwaS.close();
    }
}
