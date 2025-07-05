package ch3_syn;

import java.util.concurrent.Semaphore;

public class Sema {
    static int sharedData = 0;  //공유 변수
    static Semaphore semaphore = new Semaphore(1);  //세마포어 생성, 공유 자원 1개

    static class Increment implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                try {
                    semaphore.acquire();  //세마포 획득
                    sharedData++;  //공유 변수 증가
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();  //세마포 해제
                }
            }
        }
    }

    static class Decrement implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                try {
                    semaphore.acquire();  //세마포 획득
                    sharedData--;  //공유 변수 감소
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();  //세마포 해제
                }
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

        System.out.println("sharedData = " + sharedData);  //세마포로 동기화하여 0이 출력됨
    }
}
