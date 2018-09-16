package objects;

public enum Maneuver {

    TSpin("TSpin",400),
    TSpinSingle("TSpin Single",800 ),
    TSpinDouble("TSpin Double",1200 ),
    Tetris("Tetris",1200),
    ClearAll("Cleared Matrix",1200 );

    private final String name;
    private final int points;

    Maneuver(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name.toUpperCase();
    }

    public int getPoints() {
        return points;
    }

}
