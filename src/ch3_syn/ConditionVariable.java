package ch3_syn;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionVariable {
    private static final Lock lock = new ReentrantLock();
    private static boolean ready = false;  //락이 필요한 공유 변수?
    private static final Condition cond = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new ThreadJob1());
        Thread t2 = new Thread(new ThreadJob2());

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    static class ThreadJob1 implements Runnable {
        @Override
        public void run() {
            System.out.println("t1: 먼저 시작");

            lock.lock();
            try {
                System.out.println("t1: 2초 대기");
                while (!ready) {
                    cond.await();  //실행 중단
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            System.out.println("t1: 다시 시작");
            System.out.println("t1: 종료");
        }
    }

    static class ThreadJob2 implements Runnable {
        @Override
        public void run() {
            System.out.println("t2: 2초 실행 대기");
            try {
                Thread.sleep(2000);  //2초 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("t2: 실행 완료");
            lock.lock();
            try {
                ready = true;
                cond.signal();  //wait 걸린 t1 실행 재개
            } finally {
                lock.unlock();
            }
        }
    }
}
