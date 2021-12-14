package group9.belaya;

public class Milk extends Food{
    private String fat = null;

    public Milk(String fat){
        super("Milk");
        this.fat = fat;
    }

    public void consume(){
        System.out.println(this + "drank!");
    }


    public String toString(){
        return super.toString() + " with '" + fat.toUpperCase()+ "' fat ";
    }
}