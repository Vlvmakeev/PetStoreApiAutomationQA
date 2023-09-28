package petStore.api.Models.petinstoremodels;

public class TagForRoot {
    public int id;
    public String name;

    public TagForRoot() {
    }

    public TagForRoot(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
