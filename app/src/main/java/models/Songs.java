package models;

public abstract class Songs {

    private String title;
    private String resourceName;
    private int resourceID;

    public final String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        this.title = title;
    }

    public final String getResourceName() {
        return resourceName;
    }

    public final void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public final int getResourceID() {
        return resourceID;
    }

    public final void setResourceID(int resourceID) {
        this.resourceID = resourceID;
    }
}
