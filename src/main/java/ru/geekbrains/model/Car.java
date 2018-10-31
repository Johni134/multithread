package ru.geekbrains.model;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {
    private static int CARS_COUNT;
    final private AtomicBoolean winnerDefined;
    final private CyclicBarrier cyclicBarrier;
    final private CountDownLatch cdl;
    final private CountDownLatch cdlLast;
    static {
        CARS_COUNT = 0;
    }

    final private Race race;
    final private int speed;
    final private String name;

    public Car(Race race, int speed, CyclicBarrier cyclicBarrier, CountDownLatch cdl, CountDownLatch cdlLast, AtomicBoolean winnerDefined) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.cyclicBarrier = cyclicBarrier;
        this.cdl = cdl;
        this.cdlLast = cdlLast;
        this.winnerDefined = winnerDefined;
    }

    String getName() {
        return name;
    }

    int getSpeed() {
        return speed;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            // накапливаем для сброса
            cdl.countDown();
            // ждем пока все приготовятся
            cyclicBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        if (!winnerDefined.getAndSet(true)) {
            System.out.println(this.name + " - WIN");
        }
        // накапливаем завершение всех процессов для сброса
        cdlLast.countDown();
    }
}
