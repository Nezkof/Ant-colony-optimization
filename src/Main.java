import java.util.Arrays;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    public static void main(String[] args) throws IOException {
        double[][] citiesTable = loadCitiesTable();
        double pheromoneTrailInitial = 1.0,
                explorationFactorAlpha = 2,
                explotationFactorBeta = 5,
                pheromoneEvaporationRate = 0.5,
                initialPheromoneByAnt = 500,
                antFitnessFactor = 300,
                randomnessFactor = 0.01;
        int maxIterations = 100,
                initialCity = 6,
                maxTries = 100;

        ACO algorithm = new ACO(explorationFactorAlpha,
                explotationFactorBeta,
                pheromoneEvaporationRate,
                initialPheromoneByAnt,
                antFitnessFactor,
                randomnessFactor,
                pheromoneTrailInitial,
                citiesTable,
                initialCity,
                maxTries,
                maxIterations);

        algorithm.optimize();

        System.out.println("Best tour order: " + Arrays.toString(algorithm.getOptimalPath()));
        System.out.println("Best tour length: " + algorithm.getOptimalTourLength());

        MapVisualisation map = new MapVisualisation(algorithm.getOptimalPath());
        map.showMap();
    }

    private static double[][] loadCitiesTable() throws IOException {
        Workbook wb = new XSSFWorkbook(new FileInputStream("E:\\KNU\\2Course\\2 Semester\\OOI\\Lab4\\CitiesInfo.xlsx"));
        double[][] citiesTable = new double[25][25];
        for (int i = 0; i < 25; ++i) {
            for (int j = 0; j < 25; ++j) {
                citiesTable[i][j] = wb.getSheetAt(0).getRow(i).getCell(j).getNumericCellValue();
            }
        }
        return citiesTable;
    }
}
