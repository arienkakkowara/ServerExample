package ex03;

import java.io.*;
import java.net.*;

/**
 * BMIを計算して太りすぎかどうか判定してくれるよ
 * 体重と身長をクライアントから要求してBMIを計算するよ
 * @author arienkakkowara
 */
public class HimanServer {

    public static void main(String[] args) throws IOException {
        // サーバーとクライアントのソケット
        ServerSocket serverS = null;
        Socket clientS = null;
        try {
            // 50001番ポートを開放するよ
            serverS = new ServerSocket(50001);
        } catch (IOException e) {
            // ポート開放失敗
            System.out.println("ポートにアクセスできません。");
            System.exit(1);
        }
        try {
            // クライアントからの接続要求を受け入れるよ
            clientS = serverS.accept();
        } catch (IOException e) {
            // だめだったよ
            System.out.println("Acceptに失敗しました。");
            System.exit(1);
        }
        
        // BMI計算に必要な変数たち(最低限とは言ってない)
        Double w, h, bmi;
        // BMIの評価
        String result;
        PrintWriter out = new PrintWriter(clientS.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientS.getInputStream()));

        out.println("あなたの体重を(㎏)を入力してください");
        w = Double.parseDouble(in.readLine());

        out.println("あなたの身長を(cm)を入力してください");
        h = Double.parseDouble(in.readLine()) / 100;

        bmi = w / (h * h);

        if (bmi <= 15)
            result = "痩せすぎ";
        else if (bmi <= 20)
            result = "痩せぎみ";
        else if (bmi <= 25)
            result = "ふつう";
        else if (bmi <= 30)
            result = "太りぎみ";
        else
            result = "太りすぎ";

        out.printf("あなたの標準体重は%.2fkg。肥満度は%sです", h * h * 22, result);

        in.close();
        out.close();
        clientS.close();
        serverS.close();
    }

    /*
    
     // マルチクライアント対応パターン
    
     public static void main(String[] args) throws IOException {
     ServerSocket serverS = null;
     Socket socket = null;
     boolean end = true;
     try {
     serverS = new ServerSocket(50001);
     } catch (IOException e) {
     System.out.println("ポートにアクセスできません。");
     System.exit(1);
     }
     while(end) {
     new HimanMThread(serverS.accept()).start();
     }
     }
     */
}

/**
 * サーバーの処理を行うスレッド
 * クライアントから身長体重もらってBMI計算するよ
 * @author arienkakkowara
 */
class HimanMThread extends Thread {

    Socket socket = null;

    /**
     * こんすたらくた
     * @param s そけっと
     */
    public HimanMThread(Socket s) {
        super("HimanMSThread");
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
            String fromC, fromUser = "";
            
            // BMI計算クラスのインスタンス生成
            BMICalculator bm = new BMICalculator();
            
            // サーバー「体重入れてね」
            out.println(bm.speak(fromUser));

            // クライアントからの文字列がぬるじゃない限り繰り返すよ
            while ((fromUser = in.readLine()) != null) {
                // クライアントに送りつけるメッセージ決めるよ
                fromC = bm.speak(fromUser);
                // クライアントに送りつけるよ
                out.println(fromC);
                // クライアントに送りつけるメッセージの終わりがですならおしまい
                if (fromC.endsWith("です"))
                    break;
            }
            
            // いろいろ閉じるよ
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            // なんかだめだった
            System.out.println("runメソッド実行中例外:" + e);
            System.exit(1);
        }
    }
}

/**
 *  BMIの計算とクライアントに送りつけるメッセージを決めるクラス
 * 
 */
class BMICalculator {
    double height, weight, normal, bmi;
    String result;
    int n = 0;
    String[] msg = {
        "痩せすぎ",
        "痩せぎみ",
        "ふつう",
        "太りぎみ",
        "太りすぎ",
    };

    String speak(String user) {
        n++;
        // 全体的にー1すれば良かったんじゃないか説
        switch (n%3+1) {
            case 1:
                return "あなたの体重を(㎏)を入力してください";
            case 2:
                weight = Double.parseDouble(user);
                return "あなたの身長を(cm)を入力してください";
            case 3:
                height = Double.parseDouble(user) / 100;
                normal = (double) ((int) (100 * height * height * 22)) / 100;
                bmi = weight / (height * height);
                if (bmi <= 15)
                    result = msg[0];
                 else if (bmi <= 20)
                    result = msg[1];
                 else if (bmi <= 25)
                    result = msg[2];
                 else if (bmi <= 30)
                    result = msg[3];
                 else 
                    result = msg[4];
                
                return "あなたの標準体重は" + normal + "kg。肥満度は" + result + "です";
            default:
                return "nu";
        }
    }
}
