import java.util.Arrays;

public class AntagonisticGame {
    private int[][] matrix;
    private double[] firstPlayerProbabilities;
    private double[] secondPlayerProbabilities;


    public AntagonisticGame(int[][] matrix) {
        this.matrix = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
        firstPlayerProbabilities = new double[matrix.length];
        Arrays.fill(firstPlayerProbabilities, 1);
        secondPlayerProbabilities = new double[matrix[0].length];
        Arrays.fill(secondPlayerProbabilities, 1);
    }

    public boolean checkForSaddlePoint() {
        int selectedLine = 0;
        int miniMax = Integer.MIN_VALUE;
        int minValue;
        for (int i = 0; i < matrix.length; i++) {
            minValue = Integer.MAX_VALUE;
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] < minValue) {
                    minValue = matrix[i][j];
                }
            }
            if (minValue > miniMax) {
                miniMax = minValue;
                selectedLine = i;
            }
        }

        int selectedColumn = 0;
        int maxiMin = Integer.MAX_VALUE;
        int maxValue;
        for (int j = 0; j < matrix[0].length; j++) {
            maxValue = Integer.MIN_VALUE;
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i][j] > maxValue) {
                    maxValue = matrix[i][j];
                }
            }
            if (maxValue < maxiMin) {
                maxiMin = maxValue;
                selectedColumn = j;
            }
        }
        if (maxiMin == miniMax) {
            for (int i = 0; i < firstPlayerProbabilities.length; i++) {
                if (i != selectedLine) firstPlayerProbabilities[i] = 0;
            }
            for (int i = 0; i < secondPlayerProbabilities.length; i++) {
                if (i != selectedColumn) secondPlayerProbabilities[i] = 0;
            }
            return true;
        }
        return false;
    }

    public void simplifyMatrix() {
        boolean isChanged = false;
        int[] lineSums = new int[matrix.length];
        int[] columnSums = new int[matrix[0].length];
        outer: for (int i = 0; i < matrix.length; i++) {
            if (firstPlayerProbabilities[i] == 0) continue outer;
            inner: for (int j = 0; j < matrix[0].length; j++) {
                if (secondPlayerProbabilities[j] == 0) continue inner;
                lineSums[i] += matrix[i][j];
                columnSums[j] += matrix[i][j];
            }
        }

        outer: for (int i = 0; i < matrix.length; i++) {
            if (firstPlayerProbabilities[i] == 0) continue outer;
            middle: for (int l = 0; l < matrix.length; l++) {
                if (!(firstPlayerProbabilities[l] != 0 && l != i && lineSums[l] <= lineSums[i])) continue middle;
                inner: for (int j = 0; j < matrix[0].length; j++) {
                    if (secondPlayerProbabilities[j] == 0) continue inner;
                    if (matrix[l][j] > matrix[i][j]) continue middle;
                }
                isChanged = true;
                firstPlayerProbabilities[l] = 0;
            }
        }
        outer: for (int j = 0; j < matrix[0].length; j++) {
            if (secondPlayerProbabilities[j] == 0) continue outer;
            middle: for (int c = 0; c < matrix[0].length; c++) {
                if (!(secondPlayerProbabilities[c] != 0 && c != j && columnSums[c] >= columnSums[j])) continue middle;
                inner: for (int i = 0; i < matrix.length; i++) {
                    if (firstPlayerProbabilities[i] == 0) continue inner;
                    if (matrix[i][c] < matrix[i][j]) continue middle;
                }
                isChanged = true;
                secondPlayerProbabilities[c] = 0;
            }
        }
        if (isChanged) simplifyMatrix();
    }

    public void graphicSimplifyMatrix() {
        int activeLineCounter = 0;
        int activeColumnCounter = 0;
        for (int i = 0; i < firstPlayerProbabilities.length; i++) {
            if (firstPlayerProbabilities[i] == 1) {
                activeLineCounter++;
            }
        }
        for (int i = 0; i < secondPlayerProbabilities.length; i++) {
            if (secondPlayerProbabilities[i] == 1) {
                activeColumnCounter++;
            }
        }
        if ((activeLineCounter != 2 && activeColumnCounter != 2) ||
                (activeLineCounter == 2 && activeColumnCounter == 2) ||
                (activeLineCounter < 2) || (activeColumnCounter < 2)) return;
        if (activeLineCounter == 2) {
            int[] selectedLines = {-1, -1};
            for (int i = 0; i < firstPlayerProbabilities.length; i++) {
                if (firstPlayerProbabilities[i] != 0) {
                    selectedLines[0] = i;
                }
            }
            for (int i = firstPlayerProbabilities.length - 1; i >= 0; i--) {
                if (firstPlayerProbabilities[i] != 0) {
                    selectedLines[1] = i;
                }
            }
            int[] selectedColumns = {-1, -1};
            int minFirstValue = Integer.MAX_VALUE;
            int minSecondValue = Integer.MAX_VALUE;
            for (int j = 0; j < matrix[0].length; j++) {
                if (secondPlayerProbabilities[j] == 0) continue;
                if (matrix[selectedLines[1]][j] < minSecondValue) {
                    minSecondValue = matrix[selectedLines[1]][j];
                    minFirstValue = matrix[selectedLines[0]][j];
                    selectedColumns[0] = j;
                } else if (matrix[selectedLines[1]][j] == minSecondValue) {
                    if (matrix[selectedLines[0]][j] < minFirstValue) {
                        minSecondValue = matrix[selectedLines[1]][j];
                        minFirstValue = matrix[selectedLines[0]][j];
                        selectedColumns[0] = j;
                    }
                }
            }
            minFirstValue = Integer.MAX_VALUE;
            minSecondValue = Integer.MAX_VALUE;
            for (int j = 0; j < matrix[0].length; j++) {
                if (secondPlayerProbabilities[j] == 0) continue;
                if (matrix[selectedLines[0]][j] < minFirstValue) {
                    minSecondValue = matrix[selectedLines[1]][j];
                    minFirstValue = matrix[selectedLines[0]][j];
                    selectedColumns[1] = j;
                } else if (matrix[selectedLines[0]][j] == minFirstValue) {
                    if (matrix[selectedLines[1]][j] < minSecondValue) {
                        minSecondValue = matrix[selectedLines[1]][j];
                        minFirstValue = matrix[selectedLines[0]][j];
                        selectedColumns[1] = j;
                    }
                }
            }
            for (int i = 0; i < secondPlayerProbabilities.length; i++) {
                if (!(i == selectedColumns[0] || i == selectedColumns[1])) secondPlayerProbabilities[i] = 0;
            }
        } else {
            int[] selectedColumns = {-1, -1};
            for (int i = 0; i < secondPlayerProbabilities.length; i++) {
                if (secondPlayerProbabilities[i] != 0) {
                    selectedColumns[0] = i;
                }
            }
            for (int i = secondPlayerProbabilities.length - 1; i >= 0; i--) {
                if (secondPlayerProbabilities[i] != 0) {
                    selectedColumns[1] = i;
                }
            }
            int[] selectedLines = {-1, -1};
            int maxFirstValue = Integer.MIN_VALUE;
            int maxSecondValue = Integer.MIN_VALUE;
            for (int j = 0; j < matrix.length; j++) {
                if (firstPlayerProbabilities[j] == 0) continue;
                if (matrix[j][selectedColumns[1]] > maxSecondValue) {
                    maxSecondValue = matrix[j][selectedColumns[1]];
                    maxFirstValue = matrix[j][selectedColumns[0]];
                    selectedLines[0] = j;
                } else if (matrix[j][selectedColumns[1]] == maxSecondValue) {
                    if (matrix[j][selectedColumns[0]] > maxFirstValue) {
                        maxSecondValue = matrix[j][selectedColumns[1]];
                        maxFirstValue = matrix[j][selectedColumns[0]];
                        selectedLines[0] = j;
                    }
                }
            }
            maxFirstValue = Integer.MIN_VALUE;
            maxSecondValue = Integer.MIN_VALUE;
            for (int j = 0; j < matrix.length; j++) {
                if (firstPlayerProbabilities[j] == 0) continue;
                if (matrix[j][selectedColumns[0]] > maxFirstValue) {
                    maxSecondValue = matrix[j][selectedColumns[1]];
                    maxFirstValue = matrix[j][selectedColumns[0]];
                    selectedLines[1] = j;
                } else if (matrix[j][selectedColumns[0]] == maxFirstValue) {
                    if (matrix[j][selectedColumns[1]] > maxSecondValue) {
                        maxSecondValue = matrix[j][selectedColumns[1]];
                        maxFirstValue = matrix[j][selectedColumns[0]];
                        selectedLines[1] = j;
                    }
                }
            }
            for (int i = 0; i < firstPlayerProbabilities.length; i++) {
                if (!(i == selectedLines[0] || i == selectedLines[1])) firstPlayerProbabilities[i] = 0;
            }
        }
    }

    @Override
    public String toString() {
        String result = "\t";
        for (int i = 0; i < matrix[0].length; i++) {
            if (secondPlayerProbabilities[i] != 0) {
                result += "B" + (i + 1) + "\t";
            }
        }
        result += "\n";
        for (int i = 0; i < matrix.length; i++) {
            if (firstPlayerProbabilities[i] != 0) {
                result += "A" + (i + 1) + "\t";
                for (int j = 0; j < matrix[0].length; j++) {
                    if (secondPlayerProbabilities[j] != 0) {
                        result += matrix[i][j] + "\t";
                    }
                }
                result += "\n";
            }
        }
        return result;
    }

    public double[] getFirstPlayerProbabilities() {
        return firstPlayerProbabilities;
    }

    public double[] getSecondPlayerProbabilities() {
        return secondPlayerProbabilities;
    }

    public void setFirstPlayerProbabilitiesElement(double value, int index) {
        firstPlayerProbabilities[index] = value;
    }

    public void setSecondPlayerProbabilitiesElement(double value, int index) {
        secondPlayerProbabilities[index] = value;
    }

    public String getProbabilities() {
        String result = "{ ";
        for (int i = 0; i < firstPlayerProbabilities.length; i++) {
            result += firstPlayerProbabilities[i] + " ";
        }
        result += "}\n{ ";
        for (int i = 0; i < secondPlayerProbabilities.length; i++) {
            result += secondPlayerProbabilities[i] + " ";
        }
        result += "}";
        return result;
    }

    public int[][] getMatrix() {
        return matrix;
    }
}