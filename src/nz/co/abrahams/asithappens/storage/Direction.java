/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.abrahams.asithappens.storage;

/**
 *
 * @author mark
 */
public enum Direction {
    
    In(0), Out(1), Both(2);
    
    private Direction(int listPosition) {
        this.listPosition = listPosition;
    }
    
    public static Direction getDirectionFromListPosition(int position) {
        for (Direction direction : Direction.values()) {
            if (direction.listPosition == position) {
                return direction;
            }
        }
        return null;
    }
    
    public static Direction getDirectionFromOIDIndex(int index) {
        return Direction.getDirectionFromListPosition(index - 1);
    }
    
    public static String[] getLabels() {
        String[] labels;
        
        labels = new String[Direction.values().length];
        for (int i = 0; i < Direction.values().length; i++) {
            labels[i] = Direction.getDirectionFromListPosition(i).toString();
        }
        return labels;
    }
    
    public int getListPosition() {
        return listPosition;
    }
    
    public int getOIDIndex() {
        return listPosition + 1;
    }
    
    private int listPosition;
}
