package ru.geekbrains;

import ru.geekbrains.model.Car;
import ru.geekbrains.model.Race;
import ru.geekbrains.model.Road;
import ru.geekbrains.model.Tunnel;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Hello world!
 *
 */
public class MainClass
{
    private static final int CARS_COUNT = 4;
    public static void main(String[] args) {
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(CARS_COUNT);
        final CountDownLatch cdl = new CountDownLatch(CARS_COUNT);
        final CountDownLatch cdlLast = new CountDownLatch(CARS_COUNT);
        final Semaphore semaphore = new Semaphore(CARS_COUNT / 2);
        final AtomicBoolean winnerDefined = new AtomicBoolean(false);
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(semaphore), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cyclicBarrier, cdl, cdlLast, winnerDefined);
        }
        for (Car car : cars) {
            new Thread(car).start();
        }
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");
        cyclicBarrier.reset();
        try {
            cdlLast.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
    }
}
