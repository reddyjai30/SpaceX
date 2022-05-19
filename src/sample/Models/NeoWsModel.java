package sample.Models;

public class NeoWsModel {


    private String newsDate;
    private String name;                  // asteroid name
    private Double absoluteMagnitude;
    private Double minDiameter , maxDiameter;   // in meters;
    private Boolean isPotentiallyHazardous;
    private String approachDate;
    private String approachVelocity;   // in km/sec
    private String missDistance;   // km
    private String orbitingBody;


    public String getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAbsoluteMagnitude() {
        return absoluteMagnitude;
    }

    public void setAbsoluteMagnitude(Double absoluteMagnitude) {
        this.absoluteMagnitude = absoluteMagnitude;
    }

    public Double getMinDiameter() {
        return minDiameter;
    }

    public void setMinDiameter(Double minDiameter) {
        this.minDiameter = minDiameter;
    }

    public Double getMaxDiameter() {
        return maxDiameter;
    }

    public void setMaxDiameter(Double maxDiameter) {
        this.maxDiameter = maxDiameter;
    }

    public Boolean getPotentiallyHazardous() {
        return isPotentiallyHazardous;
    }

    public void setPotentiallyHazardous(Boolean potentiallyHazardous) {
        isPotentiallyHazardous = potentiallyHazardous;
    }

    public String getApproachDate() {
        return approachDate;
    }

    public void setApproachDate(String approachDate) {
        this.approachDate = approachDate;
    }

    public String getApproachVelocity() {
        return approachVelocity;
    }

    public void setApproachVelocity(String approachVelocity) {
        this.approachVelocity = approachVelocity;
    }

    public String getMissDistance() {
        return missDistance;
    }

    public void setMissDistance(String missDistance) {
        this.missDistance = missDistance;
    }

    public String getOrbitingBody() {
        return orbitingBody;
    }

    public void setOrbitingBody(String orbitingBody) {
        this.orbitingBody = orbitingBody;
    }

}
