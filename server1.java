
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class server1{


    private static HashMap<String, PrintWriter> countconnect = new HashMap<>();


    public static class good implements Runnable {

        //클라이언트가 acapct로 들어올때마다 thread를 만들어서 run시킴
        //run안에 while무한반복문으로 run이 안끝나게 한다 언제까지? 입력값이/exit일때까지
        //굳
        //근데 각 쓰레드마다 구별해야된다.
        //그리고 쓰레드 한명이 입력해쓸때 서버를 거쳐서 다른 쓰레드에게 입력값을 전송해야된다.
        //그러면 서버에서 입력값받으면 클라이언트는 무한읽어오기 하자 (무한이 아니라 인풋스트림을 하는거임 없으면 안받기)어떰?
        //좋다
        private Socket sock;

        public good(Socket sock){
            this.sock = sock;
        }

        @Override
        public void run() {

            //자 소켓연결되고 쓰레드로 넣어서 run하면 이게 함수가 시작된다.
            //여기서 와일문으로 입력읽어오고 출력내보내기 하면된다. 자기자신 아이피 포함해서
            //입력과 출력은 따로 함수만들어도 될듯?
            //와일문은 함수안이 아닌 run mathod에서 해야 좋아보임 흠~~~
            PrintWriter output;
            BufferedReader input;
            try{
//                connectedClients.put(sock.getInetAddress().toString(),new PrintWriter(sock.getOutputStream(), true));
                System.out.println(countconnect.values());
                output = new PrintWriter(sock.getOutputStream(), true);
                input = new BufferedReader(
                        new InputStreamReader(sock.getInputStream()));

                output.println("이름 적어보시겠어요?");
                //output.println 테스트는 안해봣는데 뿌리는거 일걸 겟아웃풋스트림 프린트롸이터 즉 클라이언트한테 뿌리기다.
                //sock.getoutputstream으로 프린트롸이터개체 생성 오토플러쉬이므로 프린트할때마다 커밋후 바로 플러쉬된다
                //즉 클라이언트마다 다르게 된다 왜냐 클라이언트마다 쓰레드 만들었기때문ㄴ이다.
                //그리고 전역변수로 커넥트 몇명인지 공유하기까지 미쳣노
                String name = input.readLine();
                System.out.println(name + "1");
                output.println(name+ "입니다.");
                System.out.println(name + "2");

                countconnect.put(name,output);
                String msg;
                while(true){
                    //여기서 읽고쓰기?
                    //아니다 여기는 서버다
                    //읽기만하자
                    //그니가 즉 이 쓰레드 1개당 클라이언트 하나라고 생각하자면
                    //클라이언트가 sock 인거임 그리고 거기서 읽어와야된다 쟤들이 stream으로 쓴거 리드라인으로 리드라인쓸려면

                    while ((msg = input.readLine()) != null)//클라이언트가 쓴메시지 읽어와서 모든 클라이언트에게 뿌리는 부분임
                        //리드라인으로 묶는다. input.readline인데 input이 소켓.getinputstream으로 클라이언트에서
                        //메시지 쓸때 inputstream으로 넣어야된다.여기 서버소켓의 인풋스트림으로 넣으면 input에 들어와서 리드라인으로 읽을 수 있다.
                        //오우
                   {
                        if (!(msg.isEmpty()))
                        {
                            //나가는거 구현해보자이break;하면 while문나감
                            //그러면 run문끝남 그러면 리무브하고 나가기
                            if (msg.equals("/exit"))
                            {
                                countconnect.remove(name);
                                break;
                            }
                            output.println("["+sock.getInetAddress()+"]" +"["+sock.getPort()+"] "+ name + " : " + msg);
                        }
                    }
                }
            }
            catch (Exception e){

            }




        }
    }
    public static void main(String[]args) throws Exception{

        int port = 5050;

        ServerSocket ssk = new ServerSocket(port);

        System.out.println("server port : " + port);
        System.out.println(ssk.getInetAddress());
        System.out.println(ssk.getLocalSocketAddress());
        System.out.println( ssk.getReuseAddress());
        while(true) {
            Socket sock = ssk.accept();
            System.out.println(sock.getInetAddress());
            System.out.println("사용자 접속 했습니다");
            System.out.println("Client ip :"+ sock.getInetAddress());

            if(countconnect.size()<5){

                Thread thread = new Thread(new good(sock));
                //여기서 쓰레드마다 구별해야된다.
                //? 맞나
                //일단 쓰레드 런하자
                thread.start();
            }

            //전역변수 해쉬맵만들어서 관리하자 제한은 여기서 빼기넣기는 run에서 좋다

            //run이 아니라 start를 해야 실행된다. 왜냐 implement이기 때문에 구현체가 필요하다

            //근데 여기서 사용자수를 제한하고싶다
            //그러면 while (true가아니라
            //while count 5명으로 제한하자 어떰 하지만 서버는 계속 돌아가야된다
            //근데 여기서 count늘려서 제한하면 클라이언트가 나갓을 경우를 카운트하지못한다.
            //제한하는건 run에서 해야되나 그러자 run에서 전역변수 5제한을둬서 아니면 바로 쓰레드가 종료하고 인원수초과햇다고 뜨게하기


        }



    }

}
