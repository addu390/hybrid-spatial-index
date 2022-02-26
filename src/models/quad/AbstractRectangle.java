package models.quad;

import models.KDNode;

public abstract class AbstractRectangle implements Rectangle {

    private String id;
    private String type;
    private Double x;
    private Double y;
    private Double h;
    private Double w;

    // TODO: Create getter and setter
    private KDNode node;
    private boolean isBBox;

    public AbstractRectangle(){}
    public AbstractRectangle(Double x, Double y, Double w, Double h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getH() {
        return h;
    }

    public void setH(Double h) {
        this.h = h;
    }

    public Double getW() {
        return w;
    }

    public void setW(Double w) {
        this.w = w;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
