package models;


public interface Rectangle {

    String getId();

    void setId(String id);

    String getType();

    void setType(String type);

    Double getX();

    void setX(Double x);

    Double getY();

    void setY(Double y);

    Double getH();

    void setH(Double h);

    Double getW();

    void setW(Double w);

    boolean getBoundary();

    void setBoundary(boolean boundary);

    Node getNode();

    void setNode(Node node);
}
