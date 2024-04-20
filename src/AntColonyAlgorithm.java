import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntColonyAlgorithm
{
    //ОСНОВНІ ПОЛЯ
    private final List<Ant> ants;
    private final double[][] graph;
    private final double[][] trails;
    private final double[] cityChoiceProbabilities;
    private final int numberOfCities;
    private final int numberOfAnts;
    private int currentCityIndex;
    private int[] bestPath;
    private double bestTourLength;

    //КОЕФІЦІЄНТИ ДЛЯ АЛГОРИТМУ
    private final double alpha;   //коеф. важливості феромонів для мурахи
    private final double beta;    //коеф. важливості відстані для мурахи
    private final double evaporation; //коеф. випаровування
    private final double pheromoneAmountByAnt;
    private final double trailsPheromoneAmount;
    private final double randomFactor; //коеф. випадкового вибору міста

    //ЗАГАЛЬНЕ
    private final int startCity;
    private final int triesNumber;
    private final int iterationsNumber;
    private final Random random;

    /* =============================
     КОНСТРУКТОР ТА СТАРТ АЛГОРИТМУ
    ============================= */

    public AntColonyAlgorithm(double alpha, double beta, double evaporation, double pheromoneAmountByAnt, double antFactor, double randomFactor, double trailsPheromoneAmount,
                              double[][] graph, int startCity, int triesNumber, int iterationsNumber)  {
        this.ants = new ArrayList<>();
        this.graph = graph;
        this.numberOfCities = graph.length;
        this.numberOfAnts = (int) (numberOfCities * antFactor);
        this.trails = new double[numberOfCities][numberOfCities];
        this.cityChoiceProbabilities = new double[numberOfCities];
        for(int i = 0; i < numberOfAnts; i++)
            ants.add(new Ant(numberOfCities));

        this.alpha = alpha;
        this.beta = beta;
        this.evaporation = evaporation;
        this.pheromoneAmountByAnt = pheromoneAmountByAnt;
        this.trailsPheromoneAmount = trailsPheromoneAmount;
        this.randomFactor = randomFactor;

        this.startCity = startCity;
        this.triesNumber = triesNumber;
        this.iterationsNumber = iterationsNumber;
        this.random = new Random();
    }

    public void startOptimization() {
        for(int i = 1; i <= triesNumber; i++) {
            setupAnts();
            clearTrails();
            for(int j = 0; j < iterationsNumber; j++) {
                moveAnts();
                updateTrails();
                updateBest();
            }
            System.out.println("Tour length #" + i + ": " + bestTourLength);
        }
    }

    private void setupAnts() {
        for(int i = 0; i < numberOfAnts; i++) {
            for(Ant ant : ants) {
                ant.clear();
                ant.visitCity(-1, startCity);
            }
        }
        currentCityIndex = 0;
    }

    /* =============================
           РУХ МУРАХ ПО МІСТАМ
    ============================= */
    private void moveAnts() {
        for(int i = currentCityIndex; i < numberOfCities-1; i++) {
            for(Ant ant : ants) {
                ant.visitCity(currentCityIndex,selectNextCity(ant));
            }
            currentCityIndex++;
        }
    }

    private int selectNextCity(Ant ant) {
        if (random.nextDouble() < randomFactor) //випадково вирішуємо чи буде місто обране рандомайзером
            for (int i = 0; i < numberOfCities; i++)
                if (!ant.visited(i))
                    return i;

        calculateProbabilities(ant); //розрахунок вибору кожного міста
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0;

        for (int i = 0; i < numberOfCities; i++) {
            cumulativeProbability += cityChoiceProbabilities[i]; //обраховуємо кумулятивнуймовірність вибору решти міст
            if (cumulativeProbability >= randomValue) //якщо вона більше за випадкове число, то обираємо місто
                return i;
        }

        throw new RuntimeException("There are no other cities");
    }

    public void calculateProbabilities(Ant ant) {
        int i = ant.trail[currentCityIndex];
        double pheromone = 0.0;

        for (int l = 0; l < numberOfCities; l++) {
            if (!ant.visited(l))
                pheromone += Math.pow(trails[i][l], alpha) * Math.pow(1.0 / graph[i][l], beta);
        }

        for (int j = 0; j < numberOfCities; j++) { //для кожного міста розрахувуємо шанс вибору
            if (ant.visited(j))
                cityChoiceProbabilities[j] = 0.0;
            else {
                double numerator = Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graph[i][j], beta);
                cityChoiceProbabilities[j] = numerator / pheromone;
            }
        }
    }

    /* =============================
      ОНОВЛЕННЯ ФЕРОМОНІВ НА РЕБРАХ
    ============================= */
    private void updateTrails() {
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++)
                trails[i][j] *= evaporation;
        }
        // Оновлення феромонів на шляхах, пройдених мурахами
        for (Ant a : ants) {
            double contribution = pheromoneAmountByAnt / a.trailLength(graph); // Розрахунок внеску кожної мурахи в рівень феромонів на її шляху
            for (int i = 0; i < numberOfCities - 1; i++) // Оновлення феромонів на кожному зв'язку на шляху мурахи
                trails[a.trail[i]][a.trail[i + 1]] += contribution;
            trails[a.trail[numberOfCities - 1]][a.trail[0]] += contribution; // Оновлення феромонів на останньому зв'язку
        }
    }

    /* =============================
       ОНОВЛЕННЯ НАЙКРАЩОГО ШЛЯХУ
    ============================= */
    private void updateBest() {
        if (bestPath == null) {
            bestPath = ants.get(0).trail;
            bestTourLength = ants.get(0).trailLength(graph);
        }

        for (Ant a : ants) {
            if (a.trailLength(graph) < bestTourLength) {
                bestTourLength = a.trailLength(graph);
                bestPath = a.trail.clone();
            }
        }
    }

    /* =============================
              ОЧИСТКА РЕБЕР
    ============================= */
    private void clearTrails() {
        for(int i = 0; i < numberOfCities; i++)
            for(int j=0; j < numberOfCities; j++)
                trails[i][j] = trailsPheromoneAmount;
    }

    /* =============================
           ГЕТТЕРИ ТА СЕТТЕРИ
    ============================= */
    int[] getBestPath (){ return bestPath; }
    double getBestTourLength() { return bestTourLength;}
}