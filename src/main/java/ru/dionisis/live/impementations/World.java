package ru.dionisis.live.impementations;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * реализация игры "Жизнь"
 * конфигурируется текстовым файлом. В файле должна быть одинаковая длинна всех строк,
 * каждый символ представляет одну сущность ('1' живая клетка, либой другой символ - мертвая).
 * следующее поколение определяется следующими правилами
 * - если у мертвой/пустой клетки есть ровно 3 живых соседа, то она становится живой
 * - если у живой клетки меншь двух соседей либо больше трех, то она погибает
 */
public class World {

    private int height;
    private int width;
    private int length;
    /**
     * хранит состояние (живой/неживой) всех клеток
     */
    private byte[] state;
    /**
     * буферная переменная в которой формируется ново поле
     */
    private byte[] newState;
    /**
     * хранит индексы соседей, первое измерение - индекс интересующей клетки,
     * второе - индекс "клетки-соседа" (по 8 для каждой клетки)
     */
    private int[][] neighborsID;

    World(File configFile) {
        List<String> config = Config.readConfig(configFile);
        this.height = config.size();
        this.width = config.get(0).length();
        this.length = height * width;
        this.state = new byte[length];
        this.neighborsID = new int[length][8];
        initField(config);
    }

    private void initField(List<String> config) {
        int index = 0;
        for (int lineID = 0; lineID < height; lineID++) {
            for (int columnID = 0; columnID < width; columnID++) {
                state[index] = (byte) (config.get(lineID).charAt(columnID) == '1' ? 1 : 0);
                initNeighborsID(index, lineID, columnID);
                index++;
            }
        }
    }

    /**
     * заполняет массив neighborsID индесами клеток-соседей для каждой клетки
     *
     * @param index    индекс клетки в массиве state
     * @param lineID   индекс строки в конфигурационном файде
     * @param columnID индекс столбца в конфигурационном файле
     */
    private void initNeighborsID(int index, int lineID, int columnID) {
        neighborsID[index][0] = height * lineID + getPrevColumnID(columnID);
        neighborsID[index][1] = height * lineID + getNexColumnID(columnID);
        neighborsID[index][2] = height * (getNexLineID(lineID)) + columnID;
        neighborsID[index][3] = height * (getNexLineID(lineID)) + getNexColumnID(columnID);
        neighborsID[index][4] = height * (getNexLineID(lineID)) + getPrevColumnID(columnID);
        neighborsID[index][5] = height * (getPrevLineID(lineID)) + columnID;
        neighborsID[index][6] = height * (getPrevLineID(lineID)) + getNexColumnID(columnID);
        neighborsID[index][7] = height * (getPrevLineID(lineID)) + getPrevColumnID(columnID);
    }

    private int getPrevLineID(int id) {
        return (--id == -1) ? (width - 1) : id;
    }

    private int getNexLineID(int id) {
        return ++id == width ? 0 : id;
    }

    private int getNexColumnID(int id) {
        return ++id == height ? 0 : id;
    }

    private int getPrevColumnID(int id) {
        return (--id == -1) ? (height - 1) : id;
    }


    /**
     * пересчитавет состояние поля в одном потоке
     * @param steps количество интераций пересчета поля
     */
    public void executeGeneration(int steps) {
        for (int i = 0; i < steps; i++) {
            newState = new byte[length];
            for (int index = 0; index < length; index++) {
                executeCell(index);
            }
            state = newState;
        }
    }

    /**
     * пересчитывает состояние поля в нескольких потоках
     *
     * @param steps количество интераций пересчета поля
     */
    public void executeGeneration(int steps, int quantityThreads) {
        for (int i = 0; i < steps; i++) {
            newState = new byte[length];
            ArrayList<Thread> threads = new ArrayList<>(quantityThreads);
            for (int threadID = 0; threadID < quantityThreads; threadID++) {
                int firstID = threadID;
                threads.add(new Thread(() -> {
                    for (int index = firstID; index < length; index += quantityThreads) {
                        executeCell(index);
                    }
                }));
            }
            starAndJoinThreads(threads);
            state = newState;
        }
    }

    private void starAndJoinThreads(ArrayList<Thread> threads) {
        threads.forEach(Thread::start);
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {

        }
    }

    private void executeCell(int index) {
        int quantityAliveNeighbors = executeAliveNeighbors(index);
        if (state[index] == 1) {
            if (quantityAliveNeighbors == 2 || quantityAliveNeighbors == 3) {
                newState[index] = 1;
            }
        } else {
            if (quantityAliveNeighbors == 3) {
                newState[index] = 1;
            }
        }
    }

    private int executeAliveNeighbors(int index) {
        return state[neighborsID[index][0]]
                + state[neighborsID[index][1]]
                + state[neighborsID[index][2]]
                + state[neighborsID[index][3]]
                + state[neighborsID[index][4]]
                + state[neighborsID[index][5]]
                + state[neighborsID[index][6]]
                + state[neighborsID[index][7]];
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int lineID = 0; lineID < height; lineID++) {
            for (int columnID = 0; columnID < width; columnID++) {
                stringBuilder.append(state[lineID * width + columnID]).append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
