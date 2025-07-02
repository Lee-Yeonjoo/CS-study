package ch3_syn;

//모니터로 상호배제하는 코드
public class Monitor {
    static int sharedData = 0;  //공유 변수

    static class Increment implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                changeSharedData(1);  //공유 변수 증가
            }
        }
    }

    static class Decrement implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                changeSharedData(2);  //공유 변수 감소
            }
        }
    }

    //synchronized를 추가하면 동기화가 된다.
    public static synchronized void changeSharedData(int threadNum) {
        if (threadNum == 1) {
            sharedData++;
        } else if (threadNum == 2){
            sharedData--;
        }
    }

    public static void main(String[] args) {
        //스레드 생성
        Thread thread1 = new Thread(new Increment());
        Thread thread2 = new Thread(new Decrement());

        //스레드 시작
        thread1.start();
        thread2.start();

        try {
            //스레드 종료 대기
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("sharedData = " + sharedData);  //synchronized로 동기화해서 공유데이터에 딱 하나의 스레드만 접근 가능하므로 0 출력됨
    }
}
