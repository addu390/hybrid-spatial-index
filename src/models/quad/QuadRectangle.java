package models.quad;


public class QuadRectangle extends AbstractRectangle {
    public QuadRectangle(){
        this.setX(0.0);
        this.setY(0.0);
        this.setW(0.0);
        this.setH(0.0);
    }

    public QuadRectangle(Double x, Double y, Double w, Double h) {
        super(x,y,w,h);
        this.setType("quad-rectangle");
        this.setId("ba737af4-51f2-4506-b7a8-cb581efe06aa");
    }

    public QuadRectangle(Double x, Double y, Double w, Double h, String id) {
        super(x,y,w,h);
        this.setId(id);
    }
}
