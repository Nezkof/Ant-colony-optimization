import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ACO {
    // Основні поля
    private final List<Ant> ants;
    private final double[][] adjMatrix;
    private final double[][] pheromoneMatrix;
    private final double[] cityProbabilities;
    private final int numCities;
    private final int numAnts;
    private int currentIndex;
    private int[] optimalPath;
    private double optimalTourLength;

    // Коефіцієнти для алгоритму
    private final double importancePheromone;
    private final double importanceDistance;
    private final double evaporationRate;
    private final double pheromoneDepositionRate;
    private final double initialPheromoneLevel;
    private final double randomnessFactor;

    // Загальні
    private final int initialCity;
    private final int maxAttempts;
    private final int maxIterations;
    private final Random random;

    // Конструктор та старт алгоритму
    public ACO(double importancePheromone, double importanceDistance, double evaporationRate, double pheromoneDepositionRate,
               double antFactor, double randomnessFactor, double initialPheromoneLevel, double[][] adjMatrix, int initialCity,
               int maxAttempts, int maxIterations) {
        this.ants = new ArrayList<>();
        this.adjMatrix = adjMatrix;
        this.numCities = adjMatrix.length;
        this.numAnts = (int) (numCities * antFactor);
        this.pheromoneMatrix = new double[numCities][numCities];
        this.cityProbabilities = new double[numCities];
        for (int i = 0; i < numAnts; i++)
            ants.add(new Ant(numCities));

        this.importancePheromone = importancePheromone;
        this.importanceDistance = importanceDistance;
        this.evaporationRate = evaporationRate;
        this.pheromoneDepositionRate = pheromoneDepositionRate;
        this.initialPheromoneLevel = initialPheromoneLevel;
        this.randomnessFactor = randomnessFactor;

        this.initialCity = initialCity;
        this.maxAttempts = maxAttempts;
        this.maxIterations = maxIterations;
        this.random = new Random();
    }

    // Початок оптимізації
    public void optimize() {
        for (int i = 1; i <= maxAttempts; i++) {
            prepareAnts();
            resetPheromone();
            for (int j = 0; j < maxIterations; j++) {
                moveAnts();
                updatePheromone();
                updateOptimal();
            }
            System.out.println("Optimal tour length #" + i + ": " + optimalTourLength);
        }
    }

    // Налаштування мурах
    private void prepareAnts() {
        for (int i = 0; i < numAnts; i++) {
            for (Ant ant : ants) {
                ant.clearExploredCities();
                ant.exploreCity(-1, initialCity);
            }
        }
        currentIndex = 0;
    }

    // Рух мурах по містах
    private void moveAnts() {
        for (int i = currentIndex; i < numCities - 1; i++) {
            for (Ant ant : ants) {
                ant.exploreCity(currentIndex, selectNextCity(ant));
            }
            currentIndex++;
        }
    }

    // Вибір наступного міста для мурахи
    private int selectNextCity(Ant ant) {
        if (random.nextDouble() < randomnessFactor)
            for (int i = 0; i < numCities; i++)
                if (!ant.hasExplored(i))
                    return i;

        calculateProbabilities(ant);
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0;

        for (int i = 0; i < numCities; i++) {
            cumulativeProbability += cityProbabilities[i];
            if (cumulativeProbability >= randomValue)
                return i;
        }

        throw new RuntimeException("There are no other cities");
    }

    // Розрахунок ймовірностей вибору міста для мурахи
    public void calculateProbabilities(Ant ant) {
        int i = ant.path[currentIndex];
        double totalWeight = 0.0;

        for (int l = 0; l < numCities; l++) {
            if (!ant.hasExplored(l))
                totalWeight += Math.pow(pheromoneMatrix[i][l], importancePheromone) * Math.pow(1.0 / adjMatrix[i][l], importanceDistance);
        }

        for (int j = 0; j < numCities; j++) {
            if (ant.hasExplored(j))
                cityProbabilities[j] = 0.0;
            else {
                double numerator = Math.pow(pheromoneMatrix[i][j], importancePheromone) * Math.pow(1.0 / adjMatrix[i][j], importanceDistance);
                cityProbabilities[j] = numerator / totalWeight;
            }
        }
    }

    // Оновлення феромонів на ребрах
    private void updatePheromone() {
        for (int i = 0; i < numCities; i++) {
            for (int j = 0; j < numCities; j++)
                pheromoneMatrix[i][j] *= evaporationRate;
        }

        for (Ant a : ants) {
            double contribution = pheromoneDepositionRate / a.calculatePathLength(adjMatrix);
            for (int i = 0; i < numCities - 1; i++)
                pheromoneMatrix[a.path[i]][a.path[i + 1]] += contribution;
            pheromoneMatrix[a.path[numCities - 1]][a.path[0]] += contribution;
        }
    }

    // Оновлення найкращого шляху
    private void updateOptimal() {
        if (optimalPath == null) {
            optimalPath = ants.get(0).path;
            optimalTourLength = ants.get(0).calculatePathLength(adjMatrix);
        }

        for (Ant a : ants) {
            if (a.calculatePathLength(adjMatrix) < optimalTourLength) {
                optimalTourLength = a.calculatePathLength(adjMatrix);
                optimalPath = a.path.clone();
            }
        }
    }

    // Очищення ребер
    private void resetPheromone() {
        for (int i = 0; i < numCities; i++)
            for (int j = 0; j < numCities; j++)
                pheromoneMatrix[i][j] = initialPheromoneLevel;
    }

    // Геттери та сеттери
    int[] getOptimalPath() {
        return optimalPath;
    }

    double getOptimalTourLength() {
        return optimalTourLength;
    }
}
