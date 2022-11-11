
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class client1 {
    static Socket socket;

    public static class read1 implements Runnable {

        //인풋스트림
        BufferedReader input;
        @Override
        public void run() {
            try{
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String msg;
                while(true){
                    if(!((msg = input.readLine()).isEmpty())){
                        System.out.println(msg);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                return;
            }
            //읽기해보자
        }
    }
    public static class write1 implements Runnable{

        PrintWriter output;
        Scanner scan;
        @Override
        public void run() {
            //쓰기해보자
            try{
                output = new PrintWriter(socket.getOutputStream(),true);
                scan = new Scanner(System.in);

                while (true){
                        if (scan.hasNext())
                            output.println(scan.nextLine());
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return;

            }

        }
    }
    public static void main(String[]args){

        //여기서 읽기 함수 하나 쓰기 함수하나 구현해야됨
        //흠 어케하지 흠
        //흠흠흠
        //흐믛ㅁ흐흠
        //읽기쓰기를 동시에 와일문으로 돌려야되는데
        //흠흠흐흠
        //흠흠흠흐므흫므흠흐흠흐 어케하지
        //흐흠흠흐믛므흐흠흠흠흐
        //흠흐믛믛믛
        //ㅂㅅ인가
        //쓰레드 다시만들기 2개
        try{
            socket = new Socket("172.20.10.5" , 5050) ;
            System.out.println("서버와 접속이 되었습니다.");
//            OutputStream output = socket.getOutputStream();
            new Thread(new read1()).start();
            new Thread(new write1()).start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
//             socket.close();
        }

    }

}
