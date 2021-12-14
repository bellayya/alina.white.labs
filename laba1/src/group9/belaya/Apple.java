package group9.belaya;

public class Apple extends Food {
    private String size;
    public Apple(String size) {
        super("Apple");
        this.size = size;
    }

    public void consume() {
        System.out.println(this + " ate");
    }


    public String toString() {
        return super.toString() + " size '" + size.toUpperCase() + "'";
    }
}
