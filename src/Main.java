import java.util.Arrays;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    public static void main(String[] args) throws IOException {

        double trailsPheromoneAmount = 1.0;
        double alpha = 2; //2
        double beta = 5; //5
        double evaporation = 0.5;
        double pheromoneAmountByAnt = 500;
        double antFactor = 300;
        double randomFactor = 0.01;
        int iterationsNumber = 10;
        int startCity = 6;
        int triesNumber = 10;

        double[][] citiesTable = new double[25][25];

        Workbook wb = new XSSFWorkbook(new FileInputStream("E:\\KNU\\2Course\\2 Semester\\OOI\\Lab4\\CitiesInfo.xlsx"));

        for (int i = 0; i < 25; ++i){
            for (int j = 0; j < 25; ++j){
                citiesTable[i][j] = wb.getSheetAt(0).getRow(i).getCell(j).getNumericCellValue();
            }
        }

        AntColonyAlgorithm algorithm = new AntColonyAlgorithm(alpha, beta, evaporation, pheromoneAmountByAnt, antFactor,randomFactor, trailsPheromoneAmount, citiesTable, startCity, triesNumber, iterationsNumber);

        algorithm.startOptimization();
        System.out.println("Best tour order: " + Arrays.toString(algorithm.getBestPath()));
        System.out.println("Best tour length: " + algorithm.getBestTourLength());

        MapVisualisation map = new MapVisualisation(algorithm.getBestPath());
        map.showMap();
    }
}
