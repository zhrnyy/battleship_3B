import java.util.List;
import java.util.Scanner;

class Battleship implements Game {
  private Player player1;
  private Player player2;
  private Player currentPlayer;
  private boolean gameOver;

  public Battleship(Player player1, Player player2) {
    this.player1 = player1;
    this.player2 = player2;
    currentPlayer = player1;
    gameOver = false;
  }

  @Override
  public void start() {
    System.out.println("Ayo Mulai Berperang");
    System.out.println("Player 1: " + player1.getName());
    System.out.println("Player 2: " + player2.getName());
    System.out.println("-----------------------------------");
    System.out.println("Memulai Game...");
    System.out.println("-----------------------------------");

    initializeShips(player1);
    initializeShips(player2);

    while (!gameOver) {
      if (player1.getShips().isEmpty()) {
        gameOver = true;
        System.out.println("Game over! " + player2.getName() + " Menjadi Pemenang!");
      } else if (player2.getShips().isEmpty()) {
        gameOver = true;
        System.out.println("Game over! " + player1.getName() + " Menjadi Pemenang!");
      } else {
        playTurn(currentPlayer);
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
      }
    }
  }

  private void initializeShips(Player player) {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Player " + player.getName() + ", Masukkan Koordinat Kapalmu.");
    for (int i = 0; i < 2; i++) {
      int x, y;
      do {
        System.out.print("Masukkan Koordinat x " + (i + 1) + ": ");
        x = scanner.nextInt();
        System.out.print("Masukkan Koordinat y " + (i + 1) + ": ");
        y = scanner.nextInt();

        if (x < 0 || x >= 7 || y < 0 || y >= 7) {
          System.out.println("Koordinat diluar batas. Masukkan koordinat antara 0-6.");
        } else {
          break;
        }
      } while (true);

      Ship ship = new ShipImpl(new Coordinate(x, y));
      player.addShip(ship);
    }
  }

  @Override
  public void playTurn(Player player) {
    Scanner scanner = new Scanner(System.in);
    System.out.println(player.getName() + ", Giliranmu.");

    boolean validCoordinates = false;
    Coordinate coordinate = null;

    while (!validCoordinates) {
      int x = getCoordinateInput(scanner, "x");
      int y = getCoordinateInput(scanner, "y");
      coordinate = new Coordinate(x, y);

      if (checkCoordinate(player1, coordinate) || checkCoordinate(player2, coordinate)) {
        System.out.println("Koordinat Sudah Pernah Digunakan. Masukkan koordinat baru.");
      } else {
        validCoordinates = true;
      }
    }

    handleShot(player, coordinate);
  }

  private int getCoordinateInput(Scanner scanner, String axis) {
    int coordinate;
    do {
      System.out.print("Masukkan Koordinat " + axis + ": ");
      coordinate = scanner.nextInt();

      if (coordinate < 0 || coordinate >= 7) {
        System.out.println("Koordinat diluar batas. Masukkan koordinat antara 0-6.");
      } else {
        break;
      }
    } while (true);

    return coordinate;
  }

  private boolean checkCoordinate(Player player, Coordinate coordinate) {
    for (Coordinate c : player.getShotsFired()) {
      if (c.getX() == coordinate.getX() && c.getY() == coordinate.getY()) {
        return true;
      }
    }
    return false;
  }

  private void handleShot(Player player, Coordinate coordinate) {
    player.addShotFired(coordinate);
    boolean hit = false;
    Player opponentPlayer = (player == player1) ? player2 : player1;

    clearScreen();

    for (Ship ship : opponentPlayer.getShips()) {
      if (ship.isHit(coordinate)) {
        hit = true;
        if (opponentPlayer.getShips().size() == 1) {
          opponentPlayer.getShips().remove(ship);
          System.out.println("Target Tenggelam!");
        } else {
          opponentPlayer.getShips().remove(ship);
          System.out.println("Target Berhasil Diserang!");
        }
        break;
      }
    }

    if (!hit) {
      for (Ship ship : player.getShips()) {
        if (ship.isHit(coordinate)) {
          hit = true;
          if (player.getShips().size() == 1) {
            player.getShips().remove(ship);
            System.out.println("Kapal Kita Tenggelam!");
          } else {
            player.getShips().remove(ship);
            System.out.println("Kapal Kita Diserang Sendiri!");
          }
          break;
        }
      }
    }

    if (!hit) {
      System.out.println("Kita Meleset!");
    }

    printKapal();
  }

  private void printKapal() {
    List<Coordinate> shotsPlayer1 = player1.getShotsFired();
    List<Coordinate> shotsPlayer2 = player2.getShotsFired();

    System.out
        .println(player1.getName() + " : " + player1.getShips().size() + "\t    " + player2.getName() + " : "
            + player2.getShips().size());
    System.out.println();

    System.out.print("  |");
    for (int i = 0; i < 7; i++) {
      System.out.print(" " + (1 + i) + " |");
    }
    System.out.println();

    for (int row = 0; row < 7; row++) {
      System.out.print((row + 1) + " |");
      for (int col = 0; col < 7; col++) {
        String coordinateHistory = getCoordinateSymbol(shotsPlayer1, shotsPlayer2, row, col);

        System.out.print(" " + coordinateHistory + " |");
      }

      System.out.println();
    }
  }

  private String getCoordinateSymbol(List<Coordinate> coordinates, List<Coordinate> coordinates2, int row, int col) {
    for (Coordinate coordinate : coordinates) {
      if (coordinate.getX() == col && coordinate.getY() == row) {
        return formatCoordinate("X", "RED");
      }
    }

    for (Coordinate coordinate : coordinates2) {
      if (coordinate.getX() == col && coordinate.getY() == row) {
        return formatCoordinate("X", "BLUE");
      }
    }

    return "-";
  }

  private String formatCoordinate(String coordinate, String color) {
    String ANSI_RESET = "\u001B[0m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_BLUE = "\u001B[34m";

    if (color.equals("RED")) {
      return ANSI_RED + coordinate + ANSI_RESET;
    } else if (color.equals("BLUE")) {
      return ANSI_BLUE + coordinate + ANSI_RESET;
    } else {
      return coordinate;
    }
  }

  @Override
  public boolean isGameOver() {
    return gameOver;
  }

  public static void clearScreen() {
    try {
      if (System.getProperty("os.name").contains("Windows")) {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } else {
        Runtime.getRuntime();
      }
    } catch (Exception e) {
      System.out.println("Error clearing the screen: " + e.getMessage());
    }
  }
}
