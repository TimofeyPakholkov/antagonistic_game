import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;

public class Main {
    public static void main(String[] args) {
        /*AntagonisticGame game = new AntagonisticGame(new int[][]{
                {9, 5, 6, 7},
                {1, 4, 3, 8},
                {6, 3, 2, -4}
        });*/
        /*AntagonisticGame game = new AntagonisticGame(new int[][]{
                {4, 3},
                {2, 4},
                {0, 5},
                {-1, 6}
        });*/
        /*AntagonisticGame game = new AntagonisticGame(new int[][]{
                {-2, 3, 1, 4},
                {4, 2, 3, 1}
        });*/
        /*System.out.println(game);
        game.simplifyMatrix();
        game.graphicSimplifyMatrix();
        System.out.println(game);*/

        AntagonisticGame game = new AntagonisticGame(new int[][]{
                {7, 2, 9},
                {2, 9, 0},
                {9, 0, 11}
        });
        brownRobinsonMethod(game, 0.1);
        System.out.println(game.getProbabilities());
    }

    public static void brownRobinsonMethod(AntagonisticGame game, double eps) {
        int counter = 0;

        double alpha = Integer.MAX_VALUE;
        double beta = Integer.MIN_VALUE;
        double prevValue = Integer.MAX_VALUE;

        int firstPlayerOptimalStrategy = 0;
        int secondPlayerOptimalStrategy = 0;

        double[] firstPlayerResultArray = new double[game.getMatrix()[0].length];
        double[] secondPlayerResultArray = new double[game.getMatrix().length];

        Map<Integer, Integer> firstPlayerStrategies = new HashMap<>();
        Map<Integer, Integer> secondPlayerStrategies = new HashMap<>();

        for (int i = 0; i < firstPlayerResultArray.length; i++) {
            firstPlayerResultArray[i] = 0;
            secondPlayerStrategies.put(i, 0);
        }
        for (int i = 0; i < secondPlayerResultArray.length; i++) {
            secondPlayerResultArray[i] = 0;
            firstPlayerStrategies.put(i, 0);
        }
        do {
            prevValue = (alpha + beta) / (2 * (counter + 1));
            alpha = Integer.MAX_VALUE;
            beta = Integer.MIN_VALUE;
            System.out.print((counter + 1) + " " + firstPlayerOptimalStrategy);
            for (int i = 0; i < game.getMatrix()[0].length; i++) {
                firstPlayerResultArray[i] += game.getMatrix()[firstPlayerOptimalStrategy][i];
                if (firstPlayerResultArray[i] < alpha) {
                    alpha = firstPlayerResultArray[i];
                    secondPlayerOptimalStrategy = i;
                }
                System.out.print(" " + firstPlayerResultArray[i]);
            }
            System.out.print(" " + secondPlayerOptimalStrategy);
            for (int i = 0; i < game.getMatrix().length; i++) {
                secondPlayerResultArray[i] += game.getMatrix()[i][secondPlayerOptimalStrategy];
                if (secondPlayerResultArray[i] > beta) {
                    beta = secondPlayerResultArray[i];
                    firstPlayerOptimalStrategy = i;
                }
                System.out.print(" " + secondPlayerResultArray[i]);
            }
            System.out.println(" " + alpha / (counter + 1) + " " + beta / (counter + 1) + " " + (alpha + beta) / (2 * (counter + 1)));
            firstPlayerStrategies.replace(firstPlayerOptimalStrategy, firstPlayerStrategies.get(firstPlayerOptimalStrategy) + 1);
            secondPlayerStrategies.replace(secondPlayerOptimalStrategy, secondPlayerStrategies.get(secondPlayerOptimalStrategy) + 1);
            counter++;
        } while (abs(prevValue - (alpha + beta) / (2 * (counter + 1))) > eps);
        for (int i = 0; i < game.getFirstPlayerProbabilities().length; i++) {
            game.setFirstPlayerProbabilitiesElement((double)firstPlayerStrategies.get(i) / counter, i);
        }
        for (int i = 0; i < game.getSecondPlayerProbabilities().length; i++) {
            game.setSecondPlayerProbabilitiesElement((double)secondPlayerStrategies.get(i) / counter, i);
        }
    }
}