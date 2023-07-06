import java.util.ArrayList;
import java.util.List;

class Player {
  private String name;
  private List<Ship> ships;
  private List<Coordinate> shotsFired;

  public Player(String name) {
    this.name = name;
    ships = new ArrayList<>();
    shotsFired = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public List<Ship> getShips() {
    return ships;
  }

  public List<Coordinate> getShotsFired() {
    return shotsFired;
  }

  public void addShip(Ship ship) {
    ships.add(ship);
  }

  public void addShotFired(Coordinate coordinate) {
    shotsFired.add(coordinate);
  }
}
