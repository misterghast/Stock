package org.hypbase.stock.item;

public class ModelDataRange {
    private int rangeStart;
    private int maxRange;

    private int latestId;
    private String namespace;

    public ModelDataRange(String namespace, int maxRange) {
        this.namespace = namespace;
        this.maxRange = maxRange;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public String getNamespace() {
        return this.namespace;
    }

    public int getRangeStart() {
        return rangeStart;
    }

    public void setRangeStart(int number) {
        this.rangeStart = number;
    }

    public boolean inRange(int number) {
        if(number >= rangeStart && number <= maxRange) {
            return true;
        }

        return false;
    }

    public boolean overlaps(ModelDataRange other) {
        if(this.getRangeStart() <= other.getMaxRange() && this.getMaxRange() >= other.getRangeStart()) {
            return true;
        } else {
            return false;
        }
    }

}
