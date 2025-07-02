package ch3_syn;

import ch3_syn.RaceCondition;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MutexLock {
    static int sharedData = 0;  //공유 변수
    static Lock lock = new ReentrantLock();  //락 선언

    static class Increment implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                lock.lock();  //락 획득
                sharedData++;  //공유 변수 증가
                lock.unlock();  //락 해제
            }
        }
    }

    static class Decrement implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                lock.lock();  //락 획득
                sharedData--;  //공유 변수 감소
                lock.unlock();  //락 해제
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

        System.out.println("sharedData = " + sharedData);  //락으로 동기화하여 0이 출력됨
    }
}
