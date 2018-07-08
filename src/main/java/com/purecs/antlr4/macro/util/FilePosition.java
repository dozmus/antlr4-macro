package com.purecs.antlr4.macro.util;

import java.util.Objects;

public class FilePosition {

    private int startIdx;
    private int endIdx;

    public FilePosition(int startIdx, int endIdx) {
        this.startIdx = startIdx;
        this.endIdx = endIdx;
    }

    public int getStartIdx() {
        return startIdx;
    }

    public void setStartIdx(int startIdx) {
        this.startIdx = startIdx;
    }

    public int getEndIdx() {
        return endIdx;
    }

    public void setEndIdx(int endIdx) {
        this.endIdx = endIdx;
    }

    public FilePosition subtract(int delta) {
        return new FilePosition(startIdx - delta, endIdx - delta);
    }

    /**
     * Returns exclusive length.
     */
    public int length() {
        return endIdx - startIdx;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilePosition that = (FilePosition) o;
        return startIdx == that.startIdx && endIdx == that.endIdx;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startIdx, endIdx);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FilePosition{");
        sb.append("startIdx=").append(startIdx);
        sb.append(", endIdx=").append(endIdx);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Object clone() {
        return new FilePosition(startIdx, endIdx);
    }
}
