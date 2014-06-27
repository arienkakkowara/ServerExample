package ex01;

import java.io.*;
import java.net.*;

/**
 * サーバクライアントクラス
 * IPの設定とかしんどいからローカルホストの50002番ポートたたくようにするよ
 * キーボード入力1行分をサーバーに送りつける→もらった出力をコンソールに垂れ流すの繰り返しだよ
 * @author arienkakkowara
 */
public class SimpleEncodeClient {

    public static void main(String[] args) throws IOException {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            // ローカルホスト(自身のアドレス)の50002番ポートの門をたたくよ
            socket = new Socket(InetAddress.getLocalHost(), 50002);
            // サーバーソケットからの入力を確保するバッファ
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // サーバーソケットへ出力するアレ
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (UnknownHostException e) {
            // 接続しくじった
            System.out.println("ホストに接続できません。");
            System.exit(1);
        } catch (IOException e) {
            // 接続できたけどなんか駄目だった
            System.out.println("IOコネクションを得られません。");
            System.exit(1);
        }

        // きいぼおどにゅうりょく
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        
        // サーバーからもらう文字列、ユーザが入力した文字列
        String fromServer;
        String fromUser;
        
        // サーバーからもらったもじれつがある限り続ける
        while ((fromServer = in.readLine()) != null ) {
            // サーバーからもらった文字列表示
            System.out.println("Server: " + fromServer);
            // きいぼおどにゅうりょく
            fromUser = stdIn.readLine();
            // 入力した文字列をサーバに出力
            out.println(fromUser);
        }

        // 各入出力を閉じるよ
        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }
}
