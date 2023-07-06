import java.util.ArrayList;
import java.util.List;

class ShipImpl implements Ship {
  private List<Coordinate> coordinates;

  public ShipImpl(Coordinate coordinate) {
    coordinates = new ArrayList<>();
    coordinates.add(coordinate);
  }

  @Override
  public boolean isHit(Coordinate coordinate) {
    for (Coordinate c : coordinates) {
      if (c.getX() == coordinate.getX() && c.getY() == coordinate.getY()) {
        coordinates.remove(c);
        return true;
      }
    }
    return false;
  }

  @Override
  public void hit(Coordinate coordinate) {
    coordinates.remove(coordinate);
  }
}
