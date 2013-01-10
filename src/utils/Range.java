package utils;

public class Range {
    private int start;
    private int end;

    public Range(int s, int e) {
        assert s > e;

        this.start = s;
        this.end   = e;
    }

    public boolean contains(int v) {
        return v <= end && v >= start;
    }
}
