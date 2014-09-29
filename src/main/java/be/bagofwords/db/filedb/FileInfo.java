package be.bagofwords.db.filedb;

import be.bagofwords.util.Pair;

import java.util.Collections;
import java.util.List;

class FileInfo implements Comparable {

    private final long firstKey;
    private int size;
    private byte[] cachedFileContents;
    //This field is only filled in when the file is clean (i.e. not isDirty)
    private List<Pair<Long, Integer>> fileLocations;

    public FileInfo(long firstKey, int size) {
        this.firstKey = firstKey;
        this.size = size;
        if (size == 0) {
            fileLocations = Collections.emptyList();
        }
    }

    public long getFirstKey() {
        return firstKey;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isDirty() {
        return fileLocations == null;
    }

    public void fileIsCleaned(List<Pair<Long, Integer>> fileLocations) {
        this.fileLocations = fileLocations;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof FileInfo) {
            return Long.compare(getFirstKey(), ((FileInfo) o).getFirstKey());
        } else if (o instanceof Long) {
            return Long.compare(getFirstKey(), (Long) o);
        } else {
            throw new RuntimeException("Can not compare FileInfo with " + o);
        }
    }

    public String toString() {
        return super.toString() + " " + getFirstKey() + " " + getSize() + " " + isDirty();
    }

    public void increaseSize(long diff, boolean cleanWrite) {
        this.size += diff;
        if (!cleanWrite) {
            this.fileLocations = null;
        }
    }

    public List<Pair<Long, Integer>> getFileLocations() {
        return fileLocations;
    }

    public long discardFileContents() {
        int bytesReleased = 0;
        if (cachedFileContents != null) {
            bytesReleased = cachedFileContents.length;
            cachedFileContents = null;
        }
        return bytesReleased;
    }

    public byte[] getCachedFileContents() {
        return cachedFileContents;
    }

    public void setCachedFileContents(byte[] cachedFileContents) {
        this.cachedFileContents = cachedFileContents;
    }

}
