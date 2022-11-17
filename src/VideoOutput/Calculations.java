package VideoOutput;

public class Calculations {

//    Variables
    int[] mode; // 1 = 4x4, 2 = 16x16, 3 = 16x32, 4 = 32x64
    int numPanelsWide;
    int numPanelsTall;



//    Constructor
    public Calculations(int mode, int numPanelsWide, int numPanelsTall) {
        this.mode = setMode(mode);
        this.numPanelsWide = numPanelsWide;
        this.numPanelsTall = numPanelsTall;
    }

//    Getters
    public int[] getMode() {
        return mode;
    }
    public int getNumPanelsWide() {
        return numPanelsWide;
    }
    public int getNumPanelsTall() {
        return numPanelsTall;
    }

//    Setter for mode and cleans input
    public int[] setMode(int mode){
        return switch (mode) {
            case 1 -> new int[]{4, 4};
            case 2 -> new int[]{16, 16};
            case 3 -> new int[]{16, 32};
            default -> new int[]{32, 64};
        };
    }




}
