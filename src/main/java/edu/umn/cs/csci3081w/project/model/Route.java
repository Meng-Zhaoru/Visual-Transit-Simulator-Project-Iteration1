package edu.umn.cs.csci3081w.project.model;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Route {
  public static final String BUS_LINE = "BUS_LINE";
  public static final String TRAIN_LINE = "TRAIN_LINE";
  private int id;
  private String lineName;
  private String lineType;
  private String name;
  private List<Stop> stops = new ArrayList<Stop>();
  private List<Double> distances = new ArrayList<Double>();
  private int destinationStopIndex;
  private Stop destinationStop;
  private PassengerGenerator generator;

  /**
   * Route constructor.
   *
   * @param id  identifier for route
   * @param lineName  name of the line
   * @param lineType  line type
   * @param name      name of route
   * @param stops     stops on this route
   * @param distances distances between subsequent stops of this route
   * @param generator Passenger generating object
   */
  public Route(int id, String lineName, String lineType, String name, List<Stop> stops,
               List<Double> distances, PassengerGenerator generator) {
    this.id = id;
    this.lineName = lineName;
    this.lineType = lineType;
    this.name = name;
    for (int i = 0; i < stops.size(); i++) {
      this.stops.add(stops.get(i));
    }
    for (int i = 0; i < stops.size() - 1; i++) {
      this.distances.add(distances.get(i));
    }
    this.generator = generator;
    this.destinationStopIndex = 0;
    this.destinationStop = stops.get(0);
  }

  /**
   * Shallow copies a route.
   * This method shallow copies a route as stops are shared
   * across copied routes
   *
   * @return Copy of route
   */
  public Route shallowCopy() {
    List<Stop> stops = new ArrayList<Stop>();
    for (int i = 0; i < this.stops.size(); i++) {
      stops.add(this.stops.get(i));
    }
    List<Double> distances = new ArrayList<Double>();
    for (int i = 0; i < this.stops.size() - 1; i++) {
      distances.add(this.distances.get(i));
    }
    Route shallowCopy = new Route(this.id, this.lineName, this.lineType, this.name, stops,
        distances, this.generator);
    return shallowCopy;
  }

  /**
   * Updates and generates passengers on the route.
   */
  public void update() {
    generateNewPassengers();
    Iterator<Stop> stopIter = this.stops.iterator();
    while (stopIter.hasNext()) {
      stopIter.next().update();
    }
  }

  /**
   * Report statistics for the route.
   *
   * @param out stream for printing
   */
  public void report(PrintStream out) {
    out.println("####Route Info Start####");
    out.println("ID: " + this.id);
    out.println("Line name: " + this.lineName);
    out.println("Line type: " + this.lineType);
    out.println("Name: " + this.name);
    out.println("Num stops: " + this.stops.size());
    int stopCounter = 0;
    Iterator<Stop> stopIter = this.stops.iterator();
    out.println("****Stops Info Start****");
    //calling all Stop's report methods
    while (stopIter.hasNext()) {
      if (stopCounter == this.destinationStopIndex) {
        out.println("++++Next Stop Info Start++++");
      }
      stopIter.next().report(out);
      if (stopCounter == this.destinationStopIndex) {
        out.println("++++Next Stop Info End++++");
      }
      stopCounter++;
    }
    out.println("****Stops Info End****");
    out.println("####Route Info End####");
  }

  public boolean isAtEnd() {
    return destinationStopIndex >= stops.size();
  }

  /**
   * Returns previous stop.
   *
   * @return previous stop
   */
  public Stop prevStop() {
    if (destinationStopIndex == 0) {
      return this.stops.get(0);
    } else if (destinationStopIndex < stops.size()) {
      return this.stops.get(destinationStopIndex - 1);
    } else {
      return this.stops.get(stops.size() - 1);
    }
  }

  /**
   * Updates destinationStop to next stop.
   */
  public void nextStop() {
    destinationStopIndex++;
    if (destinationStopIndex < stops.size()) {
      destinationStop = stops.get(destinationStopIndex);
    } else {
      destinationStop = stops.get(stops.size() - 1);
    }
  }

  /**
   * Returns destination stop.
   *
   * @return destination Stop
   */
  public Stop getDestinationStop() {
    return destinationStop;
  }

  /**
   * Computes distance to next stop.
   *
   * @return distance
   */
  public Double getNextStopDistance() {
    if (destinationStopIndex > 0) {
      return distances.get(destinationStopIndex - 1);
    } else {
      return 0.0;
    }
  }

  /**
   * Returns and generates passengers.
   *
   * @return number of generated passengers
   */
  public int generateNewPassengers() {
    // returning number of passengers added by generator
    return this.generator.generatePassengers();
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<Stop> getStops() {
    return stops;
  }

  public int getDestinationStopIndex() {
    return destinationStopIndex;
  }

  public String getLineName() {
    return lineName;
  }

  public String getLineType() {
    return lineType;
  }
}