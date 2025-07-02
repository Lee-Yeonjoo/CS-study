package ch3_syn;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ThreadSafe {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> vector = new Vector<>();

        //리스트에 요소 추가하는 작업을 실행할 스레드 각 각 생성 -> 각 각 5000개씩 추가
        Thread arrayListThread1 = new Thread(() -> addElements(arrayList, 0, 5000));
        Thread arrayListThread2 = new Thread(() -> addElements(arrayList, 5000, 10000));
        Thread vectorThread1 = new Thread(() -> addElements(vector, 0, 5000));
        Thread vectorThread2 = new Thread(() -> addElements(vector, 5000, 10000));

        arrayListThread1.start();
        arrayListThread2.start();
        vectorThread1.start();
        vectorThread2.start();

        arrayListThread1.join();
        arrayListThread2.join();
        vectorThread1.join();
        vectorThread2.join();

        System.out.println("ArrayList size: " + arrayList.size());  //레이스 컨디션이 발생해 제대로 요소 추가가 되지 않음
        System.out.println("Vector size: " + vector.size());  //스레드 안전하므로 레이스 컨디션x -> 10000 출력됨
    }

    private static void addElements(List<Integer> list, int start, int end) {
        for (int i = start; i < end; i++) {
            list.add(i);
        }
    }
}
