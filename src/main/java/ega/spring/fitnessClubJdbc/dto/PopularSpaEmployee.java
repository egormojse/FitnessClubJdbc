package ega.spring.fitnessClubJdbc.dto;

public class PopularSpaEmployee {
    private String name;
    private int count;

    public PopularSpaEmployee(String name, int count) {
        this.name = name;
        this.count = count;
    }

    // getters и setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
