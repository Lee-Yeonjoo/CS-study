package ch3_syn;

public class RaceCondition {
    static int sharedData = 0;  //공유 변수

    static class Increment implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                sharedData++;  //공유 변수 증가
            }
        }
    }

    static class Decrement implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                sharedData--;  //공유 변수 감소
            }
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

        System.out.println("sharedData = " + sharedData);  //0이 나오는걸 기대했지만 race condition이 발생해 일정하지 않은 값이 출력됨
    }
}
