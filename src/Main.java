import java.util.Scanner;
import java.util.Random;

public class Main {
	
	public Main() {
		
	}
	
	private static final Scanner sc = new Scanner(System.in);
	private static final int POPULATION_SIZE = 25;
	private static final int CHECK_N = 8;
	private static final int RADIUS = 500;
	private static final int NUM_OF_STEPS = 200;
	private static final int MAX_STEPS = 50000;
	private static final int MUTATION_RATE = 9;
	private static final int NUMBER_MUTATED = 9;
	private static final Random rnd = new Random();
	
	public static void main(String[] args) {
		//Популяция решений
		Route[] population;
		//Массив всех точек		
		Point[] allPoints;
		//матрица инциденций
		double[][] incidencyMatrix;
		//Количество точек
		int n;
		//Команда
		String cmd = "";
		//Периметр фигуры (для чек)
		double perimeter = Double.POSITIVE_INFINITY;
		
		System.out.println("Welcome\nPlease enter the command:\n\ncheck - test algorithm with predetermined options;\nexit - quit the app;\nAnything else - specify algorithm parameters.\n");
		cmd = sc.next();
		/*************TEST CIRCLE INPUT*************/
		if (cmd.equals("check")) {
			n = CHECK_N;
			allPoints = new Point[n];
			incidencyMatrix = new double[n][n];
			
			System.out.println("\nPoints coordinates:");
			
			double stepCircle = (Math.PI * 2 / CHECK_N);
			for (int i = 0; i < CHECK_N; i++) {
				allPoints[i] = new Point(i);
				allPoints[i].setX(RADIUS * Math.cos(i * stepCircle));
				allPoints[i].setY(RADIUS * Math.sin(i * stepCircle));
				
				System.out.println("x" + i + ":  " + String.format("%.2f", allPoints[i].getX()) + "  " + "y" + i + ":  " + String.format("%.2f", allPoints[i].getY()));
			}
			//Вычисление длины периметра многоуголника
			perimeter = Math.sin(Math.PI / CHECK_N) * 2 * RADIUS * CHECK_N;
			System.out.println("Perimeter: " + String.format("%.2f", perimeter));
			
		}
		/*************MANUAL INPUT*************/
		else {
			/*If exit is needed immediately*/
			if (cmd.equals("exit")){
				System.exit(0);
			}
			/*if exitting is not needed*/
			System.out.println("\nPlease enter the number of points:");
			n = sc.nextInt();
			allPoints = new Point[n];
			incidencyMatrix = new double[n][n];
			
			for (int i = 0; i < n; i++) {
				allPoints[i] = new Point(i);
				System.out.println("x" + (i + 1) + ":");
				allPoints[i].setX(sc.nextInt());
				System.out.println("y" + (i + 1) + ":");
				allPoints[i].setY(sc.nextInt());
			}
		}
		
		// Тело
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				incidencyMatrix[i][j] = getDistance(allPoints[i], allPoints[j]);
			}
		}
		
		//Рассчет и вывод Матрицы инциденций
		System.out.println("\nIncidency matrix:");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print(String.format("%.2f", incidencyMatrix[i][j]) + "   ");
				if (j == n - 1 ) System.out.println();
			}
		}
		
		//**Генетический алгоритм**
		
		//Составление исходной популяции
		population = new Route[POPULATION_SIZE];
		for (int i = 0; i < population.length; i++) {
			population[i] = new Route();
		}
		
		for (int i = 0; i < POPULATION_SIZE; i++) {
			//массив - маска для создания рандомного набора
			boolean[] mask = new boolean[n];
			for (boolean m : mask) {
				m = false;
			}
			//создание нового массива точек в массиве популяции
			population[i].initEmptyRoute(n);
			for (int j = 0; j < n; j++) {
				int pt = rnd.nextInt(n);
				while (mask[pt]) {
					pt = rnd.nextInt(n);
				}
				population[i].setPointInRoute(j, allPoints[pt]);
				mask[pt] = true;
			}
			population[i].closeRoute();
		}
		
		//Подсчет фитнесс-функции
		for (Route r : population) {
			r.setFitness();
		}
				
		//Вывод исходной популяции на экран
		System.out.println("\nInitial population (Step #0):");
		for (int i = 0; i < POPULATION_SIZE; i++) {
			population[i].printSelf();
		}
		
		//Сортировка (тест)
		quickSortRoutes(population, 0, POPULATION_SIZE - 1);
		System.out.println("\nSorted population:");
		for (int i = 0; i < POPULATION_SIZE; i++) {
			population[i].printSelf();
		}
		
		//Шаг алгоритма
		//*******WHILE*******////////////
		int x = 1;
		while (population[0].getFitness() > perimeter + 1) {
			//шаг алгоритма
			if (x >= MAX_STEPS) break;
			geneticStep(population);
			//мутации
			for (int j = 0; j < NUMBER_MUTATED; j++) {
				if (x % MUTATION_RATE == 0) mutate(population[rnd.nextInt(POPULATION_SIZE)]);
			}
			//вывод на экран
			System.out.println("\nStep #" + x + ":");
			for (int j = 0; j < POPULATION_SIZE; j++) {
				population[j].printSelf();
			}
			x++;
		}
		
		//*******FOR**********//////////////
		/*
		for (int i = 1; i <= NUM_OF_STEPS; i++) {
			//шаг алгоритма
			geneticStep(population);
			//мутации
			for (int j = 0; j < NUMBER_MUTATED; j++) {
				if (i % MUTATION_RATE == 0) mutate(population[rnd.nextInt(POPULATION_SIZE)]);
			}
			//вывод на экран
			System.out.println("\nStep #" + i + ":");
			for (int j = 0; j < POPULATION_SIZE; j++) {
				population[j].printSelf();
			}
		}
		*/
	}
	
	public static double getDistance(Point pt1, Point pt2) {
		return Math.sqrt( Math.pow( pt1.getX() - pt2.getX(), 2 ) + Math.pow( pt1.getY() - pt2.getY(), 2 ));	
	}
	
	public static void quickSortRoutes(Route[] array, int low, int high) {
		if (low < high) {
			//int pivind = rnd.nextInt(high); <-- Через выбор рандомного элемента
			int pivind = high;  		// <-- Через выбор максимального элемента
			Route pivot = array[pivind];
			int i = low - 1;
			for (int j = low; j < high; j++) {
				if (array[j].getFitness() <= pivot.getFitness()) {
					i++;
					Route buffer = array[i];
					array[i] = array[j];
					array[j] = buffer;
				}
			}
			Route buffer = array[i + 1];
			array[i + 1] = array[pivind];
			array[pivind] = buffer;
			
			quickSortRoutes(array, low, i); //array, low, (i + 1) - 1
			quickSortRoutes(array, i + 2, high); //array, (i + 1) + 1, high
		}
	}
	
	public static void geneticStep(Route[] array) {
		//Сортировка от меньшей к большей длине маршрута
		quickSortRoutes(array, 0, POPULATION_SIZE - 1);
		
		int separateDead = (int)(POPULATION_SIZE / 3); //<-- отметка на 1/3 популяции маршрутов
		int separateHalf = (int)( array[0].getLength() / 2);//<-- отметка на половине от количества смысловых точек в маршруте
		
		for (int i = separateDead; i < POPULATION_SIZE; i++ ) {
			//Маски по массивам
			boolean[] maskHead = new boolean[array[i].getLength() - separateHalf];
			for (boolean m : maskHead) {
				m = false;
			}
			boolean[] maskTail = new boolean[separateHalf];
			for (boolean m : maskTail) {
				m = false;
			}
			//Убить непригодных 
			array[i] = new Route();
			array[i].initEmptyRoute(array[0].getLength());
			if (i < 2 * separateDead) {  
				//создание маски повторов
				boolean[] mask = new boolean[array[i].getLength() - separateHalf];
				for (boolean m : mask) {
					m = false;
				}
				//перенос точек
				for (int j = 0; j < separateHalf; j++) {   // < -- Скопировать головы пригодных в новых
					array[i].setPointInRoute(j, array[i - separateDead].getPoint(j));
				}
				for (int j = separateHalf; j < array[i].getLength(); j++) { // < -- Рандомные хвосты новых
					int pt = separateHalf + rnd.nextInt(array[i].getLength() - separateHalf);
					while (mask[pt - separateHalf]) {
						pt = separateHalf + rnd.nextInt(array[i].getLength() - separateHalf);
					}
					array[i].setPointInRoute(j, array[i - separateDead].getPoint(pt));
					mask[pt - separateHalf] = true;
				}
			}
			else {
				//создание маски повторов
				boolean[] mask = new boolean[separateHalf];
				for (boolean m : mask) {
					m = false;
				}
				//перенос точек				
				for (int j = 0; j < separateHalf; j++) {   // < -- Рандомные головы новых
					int pt = rnd.nextInt(separateHalf);
					while (mask[pt]) {
						pt = rnd.nextInt(separateHalf);
					}
					array[i].setPointInRoute(j, array[i - 2 * separateDead].getPoint(pt));
					mask[pt] = true;
				}
				for (int j = separateHalf; j < array[i].getLength(); j++) {  // < -- Скопировать хвосты пригодных в новых
					array[i].setPointInRoute(j, array[i - 2 * separateDead].getPoint(j));
				}
			}
			array[i].closeRoute();
			array[i].setFitness();
		}
	}
	
	public static void mutate(Route route) {
		int geneInd1 = rnd.nextInt(route.getLength());
		int geneInd2 = rnd.nextInt(route.getLength());
		
		Point buffer = route.getPoint(geneInd1);
		route.setPointInRoute(geneInd1, route.getPoint(geneInd2));
		route.setPointInRoute(geneInd2, buffer);
	}
}