package Logic;

/**
 * Created by Shlomi on 04/12/2016.
 */

public class Board {
    private int rows;
    private int cols;
    private int bombs;
    private Tile[][] allTiles;
    private  int numOfFlagsLogic;
    // public enum types {ZERO,ONE,TWO,THREE,FOUR,FIVE,SIX,SEVEN,EIGHT,NINE,BOBM};


    public Board(int rows , int cols , int bombs){
        this.rows=rows;
        this.cols=cols;
        this.bombs=bombs;
        allTiles=new Tile[rows][cols];
        for (int i = 0 ; i < rows ; i++){
            for (int j = 0 ;  j < cols ; j++){
                allTiles[i][j]=new Tile(i,j,0,true,false);
            }
        }

        numOfFlagsLogic = 0;
    }


    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getBombs() {
        return bombs;
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
    }

    public Tile[][] getAllTiles() {
        return allTiles;
    }

    public int getNumOfFlagsLogic() {
        return numOfFlagsLogic;
    }

    public void setNumOfFlagsLogic(int numOfFlagsLogic) {
        this.numOfFlagsLogic = numOfFlagsLogic;
    }


    public void initLogicBoard() {
        int numOfBombs = 0;
        while (numOfBombs<bombs) {
            int randX = (int) (Math.random() * (rows - 1));
            int randY = (int) (Math.random() * (cols - 1));
            if (allTiles[randX][randY].getType() != 10) {
                allTiles[randX][randY].setType(10);

                for (int m = randX - 1; m <= randX + 1; m++) {
                    for (int n = randY - 1; n <= randY + 1; n++) {
                        if ((m < rows && n < cols) && (m >= 0 && n >= 0)) {
                            if (allTiles[m][n].getType() != 10)
                                allTiles[m][n].setType(allTiles[m][n].getType() + 1);
                        }
                    }
                }
                numOfBombs++;

            }
        }
    }


    public int  makeStep (int btnId, boolean isFlagUI){
        int indexY = btnId%cols;
        int indexX = btnId/cols;
        if (!isFlagUI)
            makeStepAuto(indexX,indexY);
        else {
            allTiles[indexX][indexY].setFlag(!allTiles[indexX][indexY].isFlag());
            if (!allTiles[indexX][indexY].isFlag())
                numOfFlagsLogic += 1;
            else
                numOfFlagsLogic -= 1;
        }
        return checksStatus(indexX,indexY);

    }

    public void makeStepAuto (int indexX, int indexY){
        if ((indexX >=0 && indexX<rows && indexY>=0 && indexY<cols) && (allTiles[indexX][indexY].isCover()) ) {
            allTiles[indexX][indexY].setCover(false);
            if (allTiles[indexX][indexY].getType()==0) {
                makeStepAuto(indexX - 1, indexY);
                makeStepAuto(indexX, indexY - 1);
                makeStepAuto(indexX, indexY + 1);
                makeStepAuto(indexX + 1, indexY);
            }
        }
    }



    public int checksStatus(int indexX, int indexY) {
        if (allTiles[indexX][indexY].getType() == 10) {
            return -1;
        }
        int numOfCoveredTiles = 0;


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (allTiles[i][j].isCover() == true)
                    numOfCoveredTiles++;
            }
        }

        if (numOfCoveredTiles==bombs)
            return 1;
        return  0;
    }

}
