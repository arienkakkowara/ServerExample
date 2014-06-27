package ex01;

import java.io.*;
import java.net.*;

/**
 * マルチクライアント対応のサーバ
 * 1対1関係のサーバプログラムはHimanServer.javaを参照してぬ
 * @author arienkakkowara
 */
public class SimpleEncodeServer {

    public static void main(String[] args) throws IOException {
        // サーバーのソケット
        ServerSocket serverS = null;
        // 終了フラグ(処理しないので永遠にtrue)
        boolean end = true;
        try {
            // 50002番ポートを開放するよ
            serverS = new ServerSocket(50002);
        } catch (IOException e) {
            // しくじった
            System.out.println("ポートにアクセスできません。");
            System.exit(1);
        }
        while(end) {
            // すれっどすたあと(常にポートアクセスがあったやつは随時接続し処理開始するよ)
            new SimpleEncodeMThread(serverS.accept()).start();
        }
    }
    
    
}

/**
 * サーバーの処理を行うスレッド
 * クライアントからもらった文字列をシーザー暗号でえいってやるよ
 * @author arienkakkowara
 */
class SimpleEncodeMThread extends Thread {
    // サーバーソケット
    Socket socket = null;

    /**
     * こんすたらくた
     * @param s そけっと
     */
    public SimpleEncodeMThread(Socket s) {
        super("SimpleEncodeMSThread");
        socket = s;
    }

    /**
     * スレッド(サーバーの処理)
     */
    public void run() {
        try {
            // クライアントへ出力するアレ作る
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // クライアントからの文字列を受け取るバッファ作る
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // サーバーからの文字列、ユーザーからの文字列
            String fromServer, fromUser;
                
            // クライアントへおじぎする
            out.println("Hello, Enter 'Bye' to exit");

            // クライアントからの文字列がカラじゃない、かつ、byeじゃない
            while ((fromUser = in.readLine()) != null && !fromUser.equals("Bye")) {
                // クライアントからの文字列を処理するよ
                fromServer = encode(fromUser);
                // クライアントに送り付けるよ
                out.println(fromServer);
            }
                
            // いろいろ閉じる
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            // なんかだめだった
            System.out.println("runメソッド実行中例外:" + e);
            System.exit(1);
        }
    }
        
    /**
     * シーザー暗号で文字列いじるよ
     * @param aString ずらしたい文字列
     * @return 英字を1文字ずらしたやつ
     */
    public String encode(String aString) {
        // 返す文字列(おなかすいた)
        String str = "";
        
        // 文字列の数だけ繰り返す
        for (int i = 0; i < aString.length(); i++) {
            // i番目の文字を取得
            char c = aString.charAt(i);
                
            // 英字だったら1文字ずらす
            if( c >= 'a' && c < 'z' || c >= 'A' && c < 'Z')
                c++;
            else if ( c=='z' || c =='Z')
                c -= 25;
                
            // 文字たすよ
            str += c;
        }
        // 全部足した文字列返すよ
        return str;
    }
}
