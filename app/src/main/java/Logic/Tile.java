package Logic;

/**
 * Created by Shlomi on 04/12/2016.
 */

public class Tile {
    private int x;
    private int y;
    private int type;
    private boolean isCover;
    private boolean isFlag;

    public Tile (int x, int y ,int type, boolean isCover,boolean isFlag){
        this.x=x;
        this.y=y;
        this.type=type;
        this.isCover=isCover;
        this.isFlag=isFlag;
    }


    public boolean isFlag() {
        return isFlag;
    }

    public void setFlag(boolean flag) {
        isFlag = flag;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isCover() {
        return isCover;
    }

    public void setCover(boolean cover) {
        isCover = cover;
    }
}

